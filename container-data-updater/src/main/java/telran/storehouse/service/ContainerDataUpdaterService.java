package telran.storehouse.service;

import telran.storehouse.dto.ContainerDataDto;
import telran.storehouse.dto.NewStateDto;

public interface ContainerDataUpdaterService {
	NewStateDto updateContanerCurrentFullness(NewStateDto newStateDto);

	ContainerDataDto updateContanerStatus(ContainerDataDto containerDataDto);
}
