package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import telran.storehouse.repo.SensorDataRepo;
import telran.storehouse.dto.SensorDataDto;
import telran.storehouse.model.*;
import telran.storehouse.service.UpdateDetectorService;

@SpringBootTest
class UpdateDetectorServiceTest {
	private static final double FULLNES = 40;
	private static final double FULLNES1 = 60;
	private static final Double VALUE = 100 - FULLNES;
	private static final Double VALUE2 = FULLNES - FULLNES1;	
	@Autowired
	UpdateDetectorService detectorService;
	@MockBean
	SensorDataRepo sensorDataRepo;
	HashMap<Long, SensorData> redisMockMap = new HashMap<>();
	private SensorDataDto sensorData = new SensorDataDto(123, FULLNES, 0);
	private SensorDataDto sensorDataAnotherFullnes = new SensorDataDto(123, FULLNES1, 0);
	@BeforeEach
	void mockSetUp() {
		when(sensorDataRepo.findById(any(Long.class))).then(new Answer<Optional<SensorData>>() {

			@Override
			public Optional<SensorData> answer(InvocationOnMock invocation) throws Throwable {
				Long sensorId = invocation.getArgument(0);
				SensorData sensorData = redisMockMap.get(sensorId);
				return sensorData == null ? Optional.ofNullable(null) : Optional.of(sensorData);
			}
		});
		when(sensorDataRepo.save(any(SensorData.class))).then(new Answer<SensorData>() {

			@Override
			public SensorData answer(InvocationOnMock invocation) throws Throwable {
				SensorData sensorData = invocation.getArgument(0);
				redisMockMap.put(sensorData.getSensorId(), sensorData);
				return sensorData;
			}
			
		});
	}

	@Test
	void test() {
		Double res = detectorService.getDifference(sensorData);
		assertNotNull(res);
		assertEquals(VALUE, res);
		res = detectorService.getDifference(sensorData);
		assertNull(res);
		res = detectorService.getDifference(sensorDataAnotherFullnes);
		assertNotNull(res);
		assertEquals(VALUE2, res);
		res = detectorService.getDifference(sensorDataAnotherFullnes);
		assertNull(res);
	}

}
