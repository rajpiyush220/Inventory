package com.touchblankspot.inventory.portal.service;

import com.touchblankspot.inventory.portal.data.model.StockAudit;
import com.touchblankspot.inventory.portal.data.repository.StockAuditRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class StockAuditService {
    @NonNull
    private final StockAuditRepository stockAuditRepository;

    public StockAudit save(StockAudit stockAudit) {
        return stockAuditRepository.save(stockAudit);
    }

    public Page<Object[]> getListData(Pageable pageable, String searchType, String searchKey) {
        return stockAuditRepository.getListData(pageable, searchType, searchKey);
    }

    public List<String> getAutoCompleteSuggestions(String searchType, String searchKey) {
        return stockAuditRepository.getAutoCompleteSuggestions(searchType, searchKey);
    }
}
