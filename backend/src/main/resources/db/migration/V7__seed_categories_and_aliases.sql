CREATE TABLE category_aliases (
    category_id UUID NOT NULL REFERENCES categories (id) ON DELETE CASCADE,
    alias VARCHAR(255) NOT NULL,
    PRIMARY KEY (category_id, alias)
);

CREATE INDEX idx_category_aliases_alias ON category_aliases (LOWER(alias));
CREATE UNIQUE INDEX IF NOT EXISTS idx_categories_name_lower ON categories (LOWER(name));

-- 1. Dairy & Eggs
INSERT INTO categories (id, name, parent_id, created_at, version)
VALUES ('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'Dairy & Eggs', NULL, now(), 0)
ON CONFLICT (id) DO NOTHING;

INSERT INTO category_aliases (category_id, alias) VALUES
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'dairy'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'dairies'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'milk'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'milks'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'whole milk'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'cheese'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'cheeses'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'cheddar'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'mozzarella'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'parmesan'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'gouda'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'yogurt'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'yogurts'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'yoghurt'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'butter'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'cream'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'sour cream'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'egg'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'eggs'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'kefir'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'quark'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'nabiał'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'mleko'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'ser'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'sery'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'twaróg'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'jogurt'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'masło'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'śmietana'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'jaja'),
('34298d05-bc20-4fab-9c80-85836f7c3d1d', 'jajka')
ON CONFLICT DO NOTHING;

-- 2. Fruits & Vegetables
INSERT INTO categories (id, name, parent_id, created_at, version)
VALUES ('b8adf9f3-ab72-40be-839c-d474517310b9', 'Fruits & Vegetables', NULL, now(), 0)
ON CONFLICT (id) DO NOTHING;

INSERT INTO category_aliases (category_id, alias) VALUES
('b8adf9f3-ab72-40be-839c-d474517310b9', 'fruit'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'fruits'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'vegetable'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'vegetables'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'produce'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'salad'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'salads'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'apple'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'apples'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'banana'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'bananas'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'orange'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'lemon'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'tomato'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'tomatoes'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'potato'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'potatoes'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'onion'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'carrot'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'carrots'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'cucumber'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'cucumbers'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'berry'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'berries'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'herbs'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'owoce'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'warzywa'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'jabłko'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'pomidor'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'ziemniaki'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'ogórek'),
('b8adf9f3-ab72-40be-839c-d474517310b9', 'marchew')
ON CONFLICT DO NOTHING;

-- 3. Meat & Seafood
INSERT INTO categories (id, name, parent_id, created_at, version)
VALUES ('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'Meat & Seafood', NULL, now(), 0)
ON CONFLICT (id) DO NOTHING;

INSERT INTO category_aliases (category_id, alias) VALUES
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'meat'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'meats'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'poultry'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'chicken'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'beef'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'pork'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'turkey'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'fish'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'fishes'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'seafood'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'salmon'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'tuna'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'sausage'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'sausages'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'ham'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'bacon'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'salami'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'mięso'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'drób'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'kurczak'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'wołowina'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'wieprzowina'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'ryba'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'ryby'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'kiełbasa'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'szynka'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'wędlina'),
('eea5c624-6084-4ba4-94a8-4c2a7bcaa66f', 'parówki')
ON CONFLICT DO NOTHING;

-- 4. Prepared & Leftovers
INSERT INTO categories (id, name, parent_id, created_at, version)
VALUES ('36a19a85-7233-4ae0-988c-36463d596aae', 'Prepared & Leftovers', NULL, now(), 0)
ON CONFLICT (id) DO NOTHING;

INSERT INTO category_aliases (category_id, alias) VALUES
('36a19a85-7233-4ae0-988c-36463d596aae', 'prepared'),
('36a19a85-7233-4ae0-988c-36463d596aae', 'prepared meals'),
('36a19a85-7233-4ae0-988c-36463d596aae', 'ready meals'),
('36a19a85-7233-4ae0-988c-36463d596aae', 'ready to eat'),
('36a19a85-7233-4ae0-988c-36463d596aae', 'meal'),
('36a19a85-7233-4ae0-988c-36463d596aae', 'meals'),
('36a19a85-7233-4ae0-988c-36463d596aae', 'soup'),
('36a19a85-7233-4ae0-988c-36463d596aae', 'soups'),
('36a19a85-7233-4ae0-988c-36463d596aae', 'pizza'),
('36a19a85-7233-4ae0-988c-36463d596aae', 'sandwich'),
('36a19a85-7233-4ae0-988c-36463d596aae', 'sandwiches'),
('36a19a85-7233-4ae0-988c-36463d596aae', 'dania gotowe'),
('36a19a85-7233-4ae0-988c-36463d596aae', 'danie gotowe'),
('36a19a85-7233-4ae0-988c-36463d596aae', 'zupa'),
('36a19a85-7233-4ae0-988c-36463d596aae', 'zupy')
ON CONFLICT DO NOTHING;

