package org.api.bullish.service;

import org.api.bullish.exception.DuplicateProductException;
import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.AddNewProductRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.api.bullish.model.ProductType.TV;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplTest {

    private ProductServiceImpl productService;

    @BeforeEach
    public void setup() {
        productService = new ProductServiceImpl();
    }

    @Test
    public void createProductIsSuccessful() {
        //Arrange
        AddNewProductRequest request = AddNewProductRequest.builder()
                .productName("dummyName")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .build();

        ProductDTO productDTO = ProductDTO
                .builder()
                .productId("dummyId")
                .productName("dummyName")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .createDate(new Date())
                .lastModifiedDate(new Date())
                .build();
        //Act
        ProductDTO product = productService.createProduct(request);

        //Assert
        Assertions.assertNotNull(product);
        Assertions.assertNotNull(productDTO.getCreateDate());
        Assertions.assertNotNull(productDTO.getLastModifiedDate());
        Assertions.assertNotNull(productDTO.getProductId());
        Assertions.assertEquals(productDTO.getProductName(), product.getProductName());
        Assertions.assertEquals(productDTO.getProductType(), product.getProductType());
        Assertions.assertEquals(productDTO.getPrice(), product.getPrice());
        Assertions.assertEquals(productDTO.getQuantity(), product.getQuantity());
    }

    @Test
    public void createProductIsUnSuccessful() {
        //Arrange
        AddNewProductRequest request = AddNewProductRequest.builder()
                .productName("dummyName")
                .productType(TV)
                .build();
        //Act
        productService.createProduct(request);

        Exception exception = assertThrows(DuplicateProductException.class, () -> {
            productService.createProduct(request);
        });

        String expectedMessage = "Duplicate Product has found in our DB";
        String actualMessage = exception.getMessage();

        //Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void deleteProductIsSuccessful() {
        //Arrange
        AddNewProductRequest request = AddNewProductRequest.builder()
                .productName("dummyName")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .build();

        ProductDTO productDTO = ProductDTO
                .builder()
                .productId("dummyId")
                .productName("dummyName")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .createDate(new Date())
                .lastModifiedDate(new Date())
                .build();
        //Act
        ProductDTO product = productService.createProduct(request);

        //Assert
        Assertions.assertNotNull(product);
        Assertions.assertNotNull(productDTO.getCreateDate());
        Assertions.assertNotNull(productDTO.getLastModifiedDate());
        Assertions.assertNotNull(productDTO.getProductId());
        Assertions.assertEquals(productDTO.getProductName(), product.getProductName());
        Assertions.assertEquals(productDTO.getProductType(), product.getProductType());
        Assertions.assertEquals(productDTO.getPrice(), product.getPrice());
        Assertions.assertEquals(productDTO.getQuantity(), product.getQuantity());

        //Arrange

        //Act
        productService.deleteProduct("dummyName");

        //Assert
        assertFalse(productService.getProductDTOMap().containsKey("dummyName"));
    }

    @Test
    public void deleteProductIsUnSuccessful() {
        //Arrange
        AddNewProductRequest request = AddNewProductRequest.builder()
                .productName("dummyName")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .build();

        ProductDTO productDTO = ProductDTO
                .builder()
                .productId("dummyId")
                .productName("dummyName")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .createDate(new Date())
                .lastModifiedDate(new Date())
                .build();
        //Act
        ProductDTO product = productService.createProduct(request);

        //Assert
        Assertions.assertNotNull(product);
        Assertions.assertNotNull(productDTO.getCreateDate());
        Assertions.assertNotNull(productDTO.getLastModifiedDate());
        Assertions.assertNotNull(productDTO.getProductId());
        Assertions.assertEquals(productDTO.getProductName(), product.getProductName());
        Assertions.assertEquals(productDTO.getProductType(), product.getProductType());
        Assertions.assertEquals(productDTO.getPrice(), product.getPrice());
        Assertions.assertEquals(productDTO.getQuantity(), product.getQuantity());

        //Arrange

        //Act
        Exception exception = assertThrows(DuplicateProductException.class, () -> {
            productService.deleteProduct("dummyName1");
        });

        String expectedMessage = "Product has not found in our DB";
        String actualMessage = exception.getMessage();

        //Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
