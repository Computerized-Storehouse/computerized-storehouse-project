package telran.storehouse;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.storehouse.repo.*;
import telran.storehouse.dto.*;
import telran.storehouse.model.*;
import telran.storehouse.service.ValuesAnalyzerService;

@SpringBootTest

class ValuesAnalyzerTest {

	@Autowired
	ValuesAnalyzerService service;
	@MockBean
	ErrorsCountRepo countRepo;
	@MockBean
	SensorTimeoutRepo timeoutRepo;
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
	private String consumerBindingName = "valuesAnalyzerConsumer-in-0";
	@Value("${app.values.analyzer.producer.binding.name}")
	private String producerBindingName;
	@Value("${app.values.analyzer.producer.error.count.binding.name}")
	private String producerErrorCountBindingName;
	@Value("${app.values.analyzer.producer.error.received.binding.name}")
	private String producerErrorReceivedBindingName;

	HashMap<Long, ErrorCount> countMap = new HashMap<>();
	HashMap<Long, SensorTimeout> timeoutMap = new HashMap<>();

	SensorDataDto sensorDataNormal = new SensorDataDto(100, 50L, System.currentTimeMillis());
	SensorDataDto sensorDataError = new SensorDataDto(100, 150, System.currentTimeMillis());

	@BeforeEach
	void setUp() throws Exception {
		mockSetUp(countRepo, countMap);
		mockSetUp(timeoutRepo, timeoutMap);

	}

	private <E> void mockSetUp(CrudRepository<E, Long> repo, HashMap<Long, E> map) {

		when(repo.findById(any(Long.class))).thenAnswer(new Answer<Optional<E>>() {
			@Override
			public Optional<E> answer(InvocationOnMock invocation) throws Throwable {
				Long sensorId = invocation.getArgument(0);
				E entity = map.get(sensorId);
				return entity == null ? Optional.ofNullable(null) : Optional.of(entity);
			}
		});

		when(repo.save(any())).then(new Answer<E>() {
			@Override
			public E answer(InvocationOnMock invocation) throws Throwable {
				E entity = invocation.getArgument(0);
				Long id = null;
				if (entity instanceof ErrorCount) {
					id = ((ErrorCount) entity).getSensorId();
				} else if (entity instanceof SensorTimeout) {
					id = ((SensorTimeout) entity).getSensorId();
				}
				map.put(id, entity);
				return entity;
			}
		});
	}

	@Test
	void valuesAnalyzer_serviceTest_normalFlow() {
		service.sensorDataAnalyzing(sensorDataNormal);
		assertFalse(countRepo.existsById(sensorDataNormal.id()));
		assertFalse(timeoutRepo.existsById(sensorDataNormal.id()));
	}

	@Test
	void valuesAnalyzer_serviceTest_errorData() {
		service.sensorDataAnalyzing(sensorDataError);
		List<Double> list = countRepo.findById(sensorDataError.id()).get().getErrorsCounter();
		assertEquals(1, list.size());
		service.sensorDataAnalyzing(sensorDataError);
		list = countRepo.findById(sensorDataError.id()).get().getErrorsCounter();
		assertEquals(0, list.size());

	}

	@Test
	void valuesAnalyzer_serviceTest_sensor_noData() throws InterruptedException {
		service.sensorDataAnalyzing(sensorDataNormal);
		service.startCheckMissedDataScheduler();
		TimeUnit.SECONDS.sleep(2);
		Optional<SensorTimeout> optionalSensorTimeout = timeoutRepo.findById(sensorDataNormal.id());
		assertTrue(optionalSensorTimeout.isPresent());
		TimeUnit.SECONDS.sleep(2);
		optionalSensorTimeout = timeoutRepo.findById(sensorDataNormal.id());
		assertTrue(optionalSensorTimeout.isPresent());

	}

	@Test
	void valuesAnalyzer_send_normalSensorData() {
		service.sensorDataAnalyzing(sensorDataNormal);
		producer.send(new GenericMessage<>(sensorDataNormal), consumerBindingName);
		Message<byte[]> message = consumer.receive(10, producerBindingName);
		assertNotNull(message);
	}

	@Test
	void valuesAnalyzer_send_ErrorData() throws Exception {
		producer.send(new GenericMessage<>(sensorDataError), consumerBindingName);
		Message<byte[]> message = consumer.receive(10, producerErrorCountBindingName);
		assertNull(message);
		producer.send(new GenericMessage<>(sensorDataError), consumerBindingName);
		message = consumer.receive(10, producerErrorCountBindingName);
		assertNotNull(message);
		ObjectMapper mapper = new ObjectMapper();
		ErrorDataDto actual = mapper.readValue(message.getPayload(), ErrorDataDto.class);
		List<Double> errorsCounter = actual.errorsCounter();
		ErrorDataDto expected = new ErrorDataDto(100, errorsCounter);
		assertEquals(expected.sensorId(), actual.sensorId());

	}

	@Test
	void valuesAnalizer_send_SensorTimeoutData() throws InterruptedException, Throwable {
		producer.send(new GenericMessage<>(sensorDataNormal), consumerBindingName);
		service.startCheckMissedDataScheduler();
		TimeUnit.SECONDS.sleep(4);
		Message<byte[]> message = consumer.receive(10, producerBindingName);
		assertNotNull(message);

	}

}
