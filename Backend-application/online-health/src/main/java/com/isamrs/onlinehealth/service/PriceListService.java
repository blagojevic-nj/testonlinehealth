package com.isamrs.onlinehealth.service;

import com.isamrs.onlinehealth.model.PriceList;
import com.isamrs.onlinehealth.repository.PriceListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceListService {

    @Autowired
    private PriceListRepository priceListRepository;

    public PriceList save(PriceList priceList) { return priceListRepository.save(priceList);}
}