-- 5. Beverages
INSERT INTO categories (id, name, parent_id, created_at, version)
VALUES ('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'Beverages', NULL, now(), 0)
ON CONFLICT (id) DO NOTHING;

INSERT INTO category_aliases (category_id, alias) VALUES
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'beverage'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'beverages'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'drink'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'drinks'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'juice'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'juices'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'soda'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'sodas'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'water'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'waters'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'tea'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'coffee'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'beer'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'wine'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'plant based milk'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'oat milk'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'almond milk'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'soy milk'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'napój'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'napoje'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'sok'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'soki'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'woda'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'herbata'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'kawa'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'piwo'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'wino')
ON CONFLICT DO NOTHING;

-- 6. Bakery
INSERT INTO categories (id, name, parent_id, created_at, version)
VALUES ('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'Bakery', NULL, now(), 0)
ON CONFLICT (id) DO NOTHING;

INSERT INTO category_aliases (category_id, alias) VALUES
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'bakery'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'bread'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'breads'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'baguette'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'bun'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'buns'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'roll'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'rolls'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'bagel'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'toast'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'pastry'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'pastries'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'croissant'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'tortilla'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'pieczywo'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'chleb'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'bułka'),
('3f003ce2-b482-4ac1-b3b8-7610948ff2f7', 'bułki')
ON CONFLICT DO NOTHING;

-- 7. Condiments & Sauces
INSERT INTO categories (id, name, parent_id, created_at, version)
VALUES ('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'Condiments & Sauces', NULL, now(), 0)
ON CONFLICT (id) DO NOTHING;

INSERT INTO category_aliases (category_id, alias) VALUES
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'condiment'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'condiments'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'sauce'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'sauces'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'dressing'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'ketchup'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'mustard'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'mayonnaise'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'mayo'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'oil'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'oils'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'vinegar'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'jam'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'honey'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'dip'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'sos'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'keczup'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'musztarda'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'majonez'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'oliwa'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'olej'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'dżem'),
('b8e86c4e-ba9c-4d3e-814d-502de2c67d51', 'miód')
ON CONFLICT DO NOTHING;

-- 8. Pantry & Grains
INSERT INTO categories (id, name, parent_id, created_at, version)
VALUES ('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'Pantry & Grains', NULL, now(), 0)
ON CONFLICT (id) DO NOTHING;

INSERT INTO category_aliases (category_id, alias) VALUES
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'pantry'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'grain'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'grains'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'pasta'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'noodles'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'rice'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'flour'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'cereal'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'cereals'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'oats'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'beans'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'lentils'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'canned'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'makaron'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'ryż'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'kasza'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'mąka'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'płatki'),
('e032b59a-cc9a-467d-a898-0d7cf92cc97f', 'konserwa')
ON CONFLICT DO NOTHING;

-- 9. Snacks & Sweets
INSERT INTO categories (id, name, parent_id, created_at, version)
VALUES ('149e5a2b-2fbf-4800-881a-4709403919f0', 'Snacks & Sweets', NULL, now(), 0)
ON CONFLICT (id) DO NOTHING;

INSERT INTO category_aliases (category_id, alias) VALUES
('149e5a2b-2fbf-4800-881a-4709403919f0', 'snack'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'snacks'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'sweet'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'sweets'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'candy'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'chocolate'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'chocolates'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'cookie'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'cookies'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'biscuit'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'chip'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'chips'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'crisps'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'nuts'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'popcorn'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'przekąski'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'słodycze'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'czekolada'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'ciastka'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'chipsy'),
('149e5a2b-2fbf-4800-881a-4709403919f0', 'orzechy')
ON CONFLICT DO NOTHING;

-- 10. Frozen
INSERT INTO categories (id, name, parent_id, created_at, version)
VALUES ('2b007183-7cb1-47f1-a800-e9ae3dbe2fd6', 'Frozen', NULL, now(), 0)
ON CONFLICT (id) DO NOTHING;

INSERT INTO category_aliases (category_id, alias) VALUES
('2b007183-7cb1-47f1-a800-e9ae3dbe2fd6', 'frozen'),
('2b007183-7cb1-47f1-a800-e9ae3dbe2fd6', 'frozen foods'),
('2b007183-7cb1-47f1-a800-e9ae3dbe2fd6', 'ice cream'),
('2b007183-7cb1-47f1-a800-e9ae3dbe2fd6', 'sorbet'),
('2b007183-7cb1-47f1-a800-e9ae3dbe2fd6', 'mrożonki'),
('2b007183-7cb1-47f1-a800-e9ae3dbe2fd6', 'lody')
ON CONFLICT DO NOTHING;

-- 11. Other
INSERT INTO categories (id, name, parent_id, created_at, version)
VALUES ('af31dba0-edf0-4b43-ae53-b587156ad037', 'Other', NULL, now(), 0)
ON CONFLICT (id) DO NOTHING;

INSERT INTO category_aliases (category_id, alias) VALUES
('af31dba0-edf0-4b43-ae53-b587156ad037', 'other'),
('af31dba0-edf0-4b43-ae53-b587156ad037', 'inne')
ON CONFLICT DO NOTHING;
