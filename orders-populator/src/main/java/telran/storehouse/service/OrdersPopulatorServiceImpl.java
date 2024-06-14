package telran.storehouse.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.dto.OrderDataDto;
import telran.storehouse.entity.Order;
import telran.storehouse.exceptions.IllegalOrderStateException;
import telran.storehouse.repo.OrdersRepo;
@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersPopulatorServiceImpl implements OrdersPopulatorService {
	final OrdersRepo ordersRepo;
	@Override
	@Transactional
	public OrderDataDto addOrder(OrderDataDto orderData) {
			if(ordersRepo.existsById(orderData.orderId())) {
				throw new IllegalOrderStateException();
			}
			Order order = Order.of(orderData);
			if (order.getProduct() == null) {
	            throw new IllegalArgumentException("Product cannot be null");
	        }
			ordersRepo.save(order);
			log.debug("Order {} has been saved", orderData);
			
		
		return orderData;
	}

}
