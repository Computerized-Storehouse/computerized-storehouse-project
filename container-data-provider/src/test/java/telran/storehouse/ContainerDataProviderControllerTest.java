package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.storehouse.urlConstants.UrlConstants.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import telran.storehouse.dto.ContainerDataDto;
import telran.storehouse.dto.ProductDto;
import telran.storehouse.exceptions.ContainerNotFoundException;
import telran.storehouse.service.ContainerDataProviderService;

@WebMvcTest
class ContainerDataProviderControllerTest {
	@Value("${app.container.provider.urn}")
	private String urn;

	private static final long ID_1 = 1;
	private String URL;
	private ProductDto product = new ProductDto("product1", "box1");

	@PostConstruct
	void setUrl() {
		URL = HOST + PORT + urn + Long.toString(ID_1);
	}

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ContainerDataProviderService service;

	@Autowired
	ObjectMapper mapper;

	@Test
	void getContainerData_normalFlow_success() throws Exception {
		ContainerDataDto expected = new ContainerDataDto(1, ID_1, "A1", 200, 0, "ok", 50, product);
		when(service.getContainerData(ID_1)).thenReturn(expected);
		String expectedJSON = mapper.writeValueAsString(expected);
		String response = mockMvc.perform(get(URL)).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(expectedJSON, response);
	}

	@Test
	void getContainerData_idNotExists_notFoundException() throws Exception {
		when(service.getContainerData(ID_1)).thenThrow(new ContainerNotFoundException());
		mockMvc.perform(get(URL)).andExpect(status().isNotFound());
	}
}
