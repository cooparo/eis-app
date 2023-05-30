package it.unipd.dei.eis.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class Marshalling {
    private static ObjectMapper getMapper(Format f) {
        switch (f) {
            case XML:
                return new XmlMapper();
            case JSON:
            default:
                return new ObjectMapper();
        }
    }
    public static <R> R deserialize(Format format, String content, Class<R> type) throws JsonProcessingException {
        ObjectMapper mapper = getMapper(format);
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(content, type);
    }
    public static <T> String serialize(Format format, T object) throws JsonProcessingException {
        ObjectMapper mapper = getMapper(format);
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(object);
    }
}
