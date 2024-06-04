package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.cloud.stream.binder.test.*;
import org.springframework.context.annotation.Import;

import org.springframework.messaging.support.GenericMessage;

import telran.storehouse.dto.ContainerDataDto;
import telran.storehouse.dto.*;

import telran.storehouse.exceptions.ContainerNotFoundException;
import static telran.storehouse.exceptionMessages.ExceptionMessages.*;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class ContainerDataUpdaterConsumerTest {
	@Autowired
	InputDestination producer;

	private String consumerFullnessBindingName = "containerFullnessUpdateConsumer-in-0";
	private String consumerStatusBindingName = "containerStatusUpdateConsumer-in-0";

	private SensorDataDto sensorWrongId = new SensorDataDto(0, 0, 0);
	private NewStateDto newStateWrongId = new NewStateDto(sensorWrongId, 0);
	private ContainerDataDto containerDataWrongId = new ContainerDataDto(0, 0, consumerFullnessBindingName, 0, 0,
			consumerStatusBindingName, 0, null);

	@Test
	void loadApplicationContext() {
		assertNotNull(producer);
	}

	@Test
	void containerFullnessUpdateConsumerTest() {
		try {
			producer.send(new GenericMessage<NewStateDto>(newStateWrongId), consumerFullnessBindingName);
		} catch (ContainerNotFoundException e) {
			assertEquals(CONTAINER_NOT_FOUND, e.getMessage());
		}
	}

	@Test
	void containerStatusUpdateConsumerTest() {
		try {
			producer.send(new GenericMessage<ContainerDataDto>(containerDataWrongId), consumerStatusBindingName);
		} catch (ContainerNotFoundException e) {
			assertEquals(CONTAINER_NOT_FOUND, e.getMessage());
		}
	}

}
