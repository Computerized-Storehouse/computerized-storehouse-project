package telran.storehouse.service;


import telran.storehouse.entity.CompletedOrder;

public interface OrdersUpdaterService {
	CompletedOrder updateOrder(long orderId);
}
