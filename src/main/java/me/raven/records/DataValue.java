package me.raven.records;

public record DataValue(String name, Object value) {

    public static DataValue of(String name, Object value) {
        return new DataValue(name, value);
    }
}