package telran.storehouse;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import telran.storehouse.repo.*;
import telran.storehouse.model.ErrorCount;
import telran.storehouse.model.SensorTimeout;
import telran.storehouse.service.ValuesAnalyzerService;

@SpringBootTest

class ValuesAnalyzerServiceTest {
	
	@Autowired
	ValuesAnalyzerService service;
	@MockBean
	ErrorsCountRepo countRepo;
	@MockBean
	SensorTimeoutRepo timeoutRepo;
	
	HashMap<Long, ErrorCount> countMap = new HashMap<>();
	HashMap<Long, SensorTimeout> timeoutMap = new HashMap<>();
	
	
	  void mockSetUpForErrorCount() {
		  when(countRepo.findById(any(Long.class))).then(new Answer<Optional<ErrorCount>>() {

			@Override
			public Optional<ErrorCount> answer(InvocationOnMock invocation) throws Throwable {
				Long sensorId = invocation.getArgument(0);
				ErrorCount errorCount = countMap.get(sensorId);
				return errorCount == null ? Optional.ofNullable(null) : Optional.of(errorCount);
			}
		});
		when(countRepo.save(any(ErrorCount.class)))
		.then(new Answer<ErrorCount>() {

			@Override
			public ErrorCount answer(InvocationOnMock invocation) throws Throwable {
				ErrorCount errorCount = invocation.getArgument(0);
				countMap.put(errorCount.getSensorId(), errorCount);
				return errorCount;
			}
		});
	  }
	  void mockSetUpForSensorTimeout() {
		  when(timeoutRepo.findById(any(Long.class))).then(new Answer<Optional<SensorTimeout>>() {

			@Override
			public Optional<SensorTimeout> answer(InvocationOnMock invocation) throws Throwable {
				Long sensorId = invocation.getArgument(0);
				SensorTimeout sensorTimeout = timeoutMap.get(sensorId);
				return sensorTimeout == null ? Optional.ofNullable(null) : Optional.of(sensorTimeout);
			}
		});
		when(timeoutRepo.save(any(SensorTimeout.class)))
		.then(new Answer<SensorTimeout>() {

			@Override
			public SensorTimeout answer(InvocationOnMock invocation) throws Throwable {
				SensorTimeout sensorTimeout = invocation.getArgument(0);
				timeoutMap.put(sensorTimeout.getSensorId(), sensorTimeout);
				return sensorTimeout;
			}
		});
	  }
}
