package org.api.bullish.controller;

import org.api.bullish.model.PromocodeDTO;
import org.api.bullish.request.AddNewPromocodeRequest;
import org.api.bullish.request.ApplyPromocodeRequest;
import org.api.bullish.service.PromocodeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/promocode")
public class PromocodeController {

    private final PromocodeServiceImpl promocodeService;

    @Autowired
    public PromocodeController(PromocodeServiceImpl promocodeService) {
        this.promocodeService = promocodeService;
    }

    @PostMapping("/create")
    public ResponseEntity<PromocodeDTO> createPromocode(@RequestBody AddNewPromocodeRequest request) {
        if (Objects.isNull(request.getPromocodeName())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(promocodeService.createPromocode(request));
    }

    @PostMapping("/apply")
    public ResponseEntity<String> applyPromocode(@RequestBody ApplyPromocodeRequest request) {
        if (Objects.isNull(request.getPromocodeName())) {
            return ResponseEntity.of(Optional.of("promocode is empty"));
        }
        promocodeService.applyPromocode(request);
        return ResponseEntity.ok("promocode has been applied to current shopping cart");
    }
}
