package com.israf.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BasketSystemIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
    void shouldAddProductToBasket_WhenRequestIsValid() throws Exception {
       
        String requestBody = "{ \"inventoryId\": 1, \"quantity\": 2 }";

        mockMvc.perform(post("/api/basket/add") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.totalAmount").exists()); 
    }
	
	
	

}
