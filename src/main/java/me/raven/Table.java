package me.raven;

import lombok.Getter;
import lombok.NonNull;
import me.raven.interfaces.Statements;
import me.raven.records.Set;
import me.raven.records.Sets;
import me.raven.records.Where;
import me.raven.records.Wheres;

import java.sql.*;
import java.util.*;

@Getter
@NonNull
public class Table implements Statements {

    private final Database database;
    private final Query query;
    private final String tableName;
    private final List<Column> columns;

    public Table(String tableName, Column... columns) {
        this.database = Database.get();
        this.query = database.getQuery();
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


    @Override
    public boolean rowExists(Where where) {
        return query.rowExists(tableName, where);
    }

    @Override
    public Optional<Row> getRow(Where where) {
        return query.getRow(tableName, where);
    }

    @Override
    public List<Optional<Row>> getRows(Wheres wheres) {
        return query.getRows(tableName, wheres);
    }

    @Override
    public void update(Set set, Where where) {
        query.update(tableName, set, where);
    }

    @Override
    public void update(Sets sets, Wheres wheres) {
        query.update(tableName, sets, wheres);
    }

    @Override
    public void addRows(Row... rows) {
        query.addRows(tableName, rows);
    }

    @Override
    public void addRow(Row row) {
        query.addRow(tableName, row);
    }

    @Override
    public void delete(Where where) {
        query.delete(tableName, where);
    }

    @Override
    public void delete(Wheres wheres) {
        query.delete(tableName, wheres);
    }
}
