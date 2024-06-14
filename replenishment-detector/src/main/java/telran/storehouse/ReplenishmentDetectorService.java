package telran.storehouse;

import telran.storehouse.dto.OrderDataDto;

public interface ReplenishmentDetectorService {
	
	OrderDataDto getOrder(long sensorId);

}
