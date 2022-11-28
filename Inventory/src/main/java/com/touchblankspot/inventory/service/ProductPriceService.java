package com.touchblankspot.inventory.service;

import com.touchblankspot.inventory.data.repository.ProductPriceRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductPriceService {
  @NonNull private final ProductPriceRepository productPriceRepository;
}
