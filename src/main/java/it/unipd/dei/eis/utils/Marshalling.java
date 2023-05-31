package it.unipd.dei.eis.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class Marshalling {
    private static ObjectMapper getMapper(Format f) {
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
    public static <R> R deserialize(Format format, String content, Class<R> type) throws IOException {
        ObjectMapper mapper = getMapper(format);
        return mapper.readValue(content, type);
    }
    public static <R> R deserialize(Format format, String content, TypeReference<R> type) throws IOException {
        ObjectMapper mapper = getMapper(format);
        if (format == Format.CSV) return deserializeCsv(mapper, content, type);
        return mapper.readValue(content, type);
    }
    public static String serialize(Format format, Object object) throws IOException {
        ObjectMapper mapper = getMapper(format);
        if (format == Format.CSV) return serializeCsv(mapper, object);
        return mapper.writeValueAsString(object);
    }

    private static <R> R deserializeCsv(ObjectMapper mapper, String content, TypeReference<R> type) throws IOException {
        if (!(mapper instanceof CsvMapper)) throw new IllegalArgumentException("mapper must be of type CsvMapper.");

        Type parentType = type.getType();
        if (!(parentType instanceof ParameterizedType) || !(ArrayList.class.equals(((ParameterizedType) parentType).getRawType()))) {
            throw new IllegalArgumentException("R raw type must be java.util.ArrayList");
        }
        Type baseType = ((ParameterizedType) parentType).getActualTypeArguments()[0];


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

    public static class TypeReference<T> extends com.fasterxml.jackson.core.type.TypeReference<T> {}

}
