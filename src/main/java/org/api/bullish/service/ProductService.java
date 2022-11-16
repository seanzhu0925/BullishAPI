package org.api.bullish.service;

import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.AddNewProductRequest;
import org.api.bullish.request.RemoveProductRequest;

public interface ProductService {

    ProductDTO createProduct(AddNewProductRequest request);

    void deleteProduct(RemoveProductRequest request);
}
