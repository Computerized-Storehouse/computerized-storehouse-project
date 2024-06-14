package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.*;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.storehouse.dto.*;
import telran.storehouse.service.UpdateDetectorService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class UpdateDetectorControllerTest {
	private static final long SENSOR_ID = 123;
	private static final double SENSOR_FULLNES = 40;
	private static final double DIFFERENCE = 60;
	private static final long SENSOR_ID_NO_DIFFERENCE = 124;
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
	@MockBean
	UpdateDetectorService updateDetectorService;
	
	@Test
	void loadApplicationContext() {
		assertNotNull(updateDetectorService);
	}
	
	
	@Value("${app.update.detector.producer.binding.name}")
	private String producerBindingName;
	private String consumerBindingName = "updateDetectorConsumer-in-0";
	private SensorDataDto sensorDataNoDifference = new SensorDataDto(SENSOR_ID, SENSOR_FULLNES, 0);
	private SensorDataDto sensorDataDifference = new SensorDataDto(SENSOR_ID_NO_DIFFERENCE, SENSOR_FULLNES, 0);

	@BeforeEach
	void setServiceMock() {
		when(updateDetectorService.getDifference(sensorDataDifference)).thenReturn(DIFFERENCE);
		when(updateDetectorService.getDifference(sensorDataNoDifference)).thenReturn(null);
	}
	
	@Test
	void sensorDataDifferenceTest() throws Exception {
		producer.send(new GenericMessage<SensorDataDto>(sensorDataDifference), consumerBindingName);
		Message<byte[]> message = consumer.receive(10, producerBindingName);
		assertNotNull(message);
		ObjectMapper mapper = new ObjectMapper();
		NewStateDto actual = mapper.readValue(message.getPayload(), NewStateDto.class);
		NewStateDto expected = new NewStateDto(sensorDataDifference, DIFFERENCE);
		assertEquals(expected.sensorData().id(), actual.sensorData().id());
		assertEquals(expected.difference(), actual.difference());
	}
	
	@Test
	void sensorDataNoDifferenceTest() {
		producer.send(new GenericMessage<SensorDataDto>(sensorDataNoDifference), consumerBindingName);
		Message<byte[]> message = consumer.receive(10, producerBindingName);
		assertNull(message);
	}

}
