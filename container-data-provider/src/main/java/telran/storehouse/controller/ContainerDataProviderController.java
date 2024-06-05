package telran.storehouse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.dto.ContainerDataDto;
import telran.storehouse.service.ContainerDataProviderService;
import static telran.storehouse.messages.ValidationErrorMessages.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ContainerDataProviderController {
	final ContainerDataProviderService providerService;

	@GetMapping("${app.container.provider.urn}" + "{id}")
	ContainerDataDto getContainerData(@PathVariable("id") @NotNull(message = MISSING_SENSOR_ID) long sensorId) {
		log.debug("---> getContainerData called for sensor {}", sensorId);
		var res = providerService.getContainerData(sensorId);
		log.debug("---> getContainerData sends a response {}", res);
		return res;
	}
}
