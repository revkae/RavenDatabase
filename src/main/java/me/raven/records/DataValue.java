package me.raven.records;

/**
 *
 * @param name
 * The column's name
 * @param value
 * The value of that column
 */
public record DataValue(String name, Object value) {

    public static DataValue of(String name, Object value) {
        return new DataValue(name, value);
    }
}