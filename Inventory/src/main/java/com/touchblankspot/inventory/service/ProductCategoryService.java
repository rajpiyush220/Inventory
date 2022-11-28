package com.touchblankspot.inventory.service;

import com.touchblankspot.inventory.data.model.ProductCategory;
import com.touchblankspot.inventory.data.repository.ProductCategoryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductCategoryService {
    @NonNull
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategory save(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }

    public List<ProductCategory> findByProductSize(String productSize) {
        return productCategoryRepository.findByProductSize(productSize);
    }

    public List<ProductCategory> findByCategory(String category) {
        return productCategoryRepository.findByCategory(category);
    }

    public List<ProductCategory> findBySubCategory(String subCategory) {
        return productCategoryRepository.findBySubCategory(subCategory);
    }
}
