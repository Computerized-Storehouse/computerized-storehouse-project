package telran.storehouse;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.dto.NewStateDto;
import telran.storehouse.dto.SensorDataDto;
import telran.storehouse.service.UpdateDetectorService;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class UpdateDetectorAppl {
	@Value("${app.update.detector.producer.binding.name}")
	private String producerBindingName;
	final UpdateDetectorService updateDetectorService;
	final StreamBridge streamBridge;

	public static void main(String[] args) {

		SpringApplication.run(UpdateDetectorAppl.class, args);
	}

	@Bean
	Consumer<SensorDataDto> updateDetectorConsumer() {

		return this::sensorDataMatching;
	}

	void sensorDataMatching(SensorDataDto sensorDataDto) {
		log.trace("received {}", sensorDataDto);
		Double difference = updateDetectorService.getDifference(sensorDataDto);
		long sensorId = sensorDataDto.id();
		if (difference != null) {
			NewStateDto newState = new NewStateDto(sensorDataDto, difference);
			streamBridge.send(producerBindingName, newState);
			log.debug("difference in sensor data: {} has been sent to {} binding name", difference,
					producerBindingName);
		} else {
			log.trace("no difference in sensor {} performance", sensorId);
		}

	}

}
