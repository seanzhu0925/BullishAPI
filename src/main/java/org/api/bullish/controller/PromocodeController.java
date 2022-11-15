package org.api.bullish.controller;

import org.api.bullish.model.PromocodeDTO;
import org.api.bullish.request.AddNewPromocodeRequest;
import org.api.bullish.request.ApplyPromocodeRequest;
import org.api.bullish.response.ApiResponse;
import org.api.bullish.service.PromocodeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Objects;

@RestController
@RequestMapping("/v1/api/promocode")
public class PromocodeController {

    private final PromocodeServiceImpl promocodeService;

    @Autowired
    public PromocodeController(PromocodeServiceImpl promocodeService) {
        this.promocodeService = promocodeService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PromocodeDTO>> createPromocode(@RequestBody AddNewPromocodeRequest request) {
        if (Objects.isNull(request.getPromocodeName())) {
            return new ResponseEntity<>(new ApiResponse<>(false, Collections.singletonList("product is empty"), HttpStatus.BAD_REQUEST, null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse<>(true, null, HttpStatus.CREATED, promocodeService.createPromocode(request)), HttpStatus.CREATED);
    }

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<String>> applyPromocode(@RequestBody ApplyPromocodeRequest request) {
        if (Objects.isNull(request.getPromocodeName())) {
            return new ResponseEntity<>(new ApiResponse<>(false, Collections.singletonList("promocode is empty"), HttpStatus.BAD_REQUEST, null), HttpStatus.BAD_REQUEST);
        }
        promocodeService.applyPromocode(request);
        return new ResponseEntity<>(new ApiResponse<>(true, null, HttpStatus.OK, "promocode has been applied to current shopping cart"), HttpStatus.OK);
    }
}
