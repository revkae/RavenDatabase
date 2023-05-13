package me.raven;

public class DataValue {

    public final String name;
    public final Object value;

    public DataValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static DataValue with(String name, Object value) {
        return new DataValue(name, value);
    }
}
