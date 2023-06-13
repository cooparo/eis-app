package it.unipd.dei.eis.utils;

/**
 * An enum containing the supported formats for serialization and deserialization.
 * <p>
 * If you want to support a new format, check if Jackson has a mapper for it.
 * If so, simply add the format here and its mapper in Marshalling#getMapper switch case.
 * Otherwise, implement a mapper for the new format and then follow the instructions above.
 * <p>
 * Please, if a format name differs from its extension, use the latter.
 */
public enum FileFormat {
    JSON,
    XML,
    CSV
}
