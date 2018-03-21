package com.company.controllers;

public class Utils {

    private Utils() { }

    public static String buildResponse(String message) {
        return String.format("{ status: \"OK\", result: \"%s\"}", message);
    }

    public static String buildErrorResponse(String message) {
        return String.format("{ status: \"error\", message: \"%s\" }", message);
    }
}
