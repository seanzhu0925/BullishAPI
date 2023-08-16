package org.api.bullish.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyPromocodeRequest {

    @NotBlank(message = "UserId is mandatory")
    private String userId;

    @NotBlank(message = "PromocodeName is mandatory")
    private String promocodeName;

    private Date consumeTime;
}
