package com.touchblankspot.inventory.portal.web.controller;

import com.touchblankspot.inventory.portal.web.types.SelectType;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;

public abstract class BaseController {

    private static List<SelectType> pageSizeSelectTypes;

    @Value("${application.pagination.pageSizeList}")
    public List<Integer> pageSizeList;

    protected List<SelectType> getPageSizeSelectTypes() {
        if (pageSizeSelectTypes == null) {
            pageSizeSelectTypes =
                    pageSizeList.stream()
                            .map(size -> new SelectType(size.toString(), size.toString()))
                            .toList();
        }
        return pageSizeSelectTypes;
    }

    protected void setModelAttributes(Model model, Map<String, Object> keyValues) {
        keyValues.entrySet().stream().forEach(entry -> model.addAttribute(entry.getKey(), entry.getValue()));
    }

    protected Integer getPageSize(Integer pageSize) {
        return pageSize > 0 ? pageSize : pageSizeList.get(0);
    }
}
