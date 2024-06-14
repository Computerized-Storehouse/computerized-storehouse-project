package telran.storehouse;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import telran.storehouse.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class ReplenishmentDetectorAppl {
	

	public static void main(String[] args) {
		SpringApplication.run(ReplenishmentDetectorAppl.class, args);

	}
	
	@Bean
	Consumer<NewStateDto> replenishmentDetectorConsumer(){
		return this::fullDetectorProcessing;
	}
	
	void fullDetectorProcessing(NewStateDto newStateDto) {
		
	}

}
