package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.GenericMessage;
import telran.storehouse.dto.OrderDataDto;
import telran.storehouse.dto.ProductDto;
import telran.storehouse.exceptions.IllegalOrderStateException;


@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class OrderPopulatorConsumerTest {
    
    @Autowired
    InputDestination producer;

    private  long ORDER_ID = 1234L;
    private  String consumerBindingName = "ordersPopulatorConsumer-in-0";
    final ProductDto product = new ProductDto("Product", "Units");
    final OrderDataDto orderDto = new OrderDataDto(ORDER_ID, 4321L, "A123", product,
            System.currentTimeMillis(), System.currentTimeMillis(), 20L, "creator", "status");
    @Test
	void loadApplicationContext() {
		assertNotNull(producer);
	}
    @Test
    void test_orderPopulatorConsumerTest() {
    	try {
    		producer.send(new GenericMessage<>(orderDto), consumerBindingName);
    	}catch (IllegalOrderStateException e) {
    		assertEquals("Order already exist", e.getMessage());
    	}
    }
}