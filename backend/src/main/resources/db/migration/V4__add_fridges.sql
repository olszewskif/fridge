create table fridges (
    id          UUID PRIMARY KEY,
    version     INT NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ,

    name        VARCHAR(255) NOT NULL,
    created_by  UUID NOT NULL
);
