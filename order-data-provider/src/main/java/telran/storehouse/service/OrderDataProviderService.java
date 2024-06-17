package telran.storehouse.service;

import telran.storehouse.dto.OrderDataDto;

public interface OrderDataProviderService {
	OrderDataDto getOrderData(long orderId);
}
