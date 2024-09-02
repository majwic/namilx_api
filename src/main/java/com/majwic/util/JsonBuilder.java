package com.majwic.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonBuilder {
    private final Map<String, Object> jsonMap = new LinkedHashMap<>();

    public JsonBuilder add(String key, Object value) {
        jsonMap.put(key, value);
        return this;
    }

    public JsonBuilder add(String key, JsonBuilder nestedBuilder) {
        jsonMap.put(key, nestedBuilder.jsonMap);
        return this;
    }

    public String build() {
        return buildJson(jsonMap);
    }

    private String buildJson(Object object) {
        StringBuilder json = new StringBuilder();

        if (object instanceof Map) {
            json.append("{");
            ((Map<?, ?>) object).forEach((key, value) -> {
                json.append("\"").append(key).append("\":").append(buildJson(value)).append(",");
            });
            if (json.charAt(json.length() - 1) == ',') {
                json.deleteCharAt(json.length() - 1);
            }
            json.append("}");
        } else if (object instanceof List) {
            json.append("[");
            ((List<?>) object).forEach(item -> json.append(buildJson(item)).append(","));
            if (json.charAt(json.length() - 1) == ',') {
                json.deleteCharAt(json.length() - 1);
            }
            json.append("]");
        } else if (object instanceof String) {
            json.append("\"").append(object).append("\"");
        } else {
            json.append(object);
        }

        return json.toString();
    }

    @Override
    public String toString() {
        return buildJson(jsonMap);
    }

    public static void main(String[] args) {
        JsonBuilder builder = new JsonBuilder();
        builder
            .add("id", 6)
            .add("content", "Example content")
            .add("likes", 10)
            .add("dislikes", 2)
            .add("tags", List.of("tag1", "tag2", "tag3"))
            .add("authorId", 2)
            .add("profile", new JsonBuilder()
                .add("id", 2)
                .add("email", "mwichman@gmail.com")
                .add("displayName", "Mason")
            );

        String json = builder.build();
        System.out.println(json);
    }
}
