package telran.storehouse.repo;

import org.springframework.data.repository.CrudRepository;

import telran.storehouse.model.SensorTimeout;

public interface SensorTimeoutRepo extends CrudRepository<SensorTimeout, Long> {

}
