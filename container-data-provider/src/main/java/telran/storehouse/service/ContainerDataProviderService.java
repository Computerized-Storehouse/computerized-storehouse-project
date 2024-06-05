package telran.storehouse.service;

import telran.storehouse.dto.ContainerDataDto;

public interface ContainerDataProviderService {
	ContainerDataDto getContainerData(long sensorId);
}
