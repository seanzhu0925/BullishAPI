package org.api.bullish.service;

import org.api.bullish.model.PromocodeDTO;
import org.api.bullish.request.AddNewPromocodeRequest;
import org.api.bullish.request.ApplyPromocodeRequest;

public interface PromocodeService {

    public PromocodeDTO createPromocode(AddNewPromocodeRequest request);

    public Double applyPromocode(ApplyPromocodeRequest request);
}
