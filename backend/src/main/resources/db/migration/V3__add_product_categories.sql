create table product_categories (
    product_id UUID NOT NULL REFERENCES products (id) ON DELETE CASCADE,
    category_id UUID NOT NULL REFERENCES categories (id),
    PRIMARY KEY (product_id, category_id)
);

create index idx_product_categories_category_id on product_categories (category_id);