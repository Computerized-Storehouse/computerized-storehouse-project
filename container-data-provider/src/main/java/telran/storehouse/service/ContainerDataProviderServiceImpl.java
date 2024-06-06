package telran.storehouse.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.dto.ContainerDataDto;
import telran.storehouse.entity.ContainerData;
import telran.storehouse.exceptions.ContainerNotFoundException;
import telran.storehouse.repo.ContainerDataRepo;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContainerDataProviderServiceImpl implements ContainerDataProviderService {
	final ContainerDataRepo containerRepo;

	@Override
	@Transactional(readOnly = true)
	public ContainerDataDto getContainerData(long sensorId) {
		ContainerData containerData = containerRepo.findBySensorUsedId(sensorId)
				.orElseThrow(() -> new ContainerNotFoundException());
		log.debug("---> ContainerData: {} has been found", containerData);
		return containerData.build();
	}

}
