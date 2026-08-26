-- Un libelle ne peut plus apparaitre deux fois, ni dans products ni dans type_produits.
-- La comparaison ignore la casse : "Laptop" et "laptop" sont le meme produit.

-- 1. Dedoublonnage des types : on garde une ligne par libelle, et les produits
--    qui pointaient vers les doublons sont rattaches a celle conservee.
WITH gardes AS (
    SELECT DISTINCT ON (lower(libelle)) id, lower(libelle) AS cle
      FROM type_produits
     ORDER BY lower(libelle), id
)
UPDATE products p
   SET type_produit_id = g.id
  FROM type_produits t
  JOIN gardes g ON g.cle = lower(t.libelle)
 WHERE p.type_produit_id = t.id
   AND p.type_produit_id <> g.id;

WITH gardes AS (
    SELECT DISTINCT ON (lower(libelle)) id
      FROM type_produits
     ORDER BY lower(libelle), id
)
DELETE FROM type_produits
 WHERE id NOT IN (SELECT id FROM gardes);

-- 2. Dedoublonnage des produits. A libelle egal on conserve en priorite
--    celui qui a une categorie, l'information est plus complete.
WITH gardes AS (
    SELECT DISTINCT ON (lower(libelle)) id
      FROM products
     ORDER BY lower(libelle), (type_produit_id IS NULL), id
)
DELETE FROM products
 WHERE id NOT IN (SELECT id FROM gardes);

-- 3. Index uniques sur l'expression : PostgreSQL n'accepte pas de contrainte
--    UNIQUE sur une fonction, un index unique joue le meme role.
CREATE UNIQUE INDEX uk_type_produits_libelle ON type_produits (lower(libelle));
CREATE UNIQUE INDEX uk_products_libelle      ON products      (lower(libelle));
