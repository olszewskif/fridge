alter table products
add column if not exists amount double precision,
add column if not exists unit VARCHAR(255);

alter table fridge_items
drop column if exists unit cascade;

alter table fridge_items
add check (quantity > 0);

alter table fridge_items
alter column quantity set default 1;