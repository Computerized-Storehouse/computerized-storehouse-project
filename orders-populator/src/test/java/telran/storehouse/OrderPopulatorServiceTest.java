package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import telran.storehouse.dto.OrderDataDto;
import telran.storehouse.dto.ProductDto;
import telran.storehouse.entity.Order;
import telran.storehouse.entity.Product;
import telran.storehouse.exceptions.IllegalOrderStateException;
import telran.storehouse.repo.OrdersRepo;
import telran.storehouse.repo.ProductRepo;
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
	@Autowired
	ProductRepo productRepo;
	
	final ProductDto product = new ProductDto("Product", "Units");
	final OrderDataDto orderDto = new OrderDataDto(ORDER_ID, 4321L, "A123", product,
            System.currentTimeMillis(), System.currentTimeMillis(), 20L, "creator", "status");
	@Test
	void ordersPopulator_addOrder() {
		Product product1 = Product.of(product);
		productRepo.save(product1);
		Order order = Order.of(orderDto);
		order.setProduct(product1);
		List<Order> list = ordersRepo.findAll();
		assertEquals(3, list.size());
		assertEquals(orderDto,ordersService.addOrder(orderDto));
		assertEquals(orderDto,order.build());
		
	}
	@Test
	void ordersPopulator_add_existing_id() {
		OrderDataDto orderDtoExist = new OrderDataDto(ORDER_ID_EXIST, 321, "A1", product,
	            System.currentTimeMillis(), System.currentTimeMillis(), 20L, "creator", "status");
		assertThrowsExactly(IllegalOrderStateException.class,()-> ordersService.addOrder(orderDtoExist));
	}
		
}
