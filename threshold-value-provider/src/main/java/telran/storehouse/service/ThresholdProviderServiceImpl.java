package telran.storehouse.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.dto.ContainerStatus;
import telran.storehouse.exceptions.SensorNotFoundException;
import telran.storehouse.repo.ContainerDataRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThresholdProviderServiceImpl implements ThresholdProviderService {
	final ContainerDataRepo containerRepo;

	@Override
	public double getThresholdValue(long sensorUsedId) {
		Optional<Double> value = containerRepo.findThresholdValueBySensorUsedIdAndStatus(sensorUsedId, ContainerStatus.OK);
		if(value.isEmpty()) {
			throw new SensorNotFoundException(Long.toString(sensorUsedId));
		}
		log.debug("Threshold value is {} in container with sensorUsedId {}", value, sensorUsedId);
		return value.get();
	}

}
