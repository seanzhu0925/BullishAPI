package org.api.bullish.service;

import org.api.bullish.model.OrderDTO;
import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.AddToCartRequest;

public interface CheckoutService {

    public ProductDTO addToCart(AddToCartRequest request);

    public void deleteFromCart(final String userId, final String productName, final Integer quantity);

    public OrderDTO checkout(final String userId);
}
