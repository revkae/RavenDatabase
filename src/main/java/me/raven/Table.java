package me.raven;

import lombok.Getter;
import lombok.NonNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

@Getter
@NonNull
public class Table {

    private final Database database;
    private final String tableName;
    private final List<Column> columns;

    public Table(String tableName, Column... columns) {
        this.database = Database.get();
        this.tableName = tableName;
        this.columns = List.of(columns);

        StringJoiner statementArgs = new StringJoiner(",");
        for (Column column : columns) {
            statementArgs.add(column.name + " " + column.type + " (" + column.length + ")");

            if (column.isPrimary) {
                statementArgs.add("PRIMARY KEY(" + column.name + ")");
            }
        }

        try (PreparedStatement statement = database.getConnection().prepareStatement(
                "CREATE TABLE IF NOT EXISTS %s(%s)".formatted(tableName, statementArgs))) {

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Table with(String tableName, Column... columns) {
        return new Table(tableName, columns);
    }
}
