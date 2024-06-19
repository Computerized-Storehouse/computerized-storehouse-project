package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import jakarta.validation.constraints.NotNull;
import telran.storehouse.dto.OrderDataDto;
import telran.storehouse.dto.OrderStatus;
import telran.storehouse.dto.ProductDto;
import telran.storehouse.exceptions.OrderNotFoundException;
import telran.storehouse.repo.OrdersRepo;
import telran.storehouse.repo.ProductRepo;
import telran.storehouse.service.OrderDataProviderService;
@SpringBootTest
@Sql(scripts = { "classpath:test_data.sql" })
class OrderProviderServiceTest {

	private static final @NotNull(message = "missing order id value") long ORDER_ID = 100;
	private static final long ORDER_ID_NOT_EXIST = 200;
	@Autowired
	OrderDataProviderService service;
	@Autowired
	OrdersRepo ordersRepo;
	@Autowired
	ProductRepo productRepo;
	
	final ProductDto product1 = new ProductDto("product1", "unit1");
    final OrderDataDto orderDto = new OrderDataDto(ORDER_ID, 300, "A1", product1, 30, 2023-06-06, 2023-07-07, "creator1",OrderStatus.OPEN);
	
	@Test
	 void OrderProvider_getOrder () {
		assertEquals(orderDto, service.getOrderData(ORDER_ID));
	}
	@Test
	 void OrderProvider_order_not_found() {
		assertThrowsExactly(OrderNotFoundException.class,()-> service.getOrderData(ORDER_ID_NOT_EXIST));
	}
}
