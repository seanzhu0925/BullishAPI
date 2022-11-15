package org.api.bullish.service;

import org.api.bullish.exception.DuplicatePromocodeException;
import org.api.bullish.exception.PromocodeMaxUsageReachedException;
import org.api.bullish.exception.PromocodeNotFoundException;
import org.api.bullish.exception.UserNotFoundException;
import org.api.bullish.model.ProductDTO;
import org.api.bullish.model.PromocodeDTO;
import org.api.bullish.request.AddNewPromocodeRequest;
import org.api.bullish.request.ApplyPromocodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.api.bullish.model.PromocodeType.HALF_PRICE;

@Service
public class PromocodeServiceImpl implements PromocodeService {

    // In memory store the promocode inventory info
    private final Map<String, PromocodeDTO> promocodeDTOMap;

    private final Map<String, List<PromocodeDTO>> userPromoMap;

    private final CheckoutServiceImpl checkoutService;

    @Autowired
    public PromocodeServiceImpl(CheckoutServiceImpl checkoutService) {
        this.promocodeDTOMap = new ConcurrentHashMap<>();
        this.userPromoMap = new ConcurrentHashMap<>();
        this.checkoutService = checkoutService;
    }

    @Override
    public PromocodeDTO createPromocode(AddNewPromocodeRequest request) {
        if (promocodeDTOMap.containsKey(request.getPromocodeName())) {
            throw new DuplicatePromocodeException("Duplicate Promocode has found in our DB");
        }

        return generatePromocode(request);
    }

    @Override
    public Double applyPromocode(ApplyPromocodeRequest request) {

        Map<String, Map<String, ProductDTO>> userShoppingCart = checkoutService.getShoppingCart();

        if (!userShoppingCart.containsKey(request.getUserId())) {
            throw new UserNotFoundException("Current user does not already have a shopping cart created yet!");
        }

        if (!promocodeDTOMap.containsKey(request.getPromocodeName())) {
            throw new PromocodeNotFoundException("Promocode not found in our DB");
        }

        if (promocodeDTOMap.get(request.getPromocodeName()).getTotalUsedTime()
                .equals(promocodeDTOMap.get(request.getPromocodeName()).getMaxUseTime())) {
            throw new PromocodeMaxUsageReachedException("Promocode can not be used anymore");
        }

        if (promocodeDTOMap.get(request.getPromocodeName()).getPromocodeType() == HALF_PRICE) {
            return calculateTotalAfterPromocodeApplied(userShoppingCart, request, promocodeDTOMap);
        }

        return calculateTotalWithoutDiscount(userShoppingCart, request.getUserId());
    }

    private Double calculateTotalWithoutDiscount(Map<String, Map<String, ProductDTO>> userShoppingCart, String userId) {
        double totalPrice = 0.0;

        for (Map.Entry<String, ProductDTO> curOrder : userShoppingCart.get(userId).entrySet()) {
            totalPrice += totalPrice + (curOrder.getValue().getPrice() * curOrder.getValue().getQuantity());
        }

        return totalPrice;
    }

    private Double calculateTotalAfterPromocodeApplied(Map<String, Map<String, ProductDTO>> userShoppingCart, ApplyPromocodeRequest request, Map<String, PromocodeDTO> promocodeDTOMap) {
        double totalPrice = 0.0;
        List<PromocodeDTO> promocode = new ArrayList<>();

        for (Map.Entry<String, ProductDTO> curOrder : userShoppingCart.get(request.getUserId()).entrySet()) {
            if (curOrder.getValue().getQuantity() > 1) {
                promocode.add(promocodeDTOMap.get(request.getPromocodeName()));
                userPromoMap.put(request.getUserId(), promocode);
                int discountCount = curOrder.getValue().getQuantity() / 2 / 2;
                int totalProduct = curOrder.getValue().getQuantity() - discountCount;
                totalPrice += totalPrice + (curOrder.getValue().getPrice() * totalProduct);
            }
        }

        return totalPrice;
    }

    private PromocodeDTO generatePromocode(AddNewPromocodeRequest request) {
        UUID uuid = UUID.randomUUID();

        return PromocodeDTO.builder()
                .promocodeId(uuid.toString())
                .promocodeName(request.getPromocodeName())
                .promocodeType(request.getPromocodeType())
                .createDate(new Date())
                .build();
    }

    public Map<String, PromocodeDTO> getPromocodeDTOMap() {
        return promocodeDTOMap;
    }

    public Map<String, List<PromocodeDTO>> getUserPromoMap() {
        return userPromoMap;
    }
}
