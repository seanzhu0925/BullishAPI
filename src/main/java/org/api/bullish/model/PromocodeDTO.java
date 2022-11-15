package org.api.bullish.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PromocodeDTO {

    private String promocodeId;
    private String promocodeName;
    private PromocodeType promocodeType;
    private Integer totalUsedTime;
    private Integer maxUseTime;
    private Date createDate;
    private Date lastModifiedDate;
}
