create table categories (
    id          UUID PRIMARY KEY,
    version     INT NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ,

    name        VARCHAR(255) NOT NULL,
    parent_id   UUID REFERENCES categories (id)
);

create index idx_categories_parent_id on categories (parent_id);
