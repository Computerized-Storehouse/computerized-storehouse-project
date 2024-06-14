package telran.storehouse.repo;

import org.springframework.data.repository.CrudRepository;

import telran.storehouse.model.SensorData;

public interface SensorDataRepo extends CrudRepository<SensorData, Long> {

}
