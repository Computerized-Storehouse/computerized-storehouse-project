package telran.storehouse.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import telran.storehouse.dto.ErrorDataDto;

@RequiredArgsConstructor
@Getter
@RedisHash
public class ErrorCount {
 @Id
@NonNull
Long sensorId;
 @Setter
List<Double> errorsCounter = new ArrayList<Double>();
 	

 public ErrorDataDto build() {
		return new ErrorDataDto(sensorId, errorsCounter);
 	}
}