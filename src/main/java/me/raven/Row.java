package me.raven;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.raven.records.DataValue;
import me.raven.records.Set;
import me.raven.records.Where;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public class Row implements Cloneable {

    @Setter
    private String tableName;
    private final Query query;
    private List<DataValue> row;

    public Row(String tableName, DataValue... dataValues) {
        this.tableName = tableName;
        this.query = Database.get().getQuery();

        this.row = Arrays.stream(dataValues).toList();
    }

    public Row(String tableName) {
        this.tableName = tableName;
        this.query = Database.get().getQuery();
    }

    public Row() {
        this.query = Database.get().getQuery();
    }

    public void update(DataValue... dataValues) {
        query.updateOne(tableName, Set.with(dataValues), Where.with(row.toArray(new DataValue[0])));

        this.row = Arrays.stream(dataValues).toList();
    }

    public void remove() {
        query.removeOneRow(tableName, Where.with(row.toArray(new DataValue[0])));
    }

    public DataValue getData(String name) {
        for (DataValue dataValue : row) {
            if (!dataValue.name().equalsIgnoreCase(name)) continue;

            return dataValue;
        }
        return null;
    }

    public void addData(DataValue dataValue) {
        row.add(dataValue);
    }

    public void removeData(DataValue dataValue) {
        row.remove(dataValue);
    }


    public static Row with(String tableName, DataValue... dataValues) {
        return new Row(tableName, dataValues);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row1 = (Row) o;
        return Objects.equals(tableName, row1.tableName) && Objects.equals(query, row1.query) && Objects.equals(row, row1.row);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, query, row);
    }

    @Override
    protected Row clone() {
        try {
            return (Row) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public String toString() {
        return "Row{" +
                "tableName='" + tableName + '\'' +
                ", query=" + query +
                ", row=" + row +
                '}';
    }
}
