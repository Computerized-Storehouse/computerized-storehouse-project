package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.storehouse.dto.*;
import telran.storehouse.entity.Order;
import telran.storehouse.exceptions.IllegalOrderStateException;
import telran.storehouse.repo.OrdersRepo;
import telran.storehouse.service.OrdersPopulatorService;
@SpringBootTest
class OrderPopulatorServiceTest {
	@Autowired
	OrdersPopulatorService ordersPopulatorService;
	@Mock
    private OrdersRepo orderRepo;
	 @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }
	ProductDto product = new ProductDto("Product", "Units");
	OrderDataDto orderDto = new OrderDataDto(1234L, 4321L, "A123", product, 60L, 10L, 20L, "creator", "status");
	Order order = Order.of(orderDto);
	@Test
	void testAddOrder() {
		assertEquals(orderDto, ordersPopulatorService.addOrder(orderDto));
		assertThrowsExactly(IllegalOrderStateException.class,
				()->ordersPopulatorService.addOrder(orderDto));
		order = orderRepo.findById(orderDto.orderId()).orElse(null);
		assertEquals(orderDto, order.build());
		
		}
	}


