package com.epam.esm.exception.data;

public class ExceptionMessage {
    public static final String ERROR_000000 = "no error";
    public static final String ERROR_000001 = "tag is null";
    public static final String ERROR_000002 = "tag id less than 1";
    public static final String ERROR_000003 = "tag name is null";
    public static final String ERROR_000004 = "tag name is empty";
    public static final String ERROR_000005 = "tag name less than 3";
    public static final String ERROR_000006 = "tag name more than 30";
    public static final String ERROR_000008 = "tag with this name does not exist";
    public static final String ERROR_000009 = "tag id is null";

    public static final String ERROR_000010 = "certificate is null";
    public static final String ERROR_000020 = "certificate id is null";
    public static final String ERROR_000021 = "certificate id less than 1";
    public static final String ERROR_000030 = "certificate name is null";
    public static final String ERROR_000031 = "certificate name is empty";
    public static final String ERROR_000032 = "certificate name more than 30";
    public static final String ERROR_000033 = "certificate name less than 3";
    public static final String ERROR_000034 = "certificate with this name does not exist";
    public static final String ERROR_000035 = "certificate with this ID does not exist";
    public static final String ERROR_000036 = "certificate by this param does not exist";
    public static final String ERROR_000037 = "certificate ordered, can't be removed";

    public static final String ERROR_000040 = "certificate description is null";
    public static final String ERROR_000041 = "certificate description less than 3";
    public static final String ERROR_000042 = "certificate description more than 500";
    public static final String ERROR_000050 = "certificate price less than 0";
    public static final String ERROR_000051 = "certificate price more than 100";
    public static final String ERROR_000052 = "certificate price is null";
    public static final String ERROR_000060 = "certificate date of creation is null";
    public static final String ERROR_000061 = "certificate date of creation after today";
    public static final String ERROR_000062 = "certificate date of modification is null";
    public static final String ERROR_000063 = "certificate date of modification early than today";
    public static final String ERROR_000070 = "certificate duration less than 1";
    public static final String ERROR_000071 = "certificate duration more than 365";
    public static final String ERROR_000072 = "certificate duration is null";

    public static final String ERROR_000100 = "unknown parameter";
    public static final String ERROR_000101 = "tag with this ID does not exist";
    public static final String ERROR_000102 = "page number or size less than 1";
    public static final String ERROR_000103 = "number of page must be an integer";
    public static final String ERROR_000104 = "page size number must be an integer";
    public static final String ERROR_000110 = "tag with the same name already exists";
    public static final String ERROR_000120 = "order with this ID does not exist";
    public static final String ERROR_000140 = "expired or invalid JWT token";
    public static final String ERROR_000150 = "user password and confirming password aren't equal";
    public static final String ERROR_000151 = "user with this name does not exist";
    public static final String ERROR_000152 = "user with the same name already exists";
    public static final String ERROR_000153 = "invalid login/password supplied";
    public static final String ERROR_000154 = "user with this ID does not exist";

    public static final String ERROR_000170 = "access to the resource is forbidden, action is not allowed";

    private ExceptionMessage() {
    }
}
