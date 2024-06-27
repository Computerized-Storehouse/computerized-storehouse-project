package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import telran.storehouse.exceptions.IllegalOrderStateException;
import telran.storehouse.service.OrdersUpdaterService;

@Slf4j
@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class OrdersUpdaterControllerTest {
	
	@MockBean
	OrdersUpdaterService service;
	@Autowired
	OutputDestination consumer;
    @Autowired
    InputDestination producer;
    
    @Value("${app.orders.updater.producer.info.binding.name}")
	private String producerBindingName;
    
    private String consumerBindingName = "ordersUpdaterConsumer-in-0";
    private long orderId = 100;
    @Test
    void loadContext() {
    	assertNotNull(service);
    }
    @Test
    void orderUpdaterControllerTest() throws StreamReadException, DatabindException, IOException {
    	try {
    		producer.send(new GenericMessage<Long>(orderId), consumerBindingName);
    	}catch (IllegalOrderStateException e) {
    		assertEquals("Order already exist", e.getMessage());
    	}
    	Message<byte[]> message = consumer.receive(10, producerBindingName);
		log.debug("${}", message);
		assertNotNull(message, "Expected message to be non-null");
		ObjectMapper mapper = new ObjectMapper();
		long actual = mapper.readValue(message.getPayload(), Long.class);
		log.debug("actual ${}", actual);
		assertEquals(orderId, actual);
    }
}