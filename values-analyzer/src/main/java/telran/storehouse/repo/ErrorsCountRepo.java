package telran.storehouse.repo;

import org.springframework.data.repository.CrudRepository;
import telran.storehouse.model.ErrorCount;

public interface ErrorsCountRepo extends CrudRepository<ErrorCount, Long> {

}
