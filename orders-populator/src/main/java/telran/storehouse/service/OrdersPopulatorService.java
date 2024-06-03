package telran.storehouse.service;

import telran.storehouse.dto.OrderDataDto;

public interface OrdersPopulatorService {
	OrderDataDto addOrder(OrderDataDto orderData);
}
