package org.api.bullish.controller;

import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.AddNewProductRequest;
import org.api.bullish.request.RemoveProductRequest;
import org.api.bullish.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody AddNewProductRequest request) {
        if (Objects.isNull(request.getProductName())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProduct(@RequestBody RemoveProductRequest request) {
        if (Objects.isNull(request.getProductName())) {
            return ResponseEntity.of(Optional.of("productName is empty"));
        }
        productService.deleteProduct(request);
        return ResponseEntity.ok("Product has benn deleted");
    }

}
