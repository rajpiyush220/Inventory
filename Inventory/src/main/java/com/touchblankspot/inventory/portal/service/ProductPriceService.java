package com.touchblankspot.inventory.portal.service;

import com.touchblankspot.inventory.portal.data.model.ProductPrice;
import com.touchblankspot.inventory.portal.data.repository.ProductPriceRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductPriceService {

  @NonNull private final ProductPriceRepository productPriceRepository;

  public ProductPrice save(ProductPrice productPrice) {
    return productPriceRepository.save(productPrice);
  }

  public Boolean isUniqueProductPrice(UUID productId, String productSize) {
    return productPriceRepository.findByProductIdAndProductSize(productId, productSize).isPresent();
  }

  public List<String> getProductSize(UUID productId) {
    return productPriceRepository.findByProductId(productId).stream()
        .map(ProductPrice::getProductSize)
        .sorted()
        .toList();
  }

  public BigDecimal getProductPrice(UUID productId, String productSize) {
    Optional<ProductPrice> optionalProductPrice =
        productPriceRepository.findByProductIdAndProductSize(productId, productSize);
    return optionalProductPrice.isPresent()
        ? optionalProductPrice.get().getPrice()
        : BigDecimal.ZERO;
  }

  public Optional<ProductPrice> findByProductAndSize(UUID productId, String productSize) {
    return productPriceRepository.findByProductIdAndProductSize(productId, productSize);
  }

  public Page<Object[]> getListData(Pageable pageable) {
    return productPriceRepository.getListData(pageable);
  }

  public void deleteProduct(UUID id) {
    productPriceRepository.deleteById(id);
  }
}
