SELECT p.id, p.category, c.product_price, p.source_user, p.date_added;
FROM product p JOIN product_price c ON p.id = c.product_id;