package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import telran.storehouse.dto.NewStateDto;
import telran.storehouse.dto.SensorDataDto;
import telran.storehouse.exceptions.ContainerNotFoundException;
import telran.storehouse.repo.ContainerDataRepo;
import telran.storehouse.repo.ProductRepo;
import telran.storehouse.service.ContainerDataUpdaterService;

@SpringBootTest
@Sql(scripts = { "classpath:test_data.sql" })
class ContainerDataUpdaterServiceTest {

	private static final long SENSOR_ID_1 = 101l;
	private static final long SENSOR_ID_NOT_EXISTS = 0l;
	private static final Double NEW_FULLNESS = 0d;

	@Autowired
	ContainerDataUpdaterService updaterService;

	@Autowired
	ContainerDataRepo containerDataRepo;
	@Autowired
	ProductRepo prodRepo;

	private SensorDataDto sensor101 = new SensorDataDto(SENSOR_ID_1, NEW_FULLNESS, System.currentTimeMillis());
	private SensorDataDto sensorNotExists = new SensorDataDto(SENSOR_ID_NOT_EXISTS, NEW_FULLNESS,
			System.currentTimeMillis());

	@Test
	void updateContainerFullness_newValidState_success() {
		NewStateDto newState = new NewStateDto(sensor101, 10);
		assertEquals(newState, updaterService.updateContanerCurrentFullness(newState));
		assertEquals(NEW_FULLNESS, containerDataRepo.findById(1l).get().getContainerCurrentValue());
	}

	@Test
	void updateContainerFullness_wrongState_NotFoundException() {
		NewStateDto wrongState = new NewStateDto(sensorNotExists, 0);
		assertThrowsExactly(ContainerNotFoundException.class,
				() -> updaterService.updateContanerCurrentFullness(wrongState));
	}

	@Test
	void testUpdateStatus() {

	}
}
