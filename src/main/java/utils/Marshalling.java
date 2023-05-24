package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;


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
    public static <R> R deserialise(Format format, String content, Class<R> type) throws JsonProcessingException {
        ObjectMapper mapper = getMapper(format);
        return mapper.readValue(content, type);
    }
    public static <T> String serialize(Format format, T object) throws JsonProcessingException {
        ObjectMapper mapper = getMapper(format);
        return mapper.writeValueAsString(object);
    }
}
