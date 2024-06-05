package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import telran.storehouse.dto.ContainerDataDto;
import telran.storehouse.dto.ProductDto;
import telran.storehouse.exceptions.ContainerNotFoundException;
import telran.storehouse.repo.ContainerDataRepo;
import telran.storehouse.service.ContainerDataProviderService;

@SpringBootTest
@Sql(scripts = { "classpath:test_data.sql" })
class ContainerDataProviderServiceTest {
	private static final long SENSOR_ID_1 = 101l;
	private static final long SENSOR_ID_NOT_EXISTS = 0l;

	@Autowired
	ContainerDataProviderService updaterService;

	@Autowired
	ContainerDataRepo containerDataRepo;

	private ProductDto product = new ProductDto("product1", "box1");

	@Test
	void getContainerData_validId_success() {
		ContainerDataDto containerData = new ContainerDataDto(1, SENSOR_ID_1, "A1", 200, 0, "ok", 50, product);
		assertEquals(containerData, updaterService.getContainerData(SENSOR_ID_1));
	}

	@Test
	void getContainerData_wrongId_NotFoundException() {
		assertThrowsExactly(ContainerNotFoundException.class,
				() -> updaterService.getContainerData(SENSOR_ID_NOT_EXISTS));
	}

}
