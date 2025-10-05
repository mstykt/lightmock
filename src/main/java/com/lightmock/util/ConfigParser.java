package com.lightmock.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.lightmock.model.Endpoint;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mesut Yakut
 */
public class ConfigParser {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Load endpoints from YAML or JSON file
     * @param file the config file (YAML or JSON)
     * @return List of Endpoint objects
     * @throws IOException in case of parsing errors
     */
    public static List<Endpoint> load(File file) throws IOException {
        if (!file.exists()) {
            throw new IllegalArgumentException("file does not exist: " + file);
        }
        String name = file.getName().toLowerCase();
        Endpoint[] endpoints;
        if (name.endsWith(".yaml") || name.endsWith(".yml")) {
            endpoints = YAML_MAPPER.readValue(file, Endpoint[].class);
        } else if (name.endsWith(".json")) {
            endpoints = JSON_MAPPER.readValue(file, Endpoint[].class);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + file.getName());
        }
        return Arrays.asList(endpoints);
    }
}
