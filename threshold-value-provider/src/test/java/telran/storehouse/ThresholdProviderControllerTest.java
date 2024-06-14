package telran.storehouse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.storehouse.urlConstants.UrlConstants.HOST;
import static telran.storehouse.urlConstants.UrlConstants.PORT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import telran.storehouse.service.ThresholdProviderService;
import telran.storehouse.exceptions.SensorNotFoundException;

@WebMvcTest
class ThresholdProviderControllerTest {
	@Value("${app.threshold.provider.urn}")
	private String urn;
	private static final long SENSOR_ID = 123;
	private static final double VALUE = 40;
	private static final long SENSOR_NOT_FOUND = 578;
	@MockBean
	ThresholdProviderService thresholdProvider;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	private String URL;
	
	@PostConstruct
	void setUrl() {
		URL = HOST + PORT + urn;
	}


	@Test
	void value_getData_normal() throws Exception {
		when(thresholdProvider.getThresholdValue(SENSOR_ID)).thenReturn(VALUE);
		String expectedJson = mapper.writeValueAsString(VALUE);
		String actualJson = mockMvc.perform(get(URL + SENSOR_ID)).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(expectedJson, actualJson);
	}
	
	@Test
	void value_getData_exception() throws Exception {
		when(thresholdProvider.getThresholdValue(SENSOR_NOT_FOUND)).thenThrow(new SensorNotFoundException(Long.toString(SENSOR_NOT_FOUND)));
		mockMvc.perform(get(URL + SENSOR_NOT_FOUND)).andExpect(status().isNotFound());
	}

}
