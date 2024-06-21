package telran.storehouse;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.CrudRepository;
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
	
	@BeforeEach
    void setUp() {
        mockSetUp(countRepo, countMap);
        mockSetUp(timeoutRepo, timeoutMap);
    }

    private <E> void mockSetUp(CrudRepository<E, Long> repo, HashMap<Long, E> map) {
        when(repo.findById(any(Long.class))).thenAnswer(new Answer<Optional<E>>(){
        	@Override
        	public Optional<E> answer(InvocationOnMock invocation) throws Throwable {
            Long sensorId = invocation.getArgument(0);
            E entity = map.get(sensorId);
            return entity == null ? Optional.ofNullable(null):Optional.of(entity);
        	}
        });
        when(repo.save(any())).then(new Answer<E>()  {
        	@Override
			public E answer(InvocationOnMock invocation) throws Throwable {
            E entity = invocation.getArgument(0);
            map.put(((ErrorCount) entity).getSensorId(), entity); 
            return entity;
        	}
        });
    }
    @Test
    void test(){
    	
    }
}
