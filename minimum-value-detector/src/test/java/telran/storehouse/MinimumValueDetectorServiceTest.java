package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.*;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import telran.storehouse.service.MinimumValueProviderService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MinimumValueDetectorServiceTest {
	private static final long SENSOR_ID = 123;
	private static final double VALUE = 40;
	private static final String URL = "http://localhost:8282/threshold/";
	private static final String SENOSOR_NOT_FOUND_MESSAGE = "sensor not found";
	private static final long SENSOR_ID_NOT_FOUND = 124;
	private static final double VALUE_DEFAULT = MinimumValueProviderService.DEFAULT_TRESHOLD_VALUE;
	private static final long SENSOR_ID_INVAILABLE = 125;
	@Autowired
	InputDestination producer;
	@Autowired
	MinimumValueProviderService valueProviderService;
	@MockBean
	RestTemplate restTemplate;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	@Order(1)
	void normalFlowNoCache() {
		ResponseEntity<Double> responseEntity = new ResponseEntity<>(VALUE, HttpStatus.OK);
		when(restTemplate.exchange(getUrl(SENSOR_ID), HttpMethod.GET, null, Double.class))
		.thenReturn(responseEntity);
		assertEquals(VALUE, valueProviderService.getValue(SENSOR_ID));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(2)
	void normalFlowWithCache() {
		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
		.thenAnswer(new Answer<ResponseEntity<?>>() {
			@Override
			public ResponseEntity<?> answer(InvocationOnMock invocation) throws Throwable {
				fail("method exchange should not be called");
				return null;
			}
		});
		assertEquals(VALUE, valueProviderService.getValue(SENSOR_ID));
	}
	@Test
	@Order(3)
	void senosrNotFoundTest() {
		ResponseEntity<String> responseEntity = new ResponseEntity<>(SENOSOR_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
		when(restTemplate.exchange(getUrl(SENSOR_ID_NOT_FOUND), HttpMethod.GET, null, String.class))
		.thenReturn(responseEntity);
		assertEquals(VALUE_DEFAULT, valueProviderService.getValue(SENSOR_ID_NOT_FOUND));
	}
	
	@Test
	@Order(4)
	void defaultValueNotInCache() {
		ResponseEntity<Double> responseEntity = new ResponseEntity<>(VALUE, HttpStatus.OK);
		when(restTemplate.exchange(getUrl(SENSOR_ID_NOT_FOUND), HttpMethod.GET, null, Double.class))
		.thenReturn(responseEntity);
		assertEquals(VALUE, valueProviderService.getValue(SENSOR_ID_NOT_FOUND));
	}
	@SuppressWarnings("unchecked")
	@Test
	void remoteWebServiceAnavailable() {
		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
		.thenThrow(new Exception("Service is unavailable"));
		assertEquals(VALUE_DEFAULT, valueProviderService.getValue(SENSOR_ID_INVAILABLE));
	}

	private String getUrl(long sensorId) {
		
		return URL + sensorId;
	}

}
