package org.api.bullish.service;

import org.api.bullish.exception.DuplicatePromocodeException;
import org.api.bullish.exception.PromocodeMaxUsageReachedException;
import org.api.bullish.exception.PromocodeNotFoundException;
import org.api.bullish.model.ProductDTO;
import org.api.bullish.model.PromocodeDTO;
import org.api.bullish.request.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.api.bullish.model.ProductType.TV;
import static org.api.bullish.model.PromocodeType.HALF_PRICE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PromocodeServiceImplTest {

    private PromocodeServiceImpl promocodeService;

    private ProductServiceImpl productService;

    private CheckoutServiceImpl checkoutService;

    @BeforeEach
    public void setup() {
        productService = new ProductServiceImpl();
        checkoutService = new CheckoutServiceImpl(productService);
        promocodeService = new PromocodeServiceImpl(checkoutService);
    }

    @Test
    public void createPromocodeIsSuccessful() {
        //Arrange
        AddNewPromocodeRequest request = AddNewPromocodeRequest.builder()
                .promocodeName("dummyName")
                .promocodeType(HALF_PRICE)
                .build();

        PromocodeDTO promocodeDTO = PromocodeDTO
                .builder()
                .promocodeId("dummyId")
                .promocodeName("dummyName")
                .promocodeType(HALF_PRICE)
                .createDate(new Date())
                .lastModifiedDate(new Date())
                .build();
        //Act
        PromocodeDTO promocode = promocodeService.createPromocode(request);

        //Assert
        Assertions.assertNotNull(promocode);
        Assertions.assertNotNull(promocode.getCreateDate());
        Assertions.assertNotNull(promocode.getLastModifiedDate());
        Assertions.assertNotNull(promocode.getPromocodeId());
        Assertions.assertEquals(promocodeDTO.getPromocodeName(), promocode.getPromocodeName());
        Assertions.assertEquals(promocodeDTO.getPromocodeType(), promocode.getPromocodeType());
        Assertions.assertEquals(promocodeDTO.getMaxUseTime(), promocode.getMaxUseTime());
        Assertions.assertEquals(0, promocode.getTotalUsedTime());
    }

    @Test
    public void createPromocodeIsUnSuccessful() {
        //Arrange
        AddNewPromocodeRequest request = AddNewPromocodeRequest.builder()
                .promocodeName("dummyName")
                .promocodeType(HALF_PRICE)
                .build();

        //Act
        promocodeService.createPromocode(request);

        Exception exception = assertThrows(DuplicatePromocodeException.class, () -> {
            promocodeService.createPromocode(request);
        });

        String expectedMessage = "Duplicate Promocode has found in our DB";
        String actualMessage = exception.getMessage();

        //Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void applyPromocodeIsSuccessful() {

        //Arrange
        AddNewProductRequest addNewProductRequest = AddNewProductRequest.builder()
                .productName("dummyProduct")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .build();

        AddToCartRequest addToCartRequest = AddToCartRequest.builder()
                .userId("dummyUser")
                .productName("dummyProduct")
                .quantity(10)
                .build();

        CheckoutRequest checkoutRequest = CheckoutRequest.builder()
                .userId("dummyUser")
                .build();

        AddNewPromocodeRequest addNewPromocodeRequest = AddNewPromocodeRequest.builder()
                .promocodeName("dummyPromocode")
                .promocodeType(HALF_PRICE)
                .maxUseTime(100)
                .build();

        ApplyPromocodeRequest applyPromocodeRequest = ApplyPromocodeRequest.builder()
                .promocodeName("dummyPromocode")
                .userId("dummyUser")
                .consumeTime(new Date())
                .build();

        //Act
        ProductDTO productNew = productService.createProduct(addNewProductRequest);
        ProductDTO product = checkoutService.addToCart(addToCartRequest);
        PromocodeDTO promocode = promocodeService.createPromocode(addNewPromocodeRequest);
        Double priceAfterDiscount = promocodeService.applyPromocode(applyPromocodeRequest);

        //Assert
        Assertions.assertNotNull(product);
        Assertions.assertNotNull(productNew);
        Assertions.assertNotNull(promocode);
        Assertions.assertNotNull(priceAfterDiscount);
    }

    @Test
    public void applyPromocodeNotFound() {
        //Arrange
        AddNewProductRequest addNewProductRequest = AddNewProductRequest.builder()
                .productName("dummyProduct")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .build();

        AddToCartRequest addToCartRequest = AddToCartRequest.builder()
                .userId("dummyUser")
                .productName("dummyProduct")
                .quantity(10)
                .build();

        AddNewPromocodeRequest addNewPromocodeRequest = AddNewPromocodeRequest.builder()
                .promocodeName("dummyPromocode")
                .promocodeType(HALF_PRICE)
                .maxUseTime(100)
                .build();

        ApplyPromocodeRequest applyPromocodeRequest = ApplyPromocodeRequest.builder()
                .promocodeName("dummyNotFoundPromocode")
                .userId("dummyUser")
                .consumeTime(new Date())
                .build();

        //Act
        ProductDTO productNew = productService.createProduct(addNewProductRequest);
        ProductDTO product = checkoutService.addToCart(addToCartRequest);
        PromocodeDTO promocode = promocodeService.createPromocode(addNewPromocodeRequest);

        Exception exception = assertThrows(PromocodeNotFoundException.class, () -> promocodeService.applyPromocode(applyPromocodeRequest));

        String expectedMessage = "Promocode not found in our DB";
        String actualMessage = exception.getMessage();

        //Assert
        assertTrue(actualMessage.contains(expectedMessage));

        //Assert
        Assertions.assertNotNull(product);
        Assertions.assertNotNull(productNew);
        Assertions.assertNotNull(promocode);
    }

    @Test
    public void applyPromocodeExceedMaxUsage() {
        //Arrange
        AddNewProductRequest addNewProductRequest = AddNewProductRequest.builder()
                .productName("dummyProduct")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .build();

        AddToCartRequest addToCartRequest = AddToCartRequest.builder()
                .userId("dummyUser")
                .productName("dummyProduct")
                .quantity(10)
                .build();

        AddNewPromocodeRequest addNewPromocodeRequest = AddNewPromocodeRequest.builder()
                .promocodeName("dummyPromocode")
                .promocodeType(HALF_PRICE)
                .maxUseTime(1)
                .build();

        ApplyPromocodeRequest applyPromocodeRequest = ApplyPromocodeRequest.builder()
                .promocodeName("dummyPromocode")
                .userId("dummyUser")
                .consumeTime(new Date())
                .build();

        //Act
        ProductDTO productNew = productService.createProduct(addNewProductRequest);
        ProductDTO product = checkoutService.addToCart(addToCartRequest);
        PromocodeDTO promocode = promocodeService.createPromocode(addNewPromocodeRequest);
        promocodeService.applyPromocode(applyPromocodeRequest);

        Exception exception = assertThrows(PromocodeMaxUsageReachedException.class, () -> promocodeService.applyPromocode(applyPromocodeRequest));

        String expectedMessage = "Promocode can not be used anymore";
        String actualMessage = exception.getMessage();

        //Assert
        assertTrue(actualMessage.contains(expectedMessage));

        //Assert
        Assertions.assertNotNull(product);
        Assertions.assertNotNull(productNew);
        Assertions.assertNotNull(promocode);
    }
}
