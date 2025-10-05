package com.lightmock.model;

import java.util.Map;

/**
 * @author Mesut Yakut
 */
public class ResponseDef {
    private int status;
    private Object body; // String or List<String>
    private Map<String, String> headers;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}