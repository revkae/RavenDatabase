package me.raven;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class DataValue {

    public final String name;
    public final Object value;

    public static DataValue with(String name, Object value) {
        return new DataValue(name, value);
    }
}
