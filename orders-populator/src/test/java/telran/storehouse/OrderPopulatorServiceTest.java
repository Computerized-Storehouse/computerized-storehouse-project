package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import telran.storehouse.dto.OrderDataDto;
import telran.storehouse.dto.ProductDto;
import telran.storehouse.entity.Order;
import telran.storehouse.exceptions.IllegalOrderStateException;
import telran.storehouse.repo.OrdersRepo;
import telran.storehouse.service.OrdersPopulatorService;
@SpringBootTest
@Sql(scripts = { "classpath:test_data.sql" })
class OrderPopulatorServiceTest {
	
	private static final long ORDER_ID_EXIST = 100;
	private static final long ORDER_ID = 333;
	
	@Autowired
	OrdersPopulatorService ordersService;
	@Autowired
	OrdersRepo ordersRepo;
	
	final ProductDto product = new ProductDto("Product", "Units");
	final OrderDataDto orderDto = new OrderDataDto(ORDER_ID, 4321L, "A123", product,
            System.currentTimeMillis(), System.currentTimeMillis(), 20L, "creator", "status");
	@Test
	void ordersPopulator_addOrder() {
		Order order = Order.of(orderDto);
		List<Order> list = ordersRepo.findAll();
		assertEquals(3, list.size());
		assertEquals(orderDto,ordersService.addOrder(orderDto));
		assertEquals(order,ordersRepo.findById(orderDto.orderId()));
		assertEquals(4, list.size());
	}
	@Test
	void ordersPopulator_add_existing_id() {
		OrderDataDto orderDtoExist = new OrderDataDto(ORDER_ID_EXIST, 321, "A1", product,
	            System.currentTimeMillis(), System.currentTimeMillis(), 20L, "creator", "status");
		assertThrowsExactly(IllegalOrderStateException.class,()-> ordersService.addOrder(orderDtoExist));
	}
		
}
