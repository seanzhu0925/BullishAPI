package org.api.bullish.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class RemoveProductRequest {

    @NotBlank(message = "ProductName is mandatory")
    private String productName;
}
