package com.epam.esm.builder;

import java.lang.reflect.Field;

import static com.epam.esm.builder.QueryBuilderData.*;

public class SortStringValidator {

    public static boolean isValidSortData(String in, String className) {
        String classPath = PACKAGE + className + CLASS_NAME_SUFFIX;
        boolean condition = false;
        in = in.toLowerCase().trim();
        Class<?> clazz;
        try {
            clazz = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError();
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.toString();
            int lastDotIndex = fieldName.lastIndexOf('.');
            fieldName = fieldName.substring(lastDotIndex + 1);
            if (String.valueOf(in.charAt(0)).equals(MINUS)) {
                in = in.substring(1).trim();
            }
            if (fieldName.toLowerCase().equals(in)) {
                condition = true;
            }
        }
        return condition;
    }
}
