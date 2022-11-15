package org.api.bullish.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CheckoutRequest {

    @NotBlank(message = "UserId is mandatory")
    private String userId;
}
