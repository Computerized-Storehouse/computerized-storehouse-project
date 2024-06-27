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
import telran.storehouse.dto.OrderDataDto;
import telran.storehouse.dto.OrderStatus;
import telran.storehouse.dto.ProductDto;
import telran.storehouse.exceptions.IllegalOrderStateException;
import telran.storehouse.service.OrdersPopulatorService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
@Slf4j
class OrdersPopulatorControllerTest {
	@Autowired
	OutputDestination consumer;
	@Autowired
	InputDestination producer;
	@MockBean
	OrdersPopulatorService service;
	@Value("${app.orders.populator.producer.info.binding.name}")
	private String producerBindingName;
	private String consumerBindingName = "ordersPopulatorConsumer-in-0";
	final ProductDto product = new ProductDto("Product", "Units");
	final OrderDataDto orderDto = new OrderDataDto(100, 4321L, "A123", product, System.currentTimeMillis(),
			System.currentTimeMillis(), 20L, "creator", OrderStatus.OPEN);

	@Test
	void orderPopulatorControllerTest() throws StreamReadException, DatabindException, IOException {

		try {
			producer.send(new GenericMessage<>(orderDto), consumerBindingName);
		} catch (IllegalOrderStateException e) {
			assertEquals("Order already exist", e.getMessage());
		}
		Message<byte[]> message = consumer.receive(10, producerBindingName);
		log.debug("${}", message);
		assertNotNull(message, "Expected message to be non-null");
		ObjectMapper mapper = new ObjectMapper();
		OrderDataDto actual = mapper.readValue(message.getPayload(), OrderDataDto.class);
		log.debug("actual ${}", actual);
		assertEquals(orderDto.orderId(), actual.orderId());
	
	}

}