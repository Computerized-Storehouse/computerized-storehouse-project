package telran.storehouse.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.storehouse.entity.ContainerData;

public interface ContainerDataRepo extends JpaRepository<ContainerData, Long> {
//	@Query(value="select threshold_value from container_data where sensor_id=:sensorUsedId and status='OK'", nativeQuery = true)
	Double findThresholdValueBySensorUsedIdAndStatus(long sesnorUsedId, String status);

}
