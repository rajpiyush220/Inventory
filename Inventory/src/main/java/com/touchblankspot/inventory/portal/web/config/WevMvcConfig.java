package com.touchblankspot.inventory.portal.web.config;

import com.touchblankspot.inventory.portal.web.annotations.ProductController;
import com.touchblankspot.inventory.portal.web.annotations.SalesController;
import com.touchblankspot.inventory.portal.web.annotations.StockController;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
@RequiredArgsConstructor
public class WevMvcConfig implements WebMvcConfigurer {

  @NonNull private final LocaleChangeInterceptor localeChangeInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor);
  }

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.addPathPrefix(
        "/product", HandlerTypePredicate.forAnnotation(ProductController.class));
    configurer.addPathPrefix("/sales", HandlerTypePredicate.forAnnotation(SalesController.class));
    configurer.addPathPrefix("/stock", HandlerTypePredicate.forAnnotation(StockController.class));
  }
}
