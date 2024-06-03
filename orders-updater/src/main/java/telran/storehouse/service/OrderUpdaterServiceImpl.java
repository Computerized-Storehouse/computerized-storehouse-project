package telran.storehouse.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.dto.OrderDataDto;
import telran.storehouse.entity.*;
import telran.storehouse.exceptions.IllegalOrderStateException;
import telran.storehouse.exceptions.OrderNotFoundException;
import telran.storehouse.repo.CompletedOrdersRepo;
import telran.storehouse.repo.OrdersRepo;
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderUpdaterServiceImpl implements OrdersUpdaterService {
	final OrdersRepo orderRepo ;
	final CompletedOrdersRepo completedOrderRepo ;
	@Override
	@Transactional
	public OrderDataDto updateOrder(OrderDataDto orderData) {
		Order order = orderRepo.findById(orderData.orderId()).orElseThrow(() -> new OrderNotFoundException());
		CompletedOrder completedOrder = CompletedOrder.of(order);
		orderRepo.deleteById(orderData.orderId());
		log.debug("Order {} deleted ", orderData);
		if(completedOrderRepo.existsById(orderData.orderId())) {
			throw new IllegalOrderStateException();
		}
		completedOrderRepo.save(completedOrder);
		log.debug("Completed order {} has been saved", orderData);
		return orderData;
	}

}
