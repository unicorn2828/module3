package com.epam.esm.builder;

import com.epam.esm.model.Certificate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.epam.esm.builder.QueryBuilderData.*;

/**
 * This is the QueryBuilder class; it builds query string by input params.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class QueryBuilder {
    private final QueryBuilderManager queryBuilderManager;

    public String buildQuery(Map<String, String> params, String tableName) {
        String sortingOrder = ASCENDING;
        String searchBy = ALL;
        String sortBy = ID;
        String searchByParam = null;
        for (String param : params.keySet()) {
            switch (param) {
                case SORT_BY:
                    sortBy = params.get(param);
                    if (String.valueOf(sortBy.charAt(0)).equals(MINUS)) {
                        sortBy = sortBy.substring(1);
                        sortingOrder = DESCENDING;
                    }
                    break;
                case EMAIL:
                case LOGIN:
                case TAG_NAME:
                case ORDER_OWNER_NAME:
                case CERTIFICATE_NAME:
                    searchBy = param;
                    searchByParam = params.get(param);
                    if (searchBy.equals(TAG_NAME) && tableName.equals(Certificate.class.getSimpleName())) {
                        searchBy = CERTIFICATE_TAG_NAME;
                    }
                    break;
            }
        }
        return queryBuilderManager.receiveQueryString(tableName, searchBy, searchByParam, sortBy, sortingOrder);
    }
}
