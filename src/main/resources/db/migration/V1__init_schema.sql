-- Schema initial : reprend a l'identique celui genere par Hibernate (ddl-auto=update).
-- Sur une base deja existante, Flyway pose un baseline et saute ce script.

CREATE TABLE type_produits (
    id      BIGSERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

CREATE TABLE products (
    id              BIGSERIAL PRIMARY KEY,
    libelle         VARCHAR(255) NOT NULL,
    prix            DOUBLE PRECISION NOT NULL,
    type_produit_id BIGINT NOT NULL,
    CONSTRAINT fk_products_type_produit
        FOREIGN KEY (type_produit_id) REFERENCES type_produits (id)
);

CREATE INDEX idx_products_type_produit_id ON products (type_produit_id);
