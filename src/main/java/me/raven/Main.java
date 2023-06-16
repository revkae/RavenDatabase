package me.raven;

import me.raven.enums.DataType;
import me.raven.records.DataValue;
import me.raven.records.Settings;

public class Main {
    public static void main(String[] args) {
        Database database = new Database(Settings.with("localhost", "test", "root", "", 15, 3306, false));
        Query query = database.getQuery();
        Table tablo = Table.with(
                "tablo",
                Column.with("name", DataType.VARCHAR, 255),
                Column.with("surname", DataType.VARCHAR, 255));
        database.attachTable(tablo);

        tablo.clearTable();

        Row row = Row.with("tablo", DataValue.of("name", "Ravan"), DataValue.of("surname", "Mamiyev"));
        Row row1 = Row.with("tablo", DataValue.of("name", "Fidan"), DataValue.of("surname", "Mamiyeva"));
        Row row2 = Row.with("tablo", DataValue.of("name", "Elnara"), DataValue.of("surname", "Gasimova"));
        Row row3 = Row.with("tablo", DataValue.of("name", "Sahip"), DataValue.of("surname", "Mamiyev"));

        tablo.addMultipleRow(row, row1, row2, row3);

        row.update(DataValue.of("name", "Babuska"));
        row.remove();
    }
}