package me.raven;

public class Main {
    public static void main(String[] args) {
        Database database = new Database();
        Table table = new Table("test",
                Column.with("id", DataType.VARCHAR, 100, true),
                Column.with("first", DataType.VARCHAR, 1000),
                Column.with("second", DataType.VARCHAR, 1000)
        );
        database.attachTable(table);
    }
}