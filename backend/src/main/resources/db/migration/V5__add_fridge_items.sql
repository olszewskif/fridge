create table fridge_items (
    id          UUID PRIMARY KEY,
    version     INT NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ,

    fridge_id   UUID NOT NULL REFERENCES fridges (id) ON DELETE CASCADE,
    product_id  UUID NOT NULL REFERENCES products (id),
    added_by    UUID NOT NULL,
    added_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    quantity    INT NOT NULL,
    unit        varchar(255) NOT NULL,
    expiry_date DATE
);

create index idx_fridge_items_fridge_id on fridge_items (fridge_id);