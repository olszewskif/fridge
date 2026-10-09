create table products (
    id          UUID PRIMARY KEY,
    version     INT NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ,

    name        VARCHAR(255) NOT NULL,
    brand       VARCHAR(255),
    barcode     VARCHAR(255) NOT NULL UNIQUE,
    image_url   TEXT
);