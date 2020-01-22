package com.github.wx.ccs.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

/**
 * The JSON class contains methods for parsing JavaScript Object Notation (JSON) and converting values to JSON
 * This util based on Jackson
 */
@SuppressWarnings("all")
public class JSON {

    private static Logger log = LoggerFactory.getLogger(JSON.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {

        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    /**
     * <p> The JSON.stringify() method converts a JavaScript object or value to a JSON string </p>
     * @param target The value to convert to a JSON string.
     * @return String
     * @author yangyu
     */
    public static <T> String stringify(T target) {
        return JSON.stringify(target, false);
    }

    /**
     * <p> The JSON.stringify() method converts a JavaScript object or value to a JSON string </p>
     * @param target The value to convert to a JSON string.
     * @param pretty Serialize objects to pretty string.
     * @author yangyu
     */
    public static <T> String stringify(T target, boolean pretty) {
        if (target == null) return null;
        try {
            if (target instanceof String) return (String) target;
            return pretty ? objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(target) : objectMapper.writeValueAsString(target);
        } catch (JsonProcessingException e) {
            log.warn("json stringify failed, target: {}, error information: {}", target,  e);
        }
        return null;
    }

    /**
     * <p> The JSON.parse() method parses a JSON string, constructing the Object</p>
     * @param json The string to parse as JSON. See the JSON object for a description of JSON syntax.
     * @param cls Target object class.
     * @author yangyu
     */
    public static <T> T parse(String json, Class<T> cls) {
        if (Util.isEmpty(json)) return null;
        if (Util.isEmpty(cls)) return null;
        if (cls.equals(String.class)) return (T) json;
        try {
            return objectMapper.readValue(json, cls);
        } catch (JsonProcessingException e) {
            log.warn("parse json {} to {} failed, error information: {}", json, cls, e);
        }
        return null;
    }

    /**
     * <p> The JSON.parse() method parses a JSON string, constructing the Object</p>
     *
     * <code> JSON.parse(json, new TypeReference<List<User>>() {}); </code>
     *
     * @param json The string to parse as JSON. See the JSON object for a description of JSON syntax.
     * @param typeReference Description the target object full generics type information.
     * @author yangyu
     */
    public static <T> T parse(String json, TypeReference<T> typeReference) {
        if (Util.isEmpty(json)) return null;
        if (Util.isEmpty(typeReference)) return null;
        Type type = typeReference.getType();
        if (type.equals(String.class)) return (T) json;
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.warn("parse json {} to {} failed, error information: {}", json, typeReference, e);
        }
        return null;
    }

}
