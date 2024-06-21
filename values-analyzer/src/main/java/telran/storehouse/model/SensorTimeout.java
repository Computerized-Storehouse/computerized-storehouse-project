package telran.storehouse.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import telran.storehouse.dto.SensorErrorTimeoutDto;

@RequiredArgsConstructor
@Getter
@RedisHash
public class SensorTimeout {
 @Id
 @NonNull Long sensorId;
 @Setter
 @NonNull Long lastCorrectTimestamp;
 @Temporal(TemporalType.TIMESTAMP)
 @Setter
 List<Long> errorsTimestamp = new ArrayList<Long>();
 
 public SensorErrorTimeoutDto build() {
		return new SensorErrorTimeoutDto(sensorId, lastCorrectTimestamp, errorsTimestamp);
	}
}
