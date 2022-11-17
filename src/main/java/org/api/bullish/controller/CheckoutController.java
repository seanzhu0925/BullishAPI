package org.api.bullish.controller;

import org.api.bullish.model.OrderDTO;
import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.AddToCartRequest;
import org.api.bullish.service.CheckoutServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/checkout")
public class CheckoutController {

    private final CheckoutServiceImpl checkoutService;

    @Autowired
    public CheckoutController(final CheckoutServiceImpl checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/addToCart")
    public ResponseEntity<ProductDTO> addToCart(@Valid @RequestBody AddToCartRequest request) {
        if (Objects.isNull(request.getProductName())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(checkoutService.addToCart(request));
    }

    @DeleteMapping("/userId/{userId}/productName/{productName}/quantity/{quantity}")
    public ResponseEntity<String> deleteFromCart(@PathVariable final String userId, @PathVariable final String productName, @PathVariable final Integer quantity) {
        if (Objects.isNull(userId)) {
            return ResponseEntity.of(Optional.of("ProductName is empty"));
        }

        if (Objects.isNull(productName)) {
            return ResponseEntity.of(Optional.of("UserId is empty"));
        }

        if (Objects.isNull(quantity)) {
            return ResponseEntity.of(Optional.of("Quantity is empty"));
        }
        checkoutService.deleteFromCart(userId, productName, quantity);
        return ResponseEntity.ok("Product has been removed successfully");
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<OrderDTO> checkout(@PathVariable final String userId) {
        if (Objects.isNull(userId)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(checkoutService.checkout(userId));
    }
}
