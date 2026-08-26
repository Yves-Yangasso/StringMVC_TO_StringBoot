-- Passage des cles primaires de BIGSERIAL a UUID, sans perte de donnees :
-- on cree les colonnes UUID a cote, on reporte les liens, puis on bascule.

-- 1. Nouvelles colonnes, remplies pour les lignes deja presentes
ALTER TABLE type_produits ADD COLUMN new_id UUID NOT NULL DEFAULT gen_random_uuid();
ALTER TABLE products      ADD COLUMN new_id UUID NOT NULL DEFAULT gen_random_uuid();
ALTER TABLE products      ADD COLUMN new_type_produit_id UUID;

-- 2. Report de la relation produit -> type sur les nouveaux identifiants
UPDATE products p
   SET new_type_produit_id = t.new_id
  FROM type_produits t
 WHERE p.type_produit_id = t.id;

-- 3. Suppression de l'ancienne cle etrangere. Son nom differe selon les bases :
--    genere par Hibernate sur celles creees avant Flyway, explicite depuis V1.
DO $$
DECLARE nom text;
BEGIN
    FOR nom IN
        SELECT conname FROM pg_constraint
         WHERE conrelid = 'products'::regclass AND contype = 'f'
    LOOP
        EXECUTE format('ALTER TABLE products DROP CONSTRAINT %I', nom);
    END LOOP;
END $$;

DROP INDEX IF EXISTS idx_products_type_produit_id;

-- 4. Bascule : les anciennes colonnes disparaissent (leur PK avec elles)
ALTER TABLE products      DROP COLUMN type_produit_id;
ALTER TABLE products      DROP COLUMN id;
ALTER TABLE type_produits DROP COLUMN id;

ALTER TABLE products      RENAME COLUMN new_id TO id;
ALTER TABLE products      RENAME COLUMN new_type_produit_id TO type_produit_id;
ALTER TABLE type_produits RENAME COLUMN new_id TO id;

-- Les UUID sont generes cote Java par Hibernate : le defaut SQL n'a plus lieu d'etre
ALTER TABLE products      ALTER COLUMN id DROP DEFAULT;
ALTER TABLE type_produits ALTER COLUMN id DROP DEFAULT;

ALTER TABLE type_produits ADD PRIMARY KEY (id);
ALTER TABLE products      ADD PRIMARY KEY (id);

ALTER TABLE products ADD CONSTRAINT fk_products_type_produit
    FOREIGN KEY (type_produit_id) REFERENCES type_produits (id);

CREATE INDEX idx_products_type_produit_id ON products (type_produit_id);

-- 5. Convergence : les bases creees avant Flyway laissaient libelle nullable,
--    alors que V1 le declare NOT NULL. Sans effet sur une base neuve.
ALTER TABLE products      ALTER COLUMN libelle SET NOT NULL;
ALTER TABLE type_produits ALTER COLUMN libelle SET NOT NULL;
