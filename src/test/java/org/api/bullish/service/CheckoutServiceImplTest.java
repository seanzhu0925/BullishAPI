package org.api.bullish.service;

import org.api.bullish.exception.ProductNotFoundException;
import org.api.bullish.model.OrderDTO;
import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.api.bullish.model.ProductType.TV;
import static org.api.bullish.model.PromocodeType.HALF_PRICE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckoutServiceImplTest {

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
    public void addProductToCartIsSuccessful() {
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

        //Act
        ProductDTO productNew = productService.createProduct(addNewProductRequest);
        ProductDTO product = checkoutService.addToCart(addToCartRequest);

        //Assert
        Assertions.assertNotNull(productNew);
        Assertions.assertNotNull(product);
        Assertions.assertEquals(10, (int) checkoutService.getShoppingCart().get("dummyUser").get("dummyProduct").getQuantity());
    }

    @Test
    public void addProductToCartNotEnoughProductInventory() {
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
                .quantity(30)
                .build();

        //Act
        productService.createProduct(addNewProductRequest);

        //Assert
        Exception exception = assertThrows(ProductNotFoundException.class, () -> checkoutService.addToCart(addToCartRequest));

        String expectedMessage = "Not enough product found in our inventory!";
        String actualMessage = exception.getMessage();

        //Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void addProductToCartNotFoundProduct() {
        //Arrange
        AddNewProductRequest addNewProductRequest = AddNewProductRequest.builder()
                .productName("dummyProduct")
                .productType(TV)
                .price(9.9)
                .quantity(20)
                .build();

        AddToCartRequest addToCartRequest = AddToCartRequest.builder()
                .userId("dummyUser")
                .productName("dummyNotFoundProduct")
                .quantity(30)
                .build();

        //Act
        productService.createProduct(addNewProductRequest);

        //Assert
        Exception exception = assertThrows(ProductNotFoundException.class, () -> checkoutService.addToCart(addToCartRequest));

        String expectedMessage = "No product found in our inventory!";
        String actualMessage = exception.getMessage();

        //Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void removeProductFromCartIsSuccessful() {
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

        //Act
        ProductDTO productNew = productService.createProduct(addNewProductRequest);
        ProductDTO product = checkoutService.addToCart(addToCartRequest);
        checkoutService.deleteFromCart("dummyUser", "dummyProduct", 10);

        //Assert
        Assertions.assertNotNull(productNew);
        Assertions.assertNotNull(product);
        Assertions.assertEquals(0, (int) checkoutService.getShoppingCart().get("dummyUser").get("dummyProduct").getQuantity());
        Assertions.assertEquals(20, (int) productService.getProductDTOMap().get("dummyProduct").getQuantity());
    }

    @Test
    public void removeProductFromCartProductNotFound() {
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

        //Act
        ProductDTO productNew = productService.createProduct(addNewProductRequest);
        ProductDTO product = checkoutService.addToCart(addToCartRequest);

        //Assert
        Exception exception = assertThrows(ProductNotFoundException.class, () -> checkoutService.deleteFromCart("dummyUser", "dummyRemoveProduct", 10));

        String expectedMessage = "Product not found in shopping cart!";
        String actualMessage = exception.getMessage();

        //Assert
        Assertions.assertNotNull(productNew);
        Assertions.assertNotNull(product);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void removeProductFromCartProductNotEnough() {
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

        //Act
        ProductDTO productNew = productService.createProduct(addNewProductRequest);
        ProductDTO product = checkoutService.addToCart(addToCartRequest);

        //Assert
        Exception exception = assertThrows(ProductNotFoundException.class, () -> checkoutService.deleteFromCart("dummyUser", "dummyProduct", 15));

        String expectedMessage = "Not enough product in the shopping cart to remove!";
        String actualMessage = exception.getMessage();

        //Assert
        Assertions.assertNotNull(productNew);
        Assertions.assertNotNull(product);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void checkoutGetTotalPrice() {
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

        //Act
        ProductDTO productNew = productService.createProduct(addNewProductRequest);
        ProductDTO product = checkoutService.addToCart(addToCartRequest);
        OrderDTO order = checkoutService.checkout("dummyUser");

        //Assert
        Assertions.assertNotNull(productNew);
        Assertions.assertNotNull(product);
        Assertions.assertNotNull(order);
        Assertions.assertEquals(99.0, (double) order.getTotalPrice());
        Assertions.assertEquals("dummyUser", order.getUserId());
    }

    @Test
    public void checkoutGetTotalPriceAfterDiscountApplied() {
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
                .promocodeName("dummyPromocode")
                .userId("dummyUser")
                .consumeTime(new Date())
                .build();

        //Act
        ProductDTO productNew = productService.createProduct(addNewProductRequest);
        ProductDTO product = checkoutService.addToCart(addToCartRequest);
        promocodeService.createPromocode(addNewPromocodeRequest);
        promocodeService.applyPromocode(applyPromocodeRequest);
        OrderDTO order = checkoutService.checkout("dummyUser");

        //Assert
        Assertions.assertNotNull(productNew);
        Assertions.assertNotNull(product);
        Assertions.assertNotNull(order);
        Assertions.assertEquals(79.2, (double) order.getTotalPrice());
        Assertions.assertEquals("dummyUser", order.getUserId());
    }
}
