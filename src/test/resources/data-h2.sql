-- Products
INSERT INTO products (product_id, name, price, stock_quantity, category, created_at)
VALUES (1, 'Test Coke 500ml', 70, 100, 'Beverages', CURRENT_TIMESTAMP);

INSERT INTO products (product_id, name, price, stock_quantity, category, created_at)
VALUES (2, 'Test Lemon Rum 500ml', 180, 50, 'Alcoholic Beverages', CURRENT_TIMESTAMP);

INSERT INTO products (product_id, name, price, stock_quantity, category, created_at)
VALUES (3, 'Gulab Jamun Box', 250, 200, 'Sweets', CURRENT_TIMESTAMP);

INSERT INTO products (product_id, name, price, stock_quantity, category, created_at)
VALUES (4, 'Pepsi 1L', 90, 150, 'Beverages', CURRENT_TIMESTAMP);

INSERT INTO products (product_id, name, price, stock_quantity, category, created_at)
VALUES (5, 'Whiskey Premium 750ml', 1200, 50, 'Alcoholic Beverages', CURRENT_TIMESTAMP);

INSERT INTO products (product_id, name, price, stock_quantity, category, created_at)
VALUES (6, 'Tic Tac Mint Pack', 30, 300, 'Confectionary', CURRENT_TIMESTAMP);

----------------------------------------------------

-- Insert Orders
INSERT INTO orders (order_id, order_date, status, total_amount, payment_status)
VALUES (1, CURRENT_TIMESTAMP, 'PENDING', 1130, 'PENDING');

INSERT INTO orders (order_id, order_date, status, total_amount, payment_status)
VALUES (2, CURRENT_TIMESTAMP, 'COMPLETED', 1290, 'SUCCESS');

-- Insert Order Items for Order 1
INSERT INTO order_items (order_item_id, order_id, product_id, quantity, price)
VALUES (1, 1, 5, 2, 500); -- 2 Gulab Jamun Box (250 each)

INSERT INTO order_items (order_item_id, order_id, product_id, quantity, price)
VALUES (2, 1, 6, 3, 270); -- 3 Pepsi 1L (90 each)

INSERT INTO order_items (order_item_id, order_id, product_id, quantity, price)
VALUES (3, 1, 8, 3, 360); -- 3 Tic Tac Mint Pack (30 each)

-- Insert Order Items for Order 2
INSERT INTO order_items (order_item_id, order_id, product_id, quantity, price)
VALUES (4, 2, 4, 1, 880); -- 1 Rasmalai

INSERT INTO order_items (order_item_id, order_id, product_id, quantity, price)
VALUES (5, 2, 7, 1, 1200); -- 1 Whiskey Premium 750ml

