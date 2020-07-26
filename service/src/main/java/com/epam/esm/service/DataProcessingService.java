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

    public enum PageParamType {
        PAGE_SIZE, PAGE_NUMBER
    }

    public Map<String, String> toCamelCase(Map<String, String> params) {
        Map<String, String> map = params.entrySet()
                                        .stream()
                                        .collect(Collectors.toMap(e1 -> stringManager.toCamelString(e1.getKey()),
                                                                  e1 -> stringManager.toCamelString(e1.getValue())));
        return map;
    }

    public int receivePageParam(Map<String, String> params, PageParamType type) {
        int pageParam;
        params = toCamelCase(params);
        String var = type.equals(PageParamType.PAGE_NUMBER) ? PAGE_NUMBER : PAGE_SIZE;
        if (params.get(var) != null && paginationValidator.isNumber(params.get(var))) {
            try {
                pageParam = Integer.parseInt(params.get(var));
            } catch (NumberFormatException e) {
                ServiceExceptionCode exception = PAGE_NUMBER_OR_SIZE_NOT_INTEGER;
                log.error(exception.getExceptionCode() + ":" + exception.getExceptionMessage());
                throw new ServiceException(exception);
            }
        } else {
            switch (type) {
                default:
                case PAGE_NUMBER:
                    pageParam = DEFAULT_PAGE_NUMBER;
                    break;
                case PAGE_SIZE:
                    pageParam = DEFAULT_PAGE_SIZE;
                    break;
            }
        }
        paginationValidator.isPageNumberOrSize(pageParam);
        return pageParam;
    }
}
