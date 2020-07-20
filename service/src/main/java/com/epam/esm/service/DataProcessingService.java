package com.epam.esm.service;

import com.epam.esm.manager.CamelStringManager;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.validation.PaginationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ServiceExceptionCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataProcessingService {
    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_SIZE = "pageSize";
    private final PaginationValidator paginationValidator;
    private final CamelStringManager stringManager;

    public Map<String, String> toCamelCase(Map<String, String> params) {
        Map<String, String> map = params.entrySet()
                                        .stream()
                                        .collect(Collectors.toMap(e1 -> stringManager.toCamelString(e1.getKey()),
                                                                  e1 -> stringManager.toCamelString(e1.getValue())));
        return map;
    }

    public int receivePageNumber(Map<String, String> params) {
        int pageNumber;
        params = toCamelCase(params);
        if (params.get(PAGE_NUMBER) != null && paginationValidator.isNumber(params.get(PAGE_NUMBER))) {
            try {
                pageNumber = Integer.parseInt(params.get(PAGE_NUMBER));
            } catch (NumberFormatException e) {
                ServiceExceptionCode exception = PAGE_NUMBER_NOT_INTEGER;
                log.error(exception.getExceptionCode() + ":" + exception.getExceptionMessage());
                throw new ServiceException(exception);
            }
        } else {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        paginationValidator.isPageNumberOrSize(pageNumber);
        return pageNumber;
    }

    public int receivePageSize(Map<String, String> params) {
        int pageSize;
        params = toCamelCase(params);
        if (params.get(PAGE_SIZE) != null && paginationValidator.isNumber(params.get(PAGE_SIZE))) {
            try {
                pageSize = Integer.parseInt(params.get(PAGE_SIZE));
            } catch (NumberFormatException e) {
                ServiceExceptionCode exception = NUMBER_PAGE_SIZE_NOT_INTEGER;
                log.error(exception.getExceptionCode() + ":" + exception.getExceptionMessage());
                throw new ServiceException(exception);
            }
        } else {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        paginationValidator.isPageNumberOrSize(pageSize);
        return pageSize;
    }
}
