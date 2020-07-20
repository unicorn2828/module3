package com.epam.esm.service.impl;

import com.epam.esm.validation.PaginationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataProcessingService {
    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_SIZE = "pageSize";
    private final PaginationValidator paginationValidator;

    public Map<String, String> toLowerCase(Map<String, String> params) {
        Map<String, String> map = params.entrySet()
                                        .stream()
                                        .collect(Collectors.toMap(e1 -> e1.getKey(),
                                                                  e1 -> e1.getValue().toLowerCase()));
        return map;
    }

    public int receivePageNumber(Map<String, String> params) {
        int pageNumber = params.get(PAGE_NUMBER) != null && paginationValidator.isNumber(params.get(PAGE_NUMBER)) ?
                         Integer.parseInt(params.get(PAGE_NUMBER)) : DEFAULT_PAGE_NUMBER;
        paginationValidator.isPageNumberOrSize(pageNumber);
        return pageNumber;
    }

    public int receivePageSize(Map<String, String> params) {
        int pageSize = params.get(PAGE_SIZE) != null && paginationValidator.isNumber(params.get(PAGE_SIZE)) ?
                       Integer.parseInt(params.get(PAGE_SIZE)) : DEFAULT_PAGE_SIZE;
        paginationValidator.isPageNumberOrSize(pageSize);
        return pageSize;
    }
}
