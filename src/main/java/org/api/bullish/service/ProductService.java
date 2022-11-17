package org.api.bullish.service;

import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.AddNewProductRequest;

public interface ProductService {

    ProductDTO createProduct(AddNewProductRequest request);

    void deleteProduct(String productName);
}
