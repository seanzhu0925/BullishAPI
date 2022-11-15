package org.api.bullish.request;

import lombok.Builder;
import lombok.Data;
import org.api.bullish.model.PromocodeType;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Builder
public class AddNewPromocodeRequest {

    @NotBlank(message = "PromocodeName is mandatory")
    private String promocodeName;
    @NotBlank(message = "PromocodeType is mandatory")
    private PromocodeType promocodeType;
    private Date createDate;
    private Date lastModifiedDate;

}
