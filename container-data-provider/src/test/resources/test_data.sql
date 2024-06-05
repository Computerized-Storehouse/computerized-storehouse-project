delete from container_data;
delete from product;
insert into product (product_name, product_unit) values 
('product1', 'box1'),
('product2', 'box2'),
('product3', 'canister1'),
('product4', 'canister2');

insert into container_data (container_id, sensor_id, coordinates, container_max_value, container_current_value, status, threshold_value, product) values 
(1, 101,  'A1', 200, 0, 'ok', 50, 'product1'), 
(2, 102,  'B2', 300, 50, 'sensor_error', 30,'product2'), 
(3, 103,  'C3', 500, 80, 'ok', 40, 'product3');