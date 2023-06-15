package me.raven;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@NonNull
@AllArgsConstructor
public class Column {

    public final String name;
    public final DataType type;
    public final int length;
    public final boolean isPrimary;

    public Column(String name, DataType type, int length) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.isPrimary = false;
    }

    public static Column with(String name, DataType type, int length) {
        return new Column(name, type, length);
    }

    public static Column with(String name, DataType type, int length, boolean isPrimary) {
        return new Column(name, type, length, isPrimary);
    }
}
