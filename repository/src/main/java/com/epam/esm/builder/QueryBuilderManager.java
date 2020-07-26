package com.epam.esm.builder;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static com.epam.esm.builder.QueryBuilderData.*;

/**
 * This is the QueryBuilderManager class; it is a helper class for {@link QueryBuilder} class.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
@Component
class QueryBuilderManager {

    String receiveQueryString(String tableName,
                              String searchBy,
                              String searchByParam,
                              String sortBy,
                              String sortingOrder) {
        String sql;
        String select = getSelectString(tableName);
        String orderBy = getSortString(sortBy, sortingOrder, tableName);
        switch (searchBy) {
            case ALL:
                sql = getAllQuery(select, orderBy);
                break;
            case CERTIFICATE_TAG_NAME:
                sql = getCertificateByTagNameQuery(searchByParam, select, orderBy);
                break;
            default:
                sql = getDefaultQuery(select, searchBy, searchByParam, orderBy);
        }
        return sql;
    }

    private String getAllQuery(String select, String orderBy) {
        return select + orderBy;
    }

    private String getDefaultQuery(String select, String searchBy, String searchByParam, String orderBy) {
        StringBuilder query = new StringBuilder();
        query.append(select);
        query.append(WHERE_X);
        query.append(searchBy);
        query.append(LIKE_CONCAT);
        query.append(searchByParam);
        query.append(HOOKS);
        query.append(orderBy);
        return query.toString();
    }

    private String getCertificateByTagNameQuery(String searchByParam, String select, String orderBy) {
        List<String> tagNames = Arrays.asList(searchByParam.split(COMMA));
        StringJoiner joiner = new StringJoiner(COMMA);
        tagNames.forEach(tagName -> joiner.add(APOSTROPHE + tagName.trim() + APOSTROPHE));
        StringBuilder query = new StringBuilder();
        query.append(select);
        query.append(LEFT_JOIN_FETCH);
        query.append(joiner.toString());
        query.append(HAVING_COUNT);
        query.append(tagNames.size());
        query.append(HOOK);
        query.append(orderBy);
        return query.toString();
    }

    private String getSortString(String sortBy, String sortingOrder, String tableName) {
        if (!SortStringValidator.isValidSortData(sortBy, tableName)) {
            sortBy = ID;
        }
        StringBuilder query = new StringBuilder();
        query.append(ORDER_BY);
        query.append(sortBy);
        query.append(sortingOrder);
        return query.toString();
    }

    private String getSelectString(String tableName) {
        StringBuilder query = new StringBuilder();
        query.append(SELECT_FROM);
        query.append(tableName);
        query.append(X);
        return query.toString();
    }
}
