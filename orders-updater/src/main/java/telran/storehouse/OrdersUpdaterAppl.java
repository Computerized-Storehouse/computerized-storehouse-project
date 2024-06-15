package telran.storehouse;


import java.util.function.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import lombok.RequiredArgsConstructor;
import telran.storehouse.service.OrdersUpdaterService;

@RequiredArgsConstructor
@SpringBootApplication
public class OrdersUpdaterAppl {
	final OrdersUpdaterService service;
	public static void main(String[] args) {
		SpringApplication.run(OrdersUpdaterAppl.class, args);
	}
	@Bean
	Consumer<Long>ordersUpdaterConsumer(){
		return service::updateOrder;
	}
}
