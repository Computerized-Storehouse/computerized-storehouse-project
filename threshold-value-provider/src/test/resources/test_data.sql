delete from container_data;
delete from product;
insert into product (product_name, product_unit) values 
('product1', 'box'),
('product2', 'box'),
('product3', 'bottle'),
('product4', 'bottle');

insert into container_data (container_id, sensor_id, coordinates, container_max_value, container_current_value, status, threshold_value, product) values 
(10, 123,  'A1', 200, 0, 'OK', 50, 'product1'), 
(12, 124,  'B2', 300, 50, 'SENSOR_ERROR', 30,'product2'), 
(13, 125,  'C3', 500, 80, 'OK', 40, 'product3');