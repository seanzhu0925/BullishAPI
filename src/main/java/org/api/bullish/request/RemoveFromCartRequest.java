package org.api.bullish.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class RemoveFromCartRequest {

    @NotBlank(message = "UserId is mandatory")
    private String userId;

    @NotBlank(message = "ProductName is mandatory")
    private String productName;

    @NotBlank(message = "Quantity is mandatory")
    @Min(1)
    private Integer quantity;
}
