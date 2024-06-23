package telran.storehouse;

import java.util.function.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import telran.storehouse.dto.SensorDataDto;
import telran.storehouse.service.ValuesAnalyzerService;

@RequiredArgsConstructor
@SpringBootApplication
public class ValuesAnalyzerAppl {
	
	final ValuesAnalyzerService service;
	
	public static void main(String[] args) {
		SpringApplication.run(ValuesAnalyzerAppl.class, args);

	}
	@Bean
	Consumer<SensorDataDto> valuesAnalyzerConsumer(){
		return service::sensorDataAnalyzing;
	
	}
	
}
