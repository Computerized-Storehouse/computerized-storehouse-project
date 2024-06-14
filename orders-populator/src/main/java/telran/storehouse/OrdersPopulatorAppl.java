package telran.storehouse;

import java.util.function.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import lombok.RequiredArgsConstructor;
import telran.storehouse.dto.OrderDataDto;
import telran.storehouse.service.OrdersPopulatorService;


@SpringBootApplication
@RequiredArgsConstructor
public class OrdersPopulatorAppl {
final OrdersPopulatorService service;
	public static void main(String[] args) {
		SpringApplication.run(OrdersPopulatorAppl.class, args);
		
	}
	@Bean
	Consumer<OrderDataDto> ordersPopulatorConsumer() {
		return service::addOrder;
	}
	
}