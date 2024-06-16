package telran.storehouse.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.storehouse.dto.ContainerStatus;
import telran.storehouse.entity.ContainerData;

public interface ContainerDataRepo extends JpaRepository<ContainerData, Long> {
	@Query("SELECT thresholdValue FROM ContainerData WHERE sensorUsedId=:sensorUsedId and status=:status")
	Optional<Double> findThresholdValueBySensorUsedIdAndStatus(long sensorUsedId, ContainerStatus status);

}
