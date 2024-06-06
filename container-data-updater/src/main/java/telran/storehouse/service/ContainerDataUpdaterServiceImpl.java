package telran.storehouse.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.dto.ContainerDataDto;
import telran.storehouse.dto.NewStateDto;
import telran.storehouse.entity.ContainerData;
import telran.storehouse.exceptions.ContainerNotFoundException;
import telran.storehouse.repo.ContainerDataRepo;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContainerDataUpdaterServiceImpl implements ContainerDataUpdaterService {
	final ContainerDataRepo containerRepo;

	@Override
	@Transactional
	public NewStateDto updateContanerCurrentFullness(NewStateDto newStateDto) {
		ContainerData containerData = containerRepo.findBySensorUsedId(newStateDto.sensorData().id())
				.orElseThrow(() -> new ContainerNotFoundException());
		log.debug("---> ContainerData {} found in DB", containerData);
		containerData.setContainerCurrentValue(newStateDto.sensorData().fullness());
		log.debug("---> ContainerData {} updated, new ContanerCurrentFullness is {}", containerData,
				newStateDto.sensorData().fullness());
		return newStateDto;
	}

	@Override
	@Transactional
	public ContainerDataDto updateContanerStatus(ContainerDataDto containerDataDto) {
		ContainerData containerData = containerRepo.findBySensorUsedId(containerDataDto.sensorUsedId())
				.orElseThrow(() -> new ContainerNotFoundException());
		log.debug("---> ContainerData {} found in DB", containerData);
		containerData.setStatus(containerDataDto.status());
		log.debug("---> ContainerData {} updated, new Status is {}", containerData, containerDataDto.status());
		return containerDataDto;
	}

}
