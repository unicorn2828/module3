package com.epam.esm.builder;

import java.util.Map;

@FunctionalInterface
public interface QueryBuilder {
    String buildQuery(Map<String, String> params);
}
