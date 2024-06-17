package telran.storehouse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.storehouse.urlConstants.UrlConstants.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import telran.storehouse.dto.OrderDataDto;
import telran.storehouse.dto.OrderStatus;
import telran.storehouse.dto.ProductDto;
import telran.storehouse.exceptions.OrderNotFoundException;
import telran.storehouse.service.OrderDataProviderService;


@WebMvcTest
class OrderDataProviderControllerTest {
	
	private static final long ORDER_ID = 100;
   

    @Value("${app.order.provider.urn}")
    private String urn;

    private String URL;
    

    @BeforeEach
    void setUrl() {
    	URL = HOST + PORT + urn + Long.toString(ORDER_ID);
    }

			
	@MockBean
	private OrderDataProviderService service;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	
	
	@Test
	void getOrderData_normal() throws Exception {
		final ProductDto product = new ProductDto("Product", "Units");
		final OrderDataDto orderData = new OrderDataDto(ORDER_ID, 4321L, "A123", product,
	            System.currentTimeMillis(), System.currentTimeMillis(), 20L, "creator", OrderStatus.OPEN);
		when(service.getOrderData(ORDER_ID)).thenReturn(orderData);
		String jsonExpected = mapper.writeValueAsString(orderData);
		String response = mockMvc.perform(get(URL)).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals(jsonExpected, response);
	}
	 @Test
	    void getOrderData_id_not_found() throws Exception {
	        when(service.getOrderData(ORDER_ID)).thenThrow(new OrderNotFoundException());
	        mockMvc.perform(get(URL)).andExpect(status().isNotFound());
	    }
	
}
