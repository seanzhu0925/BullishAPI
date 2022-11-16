package org.api.bullish.service;

import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.AddNewProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.api.bullish.model.ProductType.TV;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

class ProductServiceImplTest {

    private ProductServiceImpl productService;

    @BeforeEach
    public void setup() {
        productService = new ProductServiceImpl();
    }

    @Test
    public void test_create() {
        //Arrange
        AddNewProductRequest request = AddNewProductRequest.builder()
                .productName("dummyName")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .build();

        ProductDTO productDTO = ProductDTO
                .builder()
                .productName("dummyName")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .build();
        //Act
        ProductDTO product = productService.createProduct(request);
        //Assert
        System.out.println(String.format("!!!!!TEST product {%s}", product));
        assertNotNull(product);
        assertEquals(productDTO.getProductName(), product.getProductName());
    }
}
