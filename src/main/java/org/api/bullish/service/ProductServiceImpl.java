package org.api.bullish.service;

import org.api.bullish.exception.DuplicateProductException;
import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.AddNewProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProductServiceImpl implements ProductService {

    // In memory store the product inventory info
    private final Map<String, ProductDTO> productDTOMap;

    @Autowired
    public ProductServiceImpl() {
        this.productDTOMap = new ConcurrentHashMap<>();
    }

    @Override
    public ProductDTO createProduct(AddNewProductRequest request) {
        if (productDTOMap.containsKey(request.getProductName())) {
            throw new DuplicateProductException("Duplicate Product has found in our DB");
        }

        return generateProduct(request);
    }

    @Override
    public void deleteProduct(String productName) {
        if (!productDTOMap.containsKey(productName)) {
            throw new DuplicateProductException("Product has not found in our DB");
        }

        productDTOMap.remove(productName);
    }

    private ProductDTO generateProduct(AddNewProductRequest request) {
        UUID uuid = UUID.randomUUID();

        ProductDTO productDTO = ProductDTO.builder()
                .productId(uuid.toString())
                .productName(request.getProductName())
                .productType(request.getProductType())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .createDate(new Date())
                .lastModifiedDate(new Date())
                .description(request.getDescription())
                .build();

        productDTOMap.put(productDTO.getProductName(), productDTO);

        return productDTO;
    }

    public Map<String, ProductDTO> getProductDTOMap() {
        return productDTOMap;
    }
}
