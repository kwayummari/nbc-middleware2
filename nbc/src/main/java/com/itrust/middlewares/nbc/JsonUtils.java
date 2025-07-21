package com.itrust.middlewares.nbc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {


    public static JsonNode convertToCamelCase(JsonNode node, ObjectMapper mapper) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            ObjectNode newObject = mapper.createObjectNode();

            objectNode.fields().forEachRemaining(entry -> {
                String oldKey = entry.getKey();
                String newKey = toCamelCase(oldKey);
                JsonNode value = entry.getValue();

                if (value.isObject() || value.isArray()) {
                    newObject.set(newKey, convertToCamelCase(value, mapper));
                } else {
                    newObject.set(newKey, value);
                }
            });
            return newObject;
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            ArrayNode newArray = mapper.createArrayNode();

            arrayNode.forEach(element -> {
                newArray.add(convertToCamelCase(element, mapper));
            });
            return newArray;
        }
        return node;
    }

    private static String toCamelCase(String str) {
        if (str == null || str.isEmpty()) return str;

        // Handle special case "lukuRespInf"
        if (str.equals("lukuRespInf")) return "lukuRespInfo";

        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;

        for (int i = 0; i < str.length(); i++) {
            char current = str.charAt(i);
            if (i == 0) {
                result.append(Character.toLowerCase(current));
            } else if (nextUpper) {
                result.append(Character.toUpperCase(current));
                nextUpper = false;
            } else {
                result.append(current);
                nextUpper = !Character.isLetterOrDigit(current);
            }
        }
        return result.toString();
    }
}
