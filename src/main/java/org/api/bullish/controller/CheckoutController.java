package org.api.bullish.controller;

import org.api.bullish.model.OrderDTO;
import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.AddToCartRequest;
import org.api.bullish.request.CheckoutRequest;
import org.api.bullish.request.RemoveFromCartRequest;
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

    @DeleteMapping("/remove")
    public ResponseEntity<String> deleteFromCart(@RequestBody RemoveFromCartRequest request) {
        if (Objects.isNull(request.getProductName())) {
            return ResponseEntity.of(Optional.of("ProductName is empty"));
        }

        if (Objects.isNull(request.getUserId())) {
            return ResponseEntity.of(Optional.of("UserId is empty"));
        }

        if (Objects.isNull(request.getQuantity())) {
            return ResponseEntity.of(Optional.of("Quantity is empty"));
        }
        checkoutService.deleteFromCart(request);
        return ResponseEntity.ok("Product has been removed successfully");
    }

    @GetMapping("/")
    public ResponseEntity<OrderDTO> checkout(@RequestBody CheckoutRequest request) {
        if (Objects.isNull(request.getUserId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(checkoutService.checkout(request));
    }
}
