package com.lightmock.util;

import java.util.*;

/**
 * @author Mesut Yakut
 */
public final class EndpointMatcher {

    private EndpointMatcher() {}

    public static Optional<Map<String,String>> match(String requestPath, String templatePath) {
        String[] reqParts = trimAndSplit(requestPath);
        String[] tplParts = trimAndSplit(templatePath);

        if (reqParts.length != tplParts.length) return Optional.empty();

        Map<String,String> params = new HashMap<>();
        for (int i = 0; i < tplParts.length; i++) {
            String tpl = tplParts[i];
            String req = reqParts[i];

            if (isParam(tpl)) {
                ParamSpec spec = parseParamSpec(tpl);
                if (!typeCheck(req, spec.type)) return Optional.empty();
                params.put(spec.name, req);
            } else {
                if (!tpl.equals(req)) return Optional.empty();
            }
        }
        return Optional.of(params);
    }

    private static String[] trimAndSplit(String path) {
        String p = path;
        if (p.startsWith("/")) p = p.substring(1);
        if (p.endsWith("/")) p = p.substring(0, p.length()-1);
        if (p.isEmpty()) return new String[0];
        return p.split("/");
    }

    private static boolean isParam(String part) {
        return part.startsWith("{") && part.endsWith("}");
    }

    private static class ParamSpec {
        final String name;
        final String type;
        ParamSpec(String name, String type) { this.name = name; this.type = type; }
    }

    private static ParamSpec parseParamSpec(String raw) {
        String inside = raw.substring(1, raw.length()-1).trim();
        if (inside.contains(":")) {
            String[] p = inside.split(":",2);
            return new ParamSpec(p[0].trim(), p[1].trim());
        }
        return new ParamSpec(inside, null);
    }

    private static boolean typeCheck(String value, String type) {
        if (type == null || type.isEmpty()) return true;
        if (type.equals("int")) {
            try {
                Integer.parseInt(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}