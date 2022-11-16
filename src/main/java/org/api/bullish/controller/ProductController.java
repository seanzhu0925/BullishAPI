package org.api.bullish.controller;

import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.AddNewProductRequest;
import org.api.bullish.request.RemoveProductRequest;
import org.api.bullish.response.ApiResponse;
import org.api.bullish.service.ProductService;
import org.api.bullish.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Objects;

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
//        if (Objects.isNull(request.getProductName())) {
//            return new ResponseEntity<>(new ApiResponse<>(false, Collections.singletonList("product is empty"), HttpStatus.BAD_REQUEST, null), HttpStatus.BAD_REQUEST);
//        }
        return  ResponseEntity.ok(productService.createProduct(request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@RequestBody RemoveProductRequest request) {
        if (Objects.isNull(request.getProductName())) {
            return new ResponseEntity<>(new ApiResponse<>(false, Collections.singletonList("product is empty"), HttpStatus.BAD_REQUEST, null), HttpStatus.BAD_REQUEST);
        }
        productService.deleteProduct(request);
        return new ResponseEntity<>(new ApiResponse<>(true, null, HttpStatus.OK, "Product has benn deleted"), HttpStatus.OK);
    }

}
