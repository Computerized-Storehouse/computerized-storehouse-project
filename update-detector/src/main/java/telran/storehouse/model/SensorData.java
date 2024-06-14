package telran.storehouse.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.*;

@RedisHash
@Getter
public class SensorData {
	@Id
	long sensorId;
	@Setter
	Double fullnes;
	
	public SensorData(long sensorId, Double fullnes) {
		this.sensorId = sensorId;
		this.fullnes = fullnes;
	}

}
