package org.api.bullish.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddToCartRequest {

    @NotBlank(message = "UserId is mandatory")
    private String userId;

    @NotBlank(message = "ProductName is mandatory")
    private String productName;

    @Min(1)
    private Integer quantity;

}
