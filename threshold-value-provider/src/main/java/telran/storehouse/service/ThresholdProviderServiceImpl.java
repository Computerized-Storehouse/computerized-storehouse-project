package telran.storehouse.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.exceptions.SensorNotFoundException;
import telran.storehouse.repo.ContainerDataRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThresholdProviderServiceImpl implements ThresholdProviderService {
	final ContainerDataRepo containerRepo;

	@Override
	public double getThresholdValue(long sensorUsedId) {
		Double value = containerRepo.findThresholdValueBySensorUsedIdAndStatus(sensorUsedId, "OK");
		if(value == null) {
			throw new SensorNotFoundException(Long.toString(sensorUsedId));
		}
		log.debug("Threshol value is {} in container with sensorUsedId {}", value, sensorUsedId);
		return value;
	}

}
