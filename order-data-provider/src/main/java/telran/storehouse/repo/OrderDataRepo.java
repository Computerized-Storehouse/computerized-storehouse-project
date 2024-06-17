package telran.storehouse.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import telran.storehouse.entity.OrderData;

public interface OrderDataRepo extends JpaRepository<OrderData, Long> {
	
}
