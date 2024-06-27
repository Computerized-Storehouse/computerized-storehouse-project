package telran.storehouse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.storehouse.dto.OrderDataDto;
import telran.storehouse.service.OrderDataProviderService;
import static telran.storehouse.messages.ValidationErrorMessages.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderDataProviderController {
	final OrderDataProviderService service;

	@GetMapping("${app.order.provider.urn}" + "{id}")
	OrderDataDto getOrderData(@PathVariable(name = "id") @NotNull(message = MISSING_ORDER_ID) long orderId) {
		OrderDataDto orderDataDto = service.getOrderData(orderId);
		log.debug("order data received is {}", orderDataDto);
		return orderDataDto;
	}

}
