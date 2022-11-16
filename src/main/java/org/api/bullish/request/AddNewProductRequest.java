package org.api.bullish.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.bullish.model.ProductType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddNewProductRequest {

    @NotBlank(message = "ProductName is mandatory")
    private String productName;

    @NotBlank(message = "ProductType is mandatory")
    private ProductType productType;

    @NotBlank(message = "Price is mandatory")
    @DecimalMin(value = "0.01", message = "Price is mandatory and needs to be bigger than 0.01")
    private Double price;

    private String description;

    @NotBlank(message = "Quantity is mandatory")
    @Min(1)
    private Integer quantity;
}
