package telran.storehouse.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.dto.OrderDataDto;
import telran.storehouse.entity.OrderData;
import telran.storehouse.exceptions.OrderNotFoundException;
import telran.storehouse.repo.OrderDataRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderDataProviderServiceImpl implements OrderDataProviderService {
final OrderDataRepo orderDataRepo ;
	@Override
	@Transactional
	public OrderDataDto getOrderData(long orderId) {
		OrderData orderData = orderDataRepo.findById(orderId)
				.orElseThrow(()-> new OrderNotFoundException());
		log.debug("Order data:{} found in DB ", orderId);
		return orderData.build();
	}

}
