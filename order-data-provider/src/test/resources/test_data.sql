delete from order_table;
delete from product;

insert into product (product_name, product_unit) values
('product1', 'unit1'),
('product2', 'unit2'),
('product3', 'unit3');

insert into order_table (order_id, container_id, coordinates, product, required_quantity, opening_time, closing_time, creator, status) values
(100, 300, 'A1','product1', 30, 2023-06-06, 2023-07-07, 'creator1','status1'),
(101, 301, 'A2','product2', 40, 2023-07-07, 2023-08-08, 'creator2','status2'),
(102, 302, 'A3','product3', 50, 2023-08-08, 2023-09-09, 'creator3','status3');