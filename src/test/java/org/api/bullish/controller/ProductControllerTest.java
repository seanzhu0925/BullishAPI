package org.api.bullish.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.AddNewProductRequest;
import org.api.bullish.request.RemoveProductRequest;
import org.api.bullish.service.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.api.bullish.model.ProductType.TV;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest {

    private static final Gson gson = new GsonBuilder().create();

    @MockBean
    ProductServiceImpl productService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        ProductController productController = new ProductController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void createProductIsSuccessful() throws Exception {
        String url = "/v1/api/product/create";
        AddNewProductRequest request = createDummyProductRequest();
        MockHttpServletResponse response = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse();

        when(productService.createProduct(any())).thenReturn(createProductDTO());
        Assertions.assertNotNull(response.getContentAsString());
    }

    @Test
    public void deleteProductIsSuccessful() throws Exception {
        String url = "/v1/api/product/delete";
        RemoveProductRequest request = deleteDummyProductRequest();
        mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse();
        doNothing().when(productService).deleteProduct(any());
    }

    private AddNewProductRequest createDummyProductRequest() {
        return AddNewProductRequest.builder()
                .productName("dummyName")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .build();
    }

    private RemoveProductRequest deleteDummyProductRequest() {
        return RemoveProductRequest.builder()
                .productName("dummyName")
                .build();
    }

    private ProductDTO createProductDTO() {
        return ProductDTO
                .builder()
                .productName("dummyName")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .build();
    }
}