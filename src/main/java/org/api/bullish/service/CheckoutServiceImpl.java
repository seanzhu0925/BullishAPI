package org.api.bullish.service;

import org.api.bullish.exception.ProductNotFoundException;
import org.api.bullish.exception.UserNotFoundException;
import org.api.bullish.model.OrderDTO;
import org.api.bullish.model.ProductDTO;
import org.api.bullish.model.PromocodeDTO;
import org.api.bullish.request.AddToCartRequest;
import org.api.bullish.request.CheckoutRequest;
import org.api.bullish.request.RemoveFromCartRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    // key = userId
    // value = for a particular product, how many
    public final Map<String, Map<String, ProductDTO>> shoppingCart;

    private final PromocodeServiceImpl promocodeService;

    private final ProductServiceImpl productService;

    @Autowired
    public CheckoutServiceImpl(ProductServiceImpl productService, @Lazy PromocodeServiceImpl promocodeService) {
        this.productService = productService;
        this.promocodeService = promocodeService;
        this.shoppingCart = new ConcurrentHashMap<>();
    }

    @Override
    public ProductDTO addToCart(AddToCartRequest request) {

        if (!productService.getProductDTOMap().containsKey(request.getProductName())) {
            throw new ProductNotFoundException("No product found in our inventory!");
        }

        if (productService.getProductDTOMap().get(request.getProductName()).getQuantity() < request.getQuantity()) {
            throw new ProductNotFoundException("Not enough product found in our inventory!");
        }

        // userId does not exist
        if (!shoppingCart.containsKey(request.getUserId())) {
            Map<String, ProductDTO> productDTOMap = new ConcurrentHashMap<>();
            productDTOMap.put(request.getProductName(), ProductDTO.builder()
                    .quantity(request.getQuantity())
                    .productName(request.getProductName())
                    .build());
            shoppingCart.put(request.getUserId(), productDTOMap);
            // userId exist, but product is not in current shopping cart
        } else {
            Map<String, ProductDTO> userDTOMap = shoppingCart.get(request.getUserId());
            if (!userDTOMap.containsKey(request.getProductName())) {
                shoppingCart.get(request.getUserId()).put(request.getProductName(), ProductDTO.builder()
                        .quantity(request.getQuantity())
                        .productName(request.getProductName())
                        .build());
                // userId exist, product quantity needs to be updated
            } else {
                ProductDTO productDTO = userDTOMap.get(request.getProductName());
                Integer updateQuantity = productDTO.getQuantity() + request.getQuantity();
                productDTO.setQuantity(updateQuantity);
                shoppingCart.get(request.getUserId()).replace(request.getProductName(), productDTO);
            }
        }

        updateInventory(request);

        return shoppingCart.get(request.getUserId()).get(request.getProductName());
    }

    @Override
    public void deleteFromCart(RemoveFromCartRequest request) {

        if (!shoppingCart.containsKey(request.getUserId())) {
            throw new UserNotFoundException("Current user can not perform remove product from shopping cart!");
        }

        if (!shoppingCart.get(request.getUserId()).containsKey(request.getProductName())) {
            throw new ProductNotFoundException("Product not found in shopping cart!");
        }

        if (shoppingCart.get(request.getUserId()).get(request.getProductName()).getQuantity() < request.getQuantity()) {
            throw new ProductNotFoundException("Not enough product in the shopping cart to remove!");
        }

        Map<String, ProductDTO> userDTOMap = shoppingCart.get(request.getUserId());
        ProductDTO product = userDTOMap.get(request.getProductName());
        Integer updateQuantity = product.getQuantity() - request.getQuantity();
        product.setQuantity(updateQuantity);
        shoppingCart.get(request.getUserId()).replace(request.getProductName(), product);

        updateInventory(request);
    }

    @Override
    public OrderDTO checkout(CheckoutRequest request) {
        return calculateShoppingCart(shoppingCart, request.getUserId());
    }

    private void updateInventory(AddToCartRequest request) {
        Map<String, ProductDTO> inventoryMap = productService.getProductDTOMap();

        ProductDTO productUpdate = inventoryMap.get(request.getProductName());

        productUpdate.setQuantity(productUpdate.getQuantity() - request.getQuantity());

        inventoryMap.replace(request.getProductName(), productUpdate);
    }

    private void updateInventory(RemoveFromCartRequest request) {
        Map<String, ProductDTO> inventoryMap = productService.getProductDTOMap();

        ProductDTO productUpdate = inventoryMap.get(request.getProductName());

        productUpdate.setQuantity(productUpdate.getQuantity() + request.getQuantity());

        inventoryMap.replace(request.getProductName(), productUpdate);
    }

    public OrderDTO calculateShoppingCart(Map<String, Map<String, ProductDTO>> shoppingCart, String userId) {

        if (shoppingCart == null) {
            return OrderDTO.builder().totalPrice(0.00).build();
        }

        if (!shoppingCart.containsKey(userId)) {
            throw new UserNotFoundException("Current user does not have a shopping cart with us");
        }

        double totalPrice = 0.0;
        List<ProductDTO> productDTO = null;

        for (Map.Entry<String, ProductDTO> curOrder : shoppingCart.get(userId).entrySet()) {
            totalPrice += totalPrice + (curOrder.getValue().getPrice() * curOrder.getValue().getQuantity());
            productDTO.add(curOrder.getValue());
        }

        Map<String, List<PromocodeDTO>> userPromocode = promocodeService.getUserPromoMap();

        if (userPromocode.containsKey(userId)) {
            return OrderDTO.builder()
                    .orderId(UUID.randomUUID().toString())
                    .products(productDTO)
                    .promocodes(userPromocode.get(userId))
                    .createDate(new Date())
                    .totalPrice(totalPrice)
                    .build();
        }

        return OrderDTO.builder()
                .orderId(UUID.randomUUID().toString())
                .products(productDTO)
                .createDate(new Date())
                .totalPrice(totalPrice)
                .build();


    }

    public Map<String, Map<String, ProductDTO>> getShoppingCart() {
        return shoppingCart;
    }
}
