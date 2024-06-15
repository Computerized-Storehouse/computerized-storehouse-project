package telran.storehouse.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.dto.OrderDataDto;
import telran.storehouse.entity.Order;
import telran.storehouse.entity.Product;
import telran.storehouse.exceptions.IllegalOrderStateException;
import telran.storehouse.exceptions.IllegalProductStateException;
import telran.storehouse.repo.OrdersRepo;
import telran.storehouse.repo.ProductRepo;
@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersPopulatorServiceImpl implements OrdersPopulatorService {
	final OrdersRepo ordersRepo;
	final ProductRepo productRepo;
	@Override
	@Transactional
	public OrderDataDto addOrder(OrderDataDto orderData) {
			if(ordersRepo.existsById(orderData.orderId())) {
				throw new IllegalOrderStateException();
			}
			if(!productRepo.existsById(orderData.product().productName())) {
				throw new IllegalProductStateException();
			}
			Order order = Order.of(orderData);
			order.setProduct(Product.of(orderData.product()));
			ordersRepo.save(order);
			log.debug("Order {} has been saved", orderData);
		return orderData;
	}

}
