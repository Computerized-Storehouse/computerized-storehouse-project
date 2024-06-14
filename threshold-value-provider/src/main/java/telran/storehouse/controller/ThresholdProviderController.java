package telran.storehouse.controller;

import static telran.storehouse.messages.ValidationErrorMessages.MISSING_SENSOR_ID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.service.ThresholdProviderService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ThresholdProviderController {
	final ThresholdProviderService thresholdProvider;
	
	@GetMapping("${app.threshold.provider.urn}" + "{id}")
	double getThresholdValue(@PathVariable("id") @NotNull(message = MISSING_SENSOR_ID) long sensorUsedId) {
		log.debug("controller: get threshold value from container with sensor id {}", sensorUsedId);
		return thresholdProvider.getThresholdValue(sensorUsedId);
		
	}
	
	

}
