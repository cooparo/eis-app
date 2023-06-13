package it.unipd.dei.eis.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.unipd.dei.eis.exceptions.MarshallingException;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Marshalling {

    private static ObjectMapper getMapper(FileFormat f) {
        switch (f) {
            case XML:
                return new XmlMapper();
            case CSV:
                return new CsvMapper();
            case JSON:
            default:
                return new ObjectMapper();
        }
    }

    /**
     * Deserialize a string in the specified format into an object of the given class type.
     * @param fileFormat the format of the content to be deserialized
     * @param content    a String containing serialized content in the specified format
     * @param type       a Class object representing the type of the object to be created
     * @param <R>        the generic type representing the object to be created
     * @return an object of the given class type deserialized from the provided content
     */
    public static <R> R deserialize(FileFormat fileFormat, String content, Class<R> type) {
        ObjectMapper mapper = getMapper(fileFormat);
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(content, type);
        } catch (JsonProcessingException e) {
            throw new MarshallingException(e.getMessage());
        }
    }

    /**
     * Deserialize a string in the specified format into an object of type R, specified by the provided TypeReference.
     * @param fileFormat the format of the content to be deserialized
     * @param content a String containing serialized content in the specified format
     * @param type a TypeReference object representing the generic type of the object to be deserialized
     * @return an object of type R, as specified by the TypeReference object, deserialized from the provided content string
     */
    public static <R> R deserialize(FileFormat fileFormat, String content, TypeReference<R> type) {
        ObjectMapper mapper = getMapper(fileFormat);
        mapper.registerModule(new JavaTimeModule());
        try {
            if (fileFormat == FileFormat.CSV) return deserializeCsv((CsvMapper) mapper, content, type);
            return mapper.readValue(content, type);
        } catch (IOException e) {
            throw new MarshallingException(e.getMessage());
        }
    }

    /**
     * Serialize an object to a string in the specified format.
     * @param fileFormat the format you want the object to be serialized
     * @param object the object to be serialized
     * @return the serialized string
     */
    public static String serialize(FileFormat fileFormat, Object object) {
        ObjectMapper mapper = getMapper(fileFormat);
        mapper.registerModule(new JavaTimeModule());
        try {
            if (fileFormat == FileFormat.CSV) return serializeCsv((CsvMapper) mapper, object);
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new MarshallingException(e.getMessage());
        }
    }

    /**
     * Deserialize a CSV file to an object of type R.
     * @param csvMapper the csv mapper
     * @param content the file content to be deserialized
     * @param type the reference to the object type
     * return the deserialized object
     */
    private static <R> R deserializeCsv(CsvMapper csvMapper, String content, TypeReference<R> type) throws IOException {

        Type parentType = type.getType();
        if (!(parentType instanceof ParameterizedType) || !(ArrayList.class.equals(((ParameterizedType) parentType).getRawType()))) {
            throw new IllegalArgumentException("R raw type must be java.util.ArrayList");
        }
        Type baseType = ((ParameterizedType) parentType).getActualTypeArguments()[0];

        // Parsing CSV header and camel case every column
        final String csvHeader = content.substring(0, content.indexOf('\n'));
        final String headerlessCsv = content.substring(content.indexOf('\n'));
        content = csvHeaderToCamelCase(csvHeader) + headerlessCsv;

        CsvSchema headerSchema = CsvSchema.emptySchema().withHeader();
        return (R) csvMapper.readerFor(((Class<?>) baseType))
                .with(headerSchema)
                .readValues(content).readAll();
    }

    /**
     * Serialize an object to a CSV string.
     * @param csvMapper the csv mapper
     * @param object the object to be serialized
     * return the serialized string
     */
    private static String serializeCsv(CsvMapper csvMapper, Object object) throws IOException {

        if (!(object instanceof ArrayList))
            throw new IllegalArgumentException("R raw type must be java.util.ArrayList");
        ArrayList<?> arrayList = (ArrayList<?>) object;
        if (arrayList.size() == 0) return "";

        Class<?> baseType = arrayList.get(0).getClass();
        final CsvSchema schema = csvMapper.schemaFor(baseType).withHeader();
        try (StringWriter strW = new StringWriter()) {
            SequenceWriter seqW = csvMapper.writer(schema).writeValues(strW);
            for (Object element : arrayList) {
                seqW.write(element);
            }
            return strW.toString();
        }
    }

    /**
     * Convert a CSV header to camel case. <p>
     * For example: <p>
     * <code>Identifier,URL,Title,Source Set,Source</code><p>
     * would be transformed into <p>
     * <code>identifier,url,title,sourceSet,source</code><p>
     * @param csvHeader the CSV header
     * return the camel case header
     */
    private static String csvHeaderToCamelCase(String csvHeader) {
        return Arrays.stream(csvHeader.split(",")).map(Words::toCamelCase).collect(Collectors.joining(","));
    }

    /**
     * Type reference for deserialization.
     * @param <T> the type
     */
    public static class TypeReference<T> extends com.fasterxml.jackson.core.type.TypeReference<T> {}

}
