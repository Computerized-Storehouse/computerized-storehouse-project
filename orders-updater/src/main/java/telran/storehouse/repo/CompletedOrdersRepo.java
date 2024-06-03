package telran.storehouse.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.storehouse.entity.CompletedOrder;

public interface CompletedOrdersRepo extends JpaRepository<CompletedOrder, Long> {

}
