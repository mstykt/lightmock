package com.lightmock.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

/**
 * @author Mesut Yakut
 */
public class RequestValidator {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Validates request body against expected body.
     * If strictBody is false, any body is accepted.
     *
     * @param actualBody JSON string from incoming request
     * @param expectedBody Map of expected key-value pairs
     * @param strictBody whether strict validation is applied
     * @return true if request passes validation
     */
    public static boolean validate(String actualBody, Map<String,Object> expectedBody, boolean strictBody) {
        if (!strictBody) {
            // Ignore body validation
            return true;
        }

        if (expectedBody == null || expectedBody.isEmpty()) {
            return true; // nothing to validate
        }

        try {
            Map<String,Object> actualMap = mapper.readValue(
                    actualBody == null || actualBody.isEmpty() ? "{}" : actualBody,
                    Map.class
            );

            // strict validation: all expected keys must exist and match
            for (Map.Entry<String,Object> entry : expectedBody.entrySet()) {
                if (!actualMap.containsKey(entry.getKey())) return false;
                Object actualVal = actualMap.get(entry.getKey());
                if (!entry.getValue().equals(actualVal)) return false;
            }

            // strictBody: extra keys allowed (if you want exact match, add extra check)
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}