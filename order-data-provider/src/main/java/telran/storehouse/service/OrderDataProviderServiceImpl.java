package telran.storehouse.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.dto.OrderDataDto;
import telran.storehouse.entity.Order;
import telran.storehouse.exceptions.OrderNotFoundException;
import telran.storehouse.repo.OrdersRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderDataProviderServiceImpl implements OrderDataProviderService {
final OrdersRepo orderRepo ;
	@Override
	@Transactional
	public OrderDataDto getOrderData(long orderId) {
		Order order = orderRepo.findById(orderId)
				.orElseThrow(()-> new OrderNotFoundException());
		log.debug("Order data:{} found in DB ", orderId);
		return order.build();
	}

}
