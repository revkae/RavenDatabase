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
    public boolean oneRowExists(Where where) {
        return query.oneRowExists(tableName, where);
    }

    @Override
    public boolean multipleRowExists(Wheres wheres) {
        return query.multipleRowExists(tableName, wheres);
    }

    @Override
    public Optional<Row> getOneRow(Where where) {
        return query.getOneRow(tableName, where);
    }

    @Override
    public List<Optional<Row>> getMultipleRow(Wheres wheres) {
        return query.getMultipleRow(tableName, wheres);
    }

    @Override
    public void updateOne(Set set, Where where) {
        query.updateOne(tableName, set, where);
    }

    @Override
    public void updateMultiple(Sets sets, Wheres wheres) {
        query.updateMultiple(tableName, sets, wheres);
    }

    @Override
    public void addMultipleRow(Row... rows) {
        query.addMultipleRow(tableName, rows);
    }

    @Override
    public void addOneRow(Row row) {
        query.addOneRow(tableName, row);
    }

    @Override
    public void removeOneRow(Where where) {
        query.removeOneRow(tableName, where);
    }

    @Override
    public void removeMultipleRow(Wheres wheres) {
        query.removeMultipleRow(tableName, wheres);
    }

    @Override
    public void clearTable() {
        query.clearTable(tableName);
    }

    @Override
    protected Table clone() {
        try {
            return (Table) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(database, table.database) && Objects.equals(query, table.query) && Objects.equals(tableName, table.tableName) && Objects.equals(columns, table.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(database, query, tableName, columns);
    }

    @Override
    public String toString() {
        return "Table{" +
                "database=" + database +
                ", query=" + query +
                ", tableName='" + tableName + '\'' +
                ", columns=" + columns +
                '}';
    }
}
