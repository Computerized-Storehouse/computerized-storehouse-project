package telran.storehouse.service;

import telran.storehouse.dto.OrderDataDto;

public interface OrdersUpdaterService {
	OrderDataDto updateOrder(OrderDataDto orderData);
}
