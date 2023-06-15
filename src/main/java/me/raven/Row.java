package me.raven;

import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class Row {

    private Query query;
    private List<DataValue> row;

    public Row(DataValue... dataValues) {
        this.query = Database.get().getQuery();

        this.row = Arrays.stream(dataValues).toList();
    }

    public Row() {
        this.query = Database.get().getQuery();
    }

    public void update(String tableName, DataValue... dataValues) {
        query.updateRow(tableName, this, dataValues);

        this.row = Arrays.stream(dataValues).toList();
    }

    public void delete(String tableName) {
        query.deleteRow(tableName, this);
    }

    public DataValue getData(String name) {
        for (DataValue dataValue : row) {
            if (!dataValue.name.equalsIgnoreCase(name)) continue;

            return dataValue;
        }
        return null;
    }

    public void addData(DataValue dataValue) {
        row.add(dataValue);
    }

    public static Row with(DataValue... dataValues) {
        return new Row(dataValues);
    }
}
