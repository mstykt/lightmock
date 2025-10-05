package com.lightmock.core;

import com.lightmock.model.Endpoint;
import com.lightmock.model.ResponseDef;
import com.lightmock.util.EndpointMatcher;
import com.lightmock.util.RequestValidator;
import com.lightmock.util.ResponseSelector;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Mesut Yakut
 */
public class RequestHandler implements HttpHandler {

    private final List<Endpoint> endpoints;
    private final Random random = new Random();

    public RequestHandler(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String reqMethod = exchange.getRequestMethod().toUpperCase(Locale.ROOT);
        String reqPath = exchange.getRequestURI().getPath();

        Endpoint matched = null;
        Map<String, String> pathParams = null;

        for (Endpoint ep : endpoints) {
            if (!ep.getRequest().getMethod().equalsIgnoreCase(reqMethod)) continue;
            Optional<Map<String, String>> params = EndpointMatcher.match(reqPath, ep.getRequest().getPath());
            if (params.isPresent()) {
                matched = ep;
                pathParams = params.get();
                break;
            }
        }

        if (matched == null) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        String requestBody = readAll(exchange.getRequestBody());

        try {
            Map<String, Object> expectedBody = matched.getRequest().getBody();
            if (expectedBody != null) {
                boolean ok = RequestValidator.validate(requestBody, expectedBody, matched.isStrictBody());
                if (!ok) {
                    exchange.sendResponseHeaders(400, -1);
                    return;
                }
            }
        } catch (Exception e) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }

        // Random response
        List<ResponseDef> responses = matched.getResponse();
        ResponseDef respDef = responses.get(random.nextInt(responses.size()));
        String responseBody = ResponseSelector.select(respDef.getBody());

        if (pathParams != null) {
            for (Map.Entry<String, String> entry : pathParams.entrySet()) {
                responseBody = responseBody.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
        }

        String contentType = Optional.ofNullable(respDef.getHeaders())
                .map(h -> h.get("Content-Type"))
                .orElse("application/json");

        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", contentType);


        // Handle 204/205 status (no content)
        if (respDef.getStatus() == 204 || respDef.getStatus() == 205) {
            exchange.sendResponseHeaders(respDef.getStatus(), -1);
        } else {
            exchange.sendResponseHeaders(respDef.getStatus(), bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    private static String readAll(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] tmp = new byte[1024];
        int n;
        while ((n = is.read(tmp)) != -1) {
            buffer.write(tmp, 0, n);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}