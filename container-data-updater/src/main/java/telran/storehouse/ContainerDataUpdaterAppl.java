package telran.storehouse;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;

import telran.storehouse.dto.ContainerDataDto;
import telran.storehouse.dto.NewStateDto;
import telran.storehouse.service.ContainerDataUpdaterService;

@SpringBootApplication
@RequiredArgsConstructor
public class ContainerDataUpdaterAppl {
	final ContainerDataUpdaterService service;

	public static void main(String args[]) {
		SpringApplication.run(ContainerDataUpdaterAppl.class, args);
	}

	@Bean
	Consumer<NewStateDto> containerFullnessUpdateConsumer() {
		return service::updateContanerCurrentFullness;
	}

	@Bean
	Consumer<ContainerDataDto> containerStatusUpdateConsumer() {
		return service::updateContanerStatus;
	}
}
