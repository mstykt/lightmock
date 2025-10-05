package com.lightmock.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.Map;

/**
 * @author Mesut Yakut
 */
public class Endpoint {

    private Request request;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<ResponseDef> response;
    private boolean strictBody = false;

    public Request getRequest() { return request; }
    public void setRequest(Request request) { this.request = request; }

    public List<ResponseDef> getResponse() { return response; }
    public void setResponse(List<ResponseDef> response) { this.response = response; }

    public boolean isStrictBody() { return strictBody; }
    public void setStrictBody(boolean strictBody) { this.strictBody = strictBody; }

    // Nested Request class
    public static class Request {
        private String method;
        private String path;
        private Map<String,Object> body;

        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }

        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }

        public Map<String,Object> getBody() { return body; }
        public void setBody(Map<String,Object> body) { this.body = body; }
    }
}