package telran.storehouse.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.dto.SensorDataDto;
import telran.storehouse.model.SensorData;
import telran.storehouse.repo.SensorDataRepo;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateDetectorServiceImpl implements UpdateDetectorService {
	final SensorDataRepo sensorDataRepo;

	@Override
	public Double getDifference(SensorDataDto sensorDataDto) {
		Long sensorId = sensorDataDto.id();
		SensorData sensorData = sensorDataRepo.findById(sensorId).orElse(null);
		Double fullnes = sensorDataDto.fullness();
		Double res = null;
		
		if (sensorData == null || !sensorData.getFullnes().equals(fullnes)) {
			if (sensorData == null) {
				log.debug("Data for sensor {} doesn't exist", sensorId);
				sensorData = new SensorData(sensorId, fullnes);
				res = 100 - fullnes;
			} else {
				res = sensorData.getFullnes() - fullnes;
				sensorData.setFullnes(fullnes);
				log.debug("Data for sensor {} has changed", sensorId);
			}
			sensorDataRepo.save(sensorData);	
		} else {
			log.debug("Data for sensor {} hasn't changed", sensorId);
		}
		return res;
	}

}
