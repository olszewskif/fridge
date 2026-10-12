-- V8: Add additional aliases for water and beverages (e.g. Żywiec Zdrój, mineral waters, carbonation keywords)
INSERT INTO category_aliases (category_id, alias) VALUES
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'zdrój'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'zdroj'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'żywiec'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'zywiec'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'mineralna'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'źródlana'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'zrodlana'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'niegazowana'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'niegazowany'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'gazowana'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'gazowany'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'muszynianka'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'cisowianka'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'nałęczowianka'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'naleczowianka'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'kropla'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'rodowita'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'sparkling water'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'still water'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'mineral water'),
('a8cdbe34-cc8f-47aa-a060-57b98692e29a', 'spring water')
ON CONFLICT DO NOTHING;
