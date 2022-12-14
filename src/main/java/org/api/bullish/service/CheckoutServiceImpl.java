package org.api.bullish.service;

import org.api.bullish.exception.ProductNotFoundException;
import org.api.bullish.exception.UserNotFoundException;
import org.api.bullish.model.OrderDTO;
import org.api.bullish.model.ProductDTO;
import org.api.bullish.request.AddToCartRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    // key = userId
    // value = for a particular product, how many
    public final Map<String, Map<String, ProductDTO>> shoppingCart;

    private final ProductServiceImpl productService;

    @Autowired
    public CheckoutServiceImpl(ProductServiceImpl productService) {
        this.productService = productService;
        this.shoppingCart = new ConcurrentHashMap<>();
    }

    @Override
    public ProductDTO addToCart(AddToCartRequest request) {

        if (Objects.isNull(request.getUserId())) {
            throw new UserNotFoundException("No userId found!");
        }

        if (!productService.getProductDTOMap().containsKey(request.getProductName())) {
            throw new ProductNotFoundException("No product found in our inventory!");
        }

        if (productService.getProductDTOMap().get(request.getProductName()).getQuantity() < request.getQuantity()) {
            throw new ProductNotFoundException("Not enough product found in our inventory!");
        }

        ProductDTO product = productService.getProductDTOMap().get(request.getProductName());
        // userId does not exist
        if (!shoppingCart.containsKey(request.getUserId())) {
            Map<String, ProductDTO> productDTOMap = new ConcurrentHashMap<>();
            productDTOMap.put(request.getProductName(), ProductDTO.builder()
                    .quantity(request.getQuantity())
                    .productName(request.getProductName())
                    .price(product.getPrice())
                    .productType(product.getProductType())
                    .productId(product.getProductId())
                    .description(product.getDescription())
                    .createDate(product.getCreateDate())
                    .lastModifiedDate(product.getLastModifiedDate())
                    .build());
            shoppingCart.put(request.getUserId(), productDTOMap);
            // userId exist, but product is not in current shopping cart
        } else {
            Map<String, ProductDTO> userDTOMap = shoppingCart.get(request.getUserId());
            if (!userDTOMap.containsKey(request.getProductName())) {
                shoppingCart.get(request.getUserId()).put(request.getProductName(), ProductDTO.builder()
                        .quantity(request.getQuantity())
                        .productName(request.getProductName())
                        .price(product.getPrice())
                        .productType(product.getProductType())
                        .productId(product.getProductId())
                        .description(product.getDescription())
                        .createDate(product.getCreateDate())
                        .lastModifiedDate(product.getLastModifiedDate())
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
    public void deleteFromCart(final String userId, final String productName, final Integer quantity) {

        if (!shoppingCart.containsKey(userId)) {
            throw new UserNotFoundException("Current user can not perform remove product from shopping cart!");
        }

        if (!shoppingCart.get(userId).containsKey(productName)) {
            throw new ProductNotFoundException("Product not found in shopping cart!");
        }

        if (shoppingCart.get(userId).get(productName).getQuantity() < quantity) {
            throw new ProductNotFoundException("Not enough product in the shopping cart to remove!");
        }

        Map<String, ProductDTO> userDTOMap = shoppingCart.get(userId);
        ProductDTO product = userDTOMap.get(productName);
        Integer updateQuantity = product.getQuantity() - quantity;
        product.setQuantity(updateQuantity);
        shoppingCart.get(userId).replace(productName, product);

        updateInventory(productName, quantity);
    }

    @Override
    public OrderDTO checkout(final String userId) {
        return calculateShoppingCart(shoppingCart, userId);
    }

    private void updateInventory(AddToCartRequest request) {
        Map<String, ProductDTO> inventoryMap = productService.getProductDTOMap();

        ProductDTO productUpdate = inventoryMap.get(request.getProductName());

        productUpdate.setQuantity(productUpdate.getQuantity() - request.getQuantity());

        inventoryMap.replace(request.getProductName(), productUpdate);
    }

    private void updateInventory(final String productName, final Integer quantity) {
        Map<String, ProductDTO> inventoryMap = productService.getProductDTOMap();

        ProductDTO productUpdate = inventoryMap.get(productName);

        productUpdate.setQuantity(productUpdate.getQuantity() + quantity);

        inventoryMap.replace(productName, productUpdate);
    }

    public OrderDTO calculateShoppingCart(Map<String, Map<String, ProductDTO>> shoppingCart, String userId) {

        if (shoppingCart == null) {
            return OrderDTO.builder().totalPrice(0.00).build();
        }

        if (!shoppingCart.containsKey(userId)) {
            throw new UserNotFoundException("Current user does not have a shopping cart with us");
        }

        double totalPrice = 0.0;
        List<ProductDTO> productDTO = new ArrayList<>();

        OrderDTO order = OrderDTO.builder()
                .orderId(UUID.randomUUID().toString())
                .userId(userId)
                .createDate(new Date())
                .build();


        for (Map.Entry<String, ProductDTO> curOrder : shoppingCart.get(userId).entrySet()) {
            productDTO.add(curOrder.getValue());
            // apply promocode here if any summing up to total price
            if (curOrder.getValue().getTotalPriceAfterDiscount() != null) {
                totalPrice += curOrder.getValue().getTotalPriceAfterDiscount();
            } else {
                totalPrice += (curOrder.getValue().getPrice() * curOrder.getValue().getQuantity());
            }
            order.setTotalPrice(totalPrice);
            order.setProducts(productDTO);
        }

        return order;
    }

    public Map<String, Map<String, ProductDTO>> getShoppingCart() {
        return shoppingCart;
    }
}
