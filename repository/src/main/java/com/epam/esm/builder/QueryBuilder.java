package com.epam.esm.builder;

import com.epam.esm.model.Certificate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static com.epam.esm.builder.QueryBuilderData.*;

@Component
public class QueryBuilder {
    public String buildQuery(Map<String, String> params, String tableName) {
        String sortingOrder = ASCENDING;
        String searchBy = All;
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
        return buildQueryString(tableName, searchBy, searchByParam, sortBy, sortingOrder);
    }

    private String buildQueryString(String tableName,
                                    String searchBy,
                                    String searchByParam,
                                    String sortBy,
                                    String sortingOrder) {
        String sql;
        String select = getSelectString(tableName);
        String orderBy = getSortString(sortBy, sortingOrder, tableName);
        switch (searchBy) {
            case All:
                sql = getAllString(select, orderBy);
                break;
            case CERTIFICATE_TAG_NAME:
                sql = getCertificateByTagName(searchByParam, select, orderBy);
                break;
            default:
                sql = getDefaultString(select, searchBy, searchByParam, orderBy);
        }
        return sql;
    }

    private String getSortString(String sortBy, String sortingOrder, String tableName) {
        if(!SortStringValidator.isValidSortData(sortBy, tableName)){
            sortBy = ID;
        }
        return "ORDER BY x." + sortBy + " " + sortingOrder;
    }

    private String getSelectString(String tableName) {
        return "SELECT x FROM " + tableName + " x ";
    }

    private String getAllString(String select, String orderBy) {
        return select + orderBy;
    }

    private String getDefaultString(String select, String searchBy, String searchByParam, String orderBy) {
        return select + "WHERE x." + searchBy + " " + "LIKE CONCAT('" + searchByParam + "', '%') " + orderBy;
    }

    private String getCertificateByTagName(String searchByParam, String select, String orderBy) {
        List<String> tagNames = Arrays.asList(searchByParam.split(","));
        StringJoiner joiner = new StringJoiner(",");
        tagNames.forEach(tagName -> joiner.add("'" + tagName.trim() + "'"));
        return select + "LEFT JOIN FETCH x.tags t " + "WHERE x.id " +
               "IN (SELECT cc.id FROM Certificate cc LEFT JOIN cc.tags tt WHERE tt.tagName " +
               "IN (" + joiner.toString() + ") GROUP BY cc.id HAVING COUNT (cc.id) >= " + tagNames.size() + ") "
               + orderBy;
    }
}
