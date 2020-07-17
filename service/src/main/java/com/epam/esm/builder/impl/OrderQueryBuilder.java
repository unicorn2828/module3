package com.epam.esm.builder.impl;

import com.epam.esm.builder.QueryBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("orderQueryBuilder")
public class OrderQueryBuilder implements QueryBuilder {
    private static final String ID = "id";
    private static final String DEFAULT = "default";
    private static final String OWNER_NAME = "ownerName";
    private static final String ASCENDING = "ASC";
    private static final String DESCENDING = "DESC";
    private static final String SORT_BY = "sortBy";
    private static final String SELECT = "SELECT o FROM Order o ";

    @Override
    public String buildQuery(Map<String, String> params) {
        String sql;
        String sortBy = ID;
        String sortingOrder = ASCENDING;
        String searchBy = DEFAULT;
        String searchByParam = null;
        for (String param : params.keySet()) {
            switch (param) {
                case SORT_BY:
                    sortBy = params.get(param);
                    if (String.valueOf(sortBy.charAt(0)).equals("-")) {
                        sortBy = sortBy.substring(1);
                        sortingOrder = DESCENDING;
                    }
                    break;
                case OWNER_NAME:
                    searchBy = param;
                    searchByParam = params.get(param);
                    break;
                default:
                    break;
            }
        }
        String orderBy = "ORDER BY o. " + sortBy + " " + sortingOrder;
        switch (searchBy) {
            case DEFAULT:
                sql = SELECT + orderBy;
                break;
            default:
                sql = SELECT +
                      "WHERE o." + searchBy + " " +
                      "LIKE CONCAT('" + searchByParam + "', '%') " + orderBy;
        }
        return sql;
    }
}
