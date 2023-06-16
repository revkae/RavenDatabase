package me.raven;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import me.raven.enums.DataType;

import java.util.Objects;

@Getter
@NonNull
@AllArgsConstructor
public class Column implements Cloneable {

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

    @Override
    protected Column clone() {
        try {
            return (Column) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return length == column.length && isPrimary == column.isPrimary && name.equals(column.name) && type == column.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, length, isPrimary);
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", length=" + length +
                ", isPrimary=" + isPrimary +
                '}';
    }
}
