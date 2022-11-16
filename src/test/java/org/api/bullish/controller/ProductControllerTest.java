package org.api.bullish.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.AddNewProductRequest;
import org.api.bullish.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.api.bullish.model.ProductType.TV;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = ProductController.class)
@ExtendWith(MockitoExtension.class)
//@SpringBootTest(classes = ElectronicStoreApplication.class)
//@SpringBootTest(classes = Application.class)
public class ProductControllerTest {

    private static final Gson gson = new GsonBuilder().create();

    @MockBean
    ProductService productService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        ProductController productController = new ProductController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void create_product() throws Exception {
        String url = "/v1/api/product/create";
        MockHttpServletResponse response = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(createDummyProductRequest())))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse();

        when(productService.createProduct(any())).thenReturn(createProductDTO());

        System.out.println(response.getContentAsString());
        assertNotNull(response.getContentAsString());
    }

    private AddNewProductRequest createDummyProductRequest() {
        return AddNewProductRequest.builder()
                .productName("dummyName")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .build();
    }

    private ProductDTO createProductDTO(){
        return  ProductDTO
                .builder()
                .productName("dummyName")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .build();
    }
}
