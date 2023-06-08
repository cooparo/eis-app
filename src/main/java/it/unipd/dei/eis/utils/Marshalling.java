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
    public static <R> R deserialize(FileFormat fileFormat, String content, Class<R> type) {
        ObjectMapper mapper = getMapper(fileFormat);
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(content, type);
        } catch (JsonProcessingException e) {
            throw new MarshallingException(e.getMessage());
        }
    }
    public static <R> R deserialize(FileFormat fileFormat, String content, TypeReference<R> type) {
        ObjectMapper mapper = getMapper(fileFormat);
        mapper.registerModule(new JavaTimeModule());
        try {
            if (fileFormat == FileFormat.CSV) return deserializeCsv(mapper, content, type);
            return mapper.readValue(content, type);
        } catch (IOException e) {
            throw new MarshallingException(e.getMessage());
        }

    }
    public static String serialize(FileFormat fileFormat, Object object) {
        ObjectMapper mapper = getMapper(fileFormat);
        mapper.registerModule(new JavaTimeModule());
        try {
            if (fileFormat == FileFormat.CSV) return serializeCsv(mapper, object);
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new MarshallingException(e.getMessage());
        }
    }

    private static <R> R deserializeCsv(ObjectMapper mapper, String content, TypeReference<R> type) throws IOException {
        if (!(mapper instanceof CsvMapper)) throw new IllegalArgumentException("mapper must be of type CsvMapper.");

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
        return (R) mapper.readerFor(((Class<?>) baseType))
                .with(headerSchema)
                .readValues(content).readAll();
    }
    private static String serializeCsv(ObjectMapper mapper, Object object) throws IOException {
        if (!(mapper instanceof CsvMapper)) throw new IllegalArgumentException("mapper must be of type CsvMapper.");
        CsvMapper csvMapper = (CsvMapper) mapper;

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
    private static String csvHeaderToCamelCase(String csvHeader) {
        return Arrays.stream(csvHeader.split(",")).map(Words::toCamelCase).collect(Collectors.joining(","));
    }

    public static class TypeReference<T> extends com.fasterxml.jackson.core.type.TypeReference<T> {}

}
