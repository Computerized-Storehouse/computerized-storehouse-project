package telran.storehouse;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.dto.OrderDataDto;
import telran.storehouse.service.OrdersPopulatorService;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class OrdersPopulatorAppl {
final OrdersPopulatorService service;
final StreamBridge streamBridge;
@Value("${app.orders.populator.producer.info.binding.name}")
private String producerBindingName;
	public static void main(String[] args) {
		SpringApplication.run(OrdersPopulatorAppl.class, args);
		
	}
	@Bean
	Consumer<OrderDataDto> ordersPopulatorConsumer() {
		return orderData -> ordersPopulatorProcessing(orderData);
	}
	private void ordersPopulatorProcessing(OrderDataDto orderData) {
		service.addOrder(orderData);
		streamBridge.send(producerBindingName, orderData);
		log.debug("Order {} added", orderData);
	}
	
}