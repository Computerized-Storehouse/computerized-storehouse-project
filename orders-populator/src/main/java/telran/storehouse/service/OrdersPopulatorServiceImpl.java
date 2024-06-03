package telran.storehouse.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.dto.OrderDataDto;
import telran.storehouse.repo.OrdersRepo;
import telran.storehouse.entity.Order;
import telran.storehouse.exceptions.IllegalOrderStateException;
@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersPopulatorServiceImpl implements OrdersPopulatorService {
	final OrdersRepo orderRepo ;
	
		@Override
		@Transactional
		public OrderDataDto addOrder(OrderDataDto orderData) {
			if(orderRepo.existsById(orderData.orderId())) {
				throw new IllegalOrderStateException();
			}
			
			Order order = Order.of(orderData);
			orderRepo.save(order);
			log.debug("Order {} has been saved", orderData);
			return orderData;
		}

}
