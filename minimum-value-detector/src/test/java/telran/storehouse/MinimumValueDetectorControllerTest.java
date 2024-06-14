package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.*;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.storehouse.dto.NewStateDto;
import telran.storehouse.dto.SensorDataDto;
import telran.storehouse.service.MinimumValueProviderService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class MinimumValueDetectorControllerTest {
	private static final long SENSOR_ID_CORRECT = 123;
	private static final long SENSOR_ID_2 = 124;
	private static final double THRESHOLD_VALUE = 30;
	private static final double SENSOR_FULLNES_LESS = 20;
	private static final double SENSOR_FULLNES_GREATER = 50;
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
	@MockBean
	MinimumValueProviderService valueProviderService;
	
	@Test
	void loadApplicationContext() {
		assertNotNull(valueProviderService);
	}
	
	@Value("${app.minimum.value.detector.producer.binding.name}")
	private String producerBindingName;
	private String consumerBindingName = "valueDetectorConsumer-in-0";
	private SensorDataDto sensorDataWithLessValue = new SensorDataDto(SENSOR_ID_CORRECT, SENSOR_FULLNES_LESS, 0);
	private SensorDataDto sensorDataWithGreaterValue = new SensorDataDto(SENSOR_ID_2, SENSOR_FULLNES_GREATER, 0);
	private NewStateDto sensorDataGreaterThresholdValue = new NewStateDto(sensorDataWithGreaterValue, 50);
	private NewStateDto sensorDataLessThresholdValue = new NewStateDto(sensorDataWithLessValue, 30);
	private NewStateDto newStatewithDifferenceLessNull = new NewStateDto(sensorDataWithGreaterValue, -30);
	

	@BeforeEach
	void setUp() {
		when(valueProviderService.getValue(SENSOR_ID_CORRECT)).thenReturn(THRESHOLD_VALUE);
	}

	@Test
	void application_createRequest_FullnessLessThanThreshold_SendData_Test() throws Exception {
		producer.send(new GenericMessage<NewStateDto>(sensorDataLessThresholdValue), consumerBindingName);
		Message<byte[]> message = consumer.receive(10, producerBindingName);
		assertNotNull(message);
		ObjectMapper mapper = new ObjectMapper();
		SensorDataDto actual = mapper.readValue(message.getPayload(), SensorDataDto.class);
		SensorDataDto expected = sensorDataWithLessValue ;
		assertEquals(expected.id(), actual.id());
		assertEquals(expected.fullnes(), actual.fullnes());
		
	}
	
	@Test
	void application_createRequest_FullnessGreaterThanThreshold_NotSendData_Test() {
		producer.send(new GenericMessage<NewStateDto>(sensorDataGreaterThresholdValue), consumerBindingName);
		Message<byte[]> message = consumer.receive(10, producerBindingName);
		assertNull(message);
	}
	
	
	@Test
	void application_noCreateRequest_DifferenceLessNull_Test() {
		producer.send(new GenericMessage<NewStateDto>(newStatewithDifferenceLessNull), consumerBindingName);
		Message<byte[]> message = consumer.receive(10, producerBindingName);
		assertNull(message);
	}

}
