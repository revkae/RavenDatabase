package me.raven;

import java.util.Arrays;
import java.util.List;

public class Row {

    private List<DataValue> row;

    public Row(DataValue... dataValues) {
        this.row = Arrays.stream(dataValues).toList();
    }

    public void update(DataValue dataValue) {
        for (DataValue value : row) {
            if (!value.name.equalsIgnoreCase(dataValue.name)) continue;

            value = dataValue;
        }
    }

    public DataValue get(String name) {
        for (DataValue dataValue : row) {
            if (!dataValue.name.equalsIgnoreCase(name)) continue;

            return dataValue;
        }
        return null;
    }

    public List<DataValue> getDataValues() {
        return row;
    }

    public static Row with(DataValue... dataValues) {
        return new Row(dataValues);
    }
}
