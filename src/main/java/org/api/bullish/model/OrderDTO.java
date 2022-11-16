package org.api.bullish.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class OrderDTO {

    private String orderId;
    private String userId;
    private Double totalPrice;
    private List<ProductDTO> products;
    private Date createDate;

}
