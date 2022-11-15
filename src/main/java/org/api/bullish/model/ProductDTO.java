package org.api.bullish.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ProductDTO {

    private String productId;
    private String productName;
    private ProductType productType;
    private String description;
    private Double price;
    private Integer quantity;
    private Date createDate;
    private Date lastModifiedDate;

}
