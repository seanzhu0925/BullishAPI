package org.api.bullish.service;

import org.api.bullish.model.OrderDTO;
import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.AddToCartRequest;
import org.api.bullish.request.CheckoutRequest;
import org.api.bullish.request.RemoveFromCartRequest;

public interface CheckoutService {

    public ProductDTO addToCart(AddToCartRequest request);

    public void deleteFromCart(RemoveFromCartRequest request);

    public OrderDTO checkout(CheckoutRequest request);
}
