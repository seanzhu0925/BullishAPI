package org.api.bullish.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.bullish.model.PromocodeType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddNewPromocodeRequest {

    @NotBlank(message = "PromocodeName is mandatory")
    private String promocodeName;

    @NotBlank(message = "PromocodeType is mandatory")
    private PromocodeType promocodeType;

    @NotBlank(message = "maxUseTime is mandatory")
    @Min(1)
    @Max(100)
    private Integer maxUseTime;

}
