package org.api.bullish.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyPromocodeRequest {

    @NotBlank(message = "UserId is mandatory")
    private String userId;

    @NotBlank(message = "PromocodeName is mandatory")
    private String promocodeName;

    private Date consumeTime;
}
