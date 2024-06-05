package telran.storehouse.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.storehouse.entity.ContainerData;

public interface ContainerDataRepo extends JpaRepository<ContainerData, Long> {
	Optional<ContainerData> findBySensorUsedId(long sensorUsedId);
}
