package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.GenericMessage;

import telran.storehouse.exceptions.IllegalOrderStateException;


@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class OrdersUpdaterConsumerTest {
    
    @Autowired
    InputDestination producer;
    private  String consumerBindingName = "ordersUpdaterConsumer-in-0";
    private long orderId = 100;
    @Test
	void loadApplicationContext() {
		assertNotNull(producer);
	}
    @Test
    void test_orderUpdaterConsumerTest() {
    	try {
    		producer.send(new GenericMessage<Long>(orderId), consumerBindingName);
    	}catch (IllegalOrderStateException e) {
    		assertEquals("Order already exist", e.getMessage());
    	}
    }
}