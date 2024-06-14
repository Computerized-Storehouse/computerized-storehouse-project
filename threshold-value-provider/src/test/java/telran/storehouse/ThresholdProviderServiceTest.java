package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import telran.storehouse.exceptions.SensorNotFoundException;
import telran.storehouse.repo.ContainerDataRepo;
import telran.storehouse.service.ThresholdProviderService;

@SpringBootTest
@Sql(scripts = { "classpath:test_data.sql" })
class ThresholdProviderServiceTest {
	private static final long SENSOR_ID = 123;
	private static final double VALUE = 50;
	private static final double WRONG_VALUE = 0;
	private static final long SENSOR_WRONG_ID = 170;
	@Autowired
	ThresholdProviderService thresholdProvider;
	@Autowired
	ContainerDataRepo containerRepo;

	
	@Test
	void getValue_validId_correct() {
		assertEquals(VALUE, thresholdProvider.getThresholdValue(SENSOR_ID));
		assertNotEquals(WRONG_VALUE, thresholdProvider.getThresholdValue(SENSOR_ID));
	}
	
	@Test
	void getValue_wrongId_exception() {
		assertThrowsExactly(SensorNotFoundException.class, () -> thresholdProvider.getThresholdValue(SENSOR_WRONG_ID));
	}

}
