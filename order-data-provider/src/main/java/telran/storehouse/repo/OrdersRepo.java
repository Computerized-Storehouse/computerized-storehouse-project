package telran.storehouse.repo;
import org.springframework.data.jpa.repository.JpaRepository;

import telran.storehouse.entity.Order;


public interface OrdersRepo extends JpaRepository<Order, Long> {
	
}
