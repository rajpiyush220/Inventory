package com.touchblankspot.inventory.portal.service;

import com.touchblankspot.inventory.portal.data.model.ProductPrice;
import com.touchblankspot.inventory.portal.data.repository.ProductPriceRepository;
import com.touchblankspot.inventory.portal.web.types.product.price.ProductPriceUpdateType;
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
import org.springframework.util.ObjectUtils;

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

  public Page<Object[]> getListData(Pageable pageable, String searchType, String searchKey) {
    return productPriceRepository.getListData(pageable, searchType, searchKey);
  }

  public List<String> getAutoCompleteSuggestions(String searchType, String searchKey) {
    return productPriceRepository.getAutoCompleteSuggestions(searchType, searchKey);
  }

  public Boolean updatePrice(ProductPriceUpdateType requestType) {
    Optional<ProductPrice> optionalProductPrice =
        productPriceRepository.findById(requestType.getId());
    if (optionalProductPrice.isPresent()) {
      ProductPrice productPrice = optionalProductPrice.get();
      productPrice.setPrice(BigDecimal.valueOf(requestType.getPrice()));
      productPrice.setDiscountPercentage(requestType.getDiscountPercentage());
      if (!ObjectUtils.isEmpty(requestType.getMaxDiscountAmount())) {
        productPrice.setMaxDiscountAmount(Long.valueOf(requestType.getMaxDiscountAmount()));
      }
      save(productPrice);
      return true;
    }
    return false;
  }

  public void deleteProduct(UUID id) {
    productPriceRepository.deleteById(id);
  }

  public Object[] getPriceViewData(UUID id) {
    return productPriceRepository.getPriceViewData(id);
  }
}
