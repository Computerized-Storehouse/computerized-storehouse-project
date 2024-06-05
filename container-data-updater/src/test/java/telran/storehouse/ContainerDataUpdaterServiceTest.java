package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import telran.storehouse.dto.ContainerDataDto;
import telran.storehouse.dto.NewStateDto;
import telran.storehouse.dto.ProductDto;
import telran.storehouse.dto.SensorDataDto;
import telran.storehouse.exceptions.ContainerNotFoundException;
import telran.storehouse.repo.ContainerDataRepo;
import telran.storehouse.service.ContainerDataUpdaterService;

@SpringBootTest
@Sql(scripts = { "classpath:test_data.sql" })
class ContainerDataUpdaterServiceTest {

	private static final String NEW_STATUS = "not ok";
	private static final long SENSOR_ID_1 = 101l;
	private static final long SENSOR_ID_2 = 102l;
	private static final long SENSOR_ID_NOT_EXISTS = 0l;
	private static final Double NEW_FULLNESS = 25d;

	@Autowired
	ContainerDataUpdaterService updaterService;

	@Autowired
	ContainerDataRepo containerDataRepo;

	private SensorDataDto sensor101 = new SensorDataDto(SENSOR_ID_1, NEW_FULLNESS, System.currentTimeMillis());
	private SensorDataDto sensorNotExists = new SensorDataDto(SENSOR_ID_NOT_EXISTS, NEW_FULLNESS,
			System.currentTimeMillis());
	private ProductDto product = new ProductDto("pr1", "un1");

	@Test
	void updateContainerFullness_newValidState_success() {
		NewStateDto newState = new NewStateDto(sensor101, 10);
		assertEquals(newState, updaterService.updateContanerCurrentFullness(newState));
		assertEquals(NEW_FULLNESS, containerDataRepo.findById(1l).get().getContainerCurrentValue());
	}

	@Test
	void updateContainerFullness_wrongId_NotFoundException() {
		NewStateDto wrongState = new NewStateDto(sensorNotExists, 0);
		assertThrowsExactly(ContainerNotFoundException.class,
				() -> updaterService.updateContanerCurrentFullness(wrongState));
	}

	@Test
	void updateContainerStatus_newValidState_success() {
		ContainerDataDto containerData = new ContainerDataDto(0l, SENSOR_ID_2, "A5", 255d, 40d, NEW_STATUS, 60d,
				product);
		assertEquals(containerData, updaterService.updateContanerStatus(containerData));
		assertEquals(NEW_STATUS, containerDataRepo.findById(2l).get().getStatus());
	}

	@Test
	void updateContainerStatus_wrongId_NotFoundException() {
		ContainerDataDto wrongContainerData = new ContainerDataDto(0l, SENSOR_ID_NOT_EXISTS, "A5", 255d, 40d,
				NEW_STATUS, 60d, product);
		assertThrowsExactly(ContainerNotFoundException.class,
				() -> updaterService.updateContanerStatus(wrongContainerData));
	}
}
