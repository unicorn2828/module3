package com.epam.esm.builder;

/**
 * This is the QueryBuilderData class; it contains constant data for {@link QueryBuilder} class.
 *
 * @author Vitaly Kononov
 * @version 1.0
 */
class QueryBuilderData {
    static final String CERTIFICATE_TAG_NAME = "certificateTagName";
    static final String CERTIFICATE_NAME = "certificateName";
    static final String ORDER_OWNER_NAME = "ownerName";
    static final String TAG_NAME = "tagName";
    static final String ALL = "default";
    static final String DESCENDING = " DESC";
    static final String ASCENDING = " ASC";
    static final String SORT_BY = "sortBy";
    static final String LOGIN = "login";
    static final String EMAIL = "email";
    static final String ID = "id";
    static final String MINUS = "-";
    static final String PACKAGE = "com.epam.esm.dto.";
    static final String CLASS_NAME_SUFFIX = "Dto";

    static final String SELECT_FROM = "SELECT x FROM ";
    static final String LIKE_CONCAT = " LIKE CONCAT('";
    static final String HOOKS = "', '%') ";
    static final String HOOK = ") ";
    static final String X = " x ";
    static final String COMMA = ",";
    static final String POINT = "'.'";
    static final String APOSTROPHE = "'";
    static final String ORDER_BY = "ORDER BY x.";
    static final String WHERE_X = "WHERE x.";
    static final String LEFT_JOIN_FETCH = "LEFT JOIN FETCH x.tags t WHERE x.id IN " +
                                          "(SELECT cc.id FROM Certificate cc LEFT JOIN cc.tags tt WHERE tt.tagName IN (";
    static final String HAVING_COUNT = ") GROUP BY cc.id HAVING COUNT (cc.id) >= ";

    private QueryBuilderData() {
    }
}
