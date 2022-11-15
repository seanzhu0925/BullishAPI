package org.api.bullish.controller;

import org.api.bullish.model.OrderDTO;
import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.AddToCartRequest;
import org.api.bullish.request.CheckoutRequest;
import org.api.bullish.request.RemoveFromCartRequest;
import org.api.bullish.response.ApiResponse;
import org.api.bullish.service.CheckoutServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Objects;

@RestController
@RequestMapping("/v1/api/checkout")
public class CheckoutController {

    private final CheckoutServiceImpl checkoutService;

    @Autowired
    public CheckoutController(final CheckoutServiceImpl checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/addToCart")
    public ResponseEntity<ApiResponse<ProductDTO>> addToCart(@Valid @RequestBody AddToCartRequest request) {
        if (Objects.isNull(request.getProductName())) {
            return new ResponseEntity<>(new ApiResponse<>(false, Collections.singletonList("product is empty"), HttpStatus.BAD_REQUEST, null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse<>(true, null, HttpStatus.CREATED, checkoutService.addToCart(request)), HttpStatus.CREATED);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<String>> deleteFromCart(@RequestBody RemoveFromCartRequest request) {
        if (Objects.isNull(request.getProductName())) {
            return new ResponseEntity<>(new ApiResponse<>(false, Collections.singletonList("product is empty"), HttpStatus.BAD_REQUEST, null), HttpStatus.BAD_REQUEST);
        }
        checkoutService.deleteFromCart(request);
        return new ResponseEntity<>(new ApiResponse<>(true, null, HttpStatus.OK, "Product has been removed successfully"), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<OrderDTO>> checkout(@RequestBody CheckoutRequest request) {
        if (Objects.isNull(request.getUserId())) {
            return new ResponseEntity<>(new ApiResponse<>(false, Collections.singletonList("userId can not be null"), HttpStatus.BAD_REQUEST, null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse<>(true, null, HttpStatus.OK, checkoutService.checkout(request)), HttpStatus.OK);
    }
}
