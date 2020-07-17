package com.epam.esm.builder.impl;

import com.epam.esm.builder.QueryBuilder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Component("certificateQueryBuilder")
public class CertificateQueryBuilder implements QueryBuilder {
    private static final String ID = "id";
    private static final String DEFAULT = "default";
    private static final String DESCRIPTION = "description";
    private static final String TAG_NAME = "tagName";
    private static final String CERTIFICATE_NAME = "certificateName";
    private static final String ASCENDING = "ASC";
    private static final String DESCENDING = "DESC";
    private static final String SORT_BY = "sortBy";

    @Override
    public String buildQuery(Map<String, String> params) {
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
                case TAG_NAME:
                case CERTIFICATE_NAME:
                case DESCRIPTION:
                    searchBy = param;
                    searchByParam = params.get(param);
                    break;
                default:
                    break;
            }
        }
        String orderBy = "ORDER BY c." + sortBy + " " + sortingOrder;
        return buildQueryString(searchBy, searchByParam, orderBy);
    }

    private String buildQueryString(String searchBy, String searchByParam, String orderBy) {
        String sql;
        switch (searchBy) {
            case DEFAULT:
                sql = "SELECT c " +
                      "FROM Certificate c " + orderBy;
                break;
            case TAG_NAME:
                List<String> tagNames = Arrays.asList(searchByParam.split(","));
                StringJoiner joiner = new StringJoiner(",");
                tagNames.forEach(tagName -> joiner.add("'" + tagName.trim() + "'"));
                sql = "SELECT c " +
                      "FROM Certificate c " +
                      "LEFT JOIN FETCH c.tags t " +
                      "WHERE c.id " +
                      "IN (SELECT cc.id FROM Certificate cc LEFT JOIN cc.tags tt WHERE tt.tagName " +
                      "IN (" + joiner.toString() + ") GROUP BY cc.id HAVING COUNT (cc.id) >= " + tagNames.size() + ") "
                      + orderBy;

                break;
            default:
                sql = "SELECT c " +
                      "FROM Certificate c " +
                      "WHERE c." + searchBy + " " +
                      "LIKE CONCAT('" + searchByParam.trim() + "', '%') " + orderBy;
        }
        return sql;
    }
}
