package me.raven;

import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

@Getter
public class Table {

    private final Database database;
    private final String tableName;

    public Table(String tableName, Column... columns) {
        this.database = Database.get();
        this.tableName = tableName;

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

    public boolean exists(Where where) {
        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue dataValue : where.getDataValues()) {
            wheres.add(dataValue.name + " = '" + dataValue.value + "'");
        }

        try {
            Connection connection = database.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM %s WHERE EXISTS (SELECT * FROM %s WHERE `%s`)"
                            .formatted(tableName, tableName, where)
            );

            ResultSet resultSet = preparedStatement.executeQuery();

            boolean rowExists = resultSet.next();

            if (rowExists) {

            }

            preparedStatement.close();
            resultSet.close();
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return !;
    }

    public void updateRow(Row row, WhereValue whereValue) {
        StringJoiner sets = new StringJoiner(", ");
        for (DataValue dataValue : whereValue.getDataValues()) {
            sets.add(dataValue.name + " = '" + dataValue.value + "'");
        }

        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue dataValue : row.getDataValues()) {
            wheres.add(dataValue.name + " = '" + dataValue.value + "'");
        }

        try (PreparedStatement statement = database.getConnection().prepareStatement(
                "UPDATE %s SET %s WHERE %s".formatted(tableName, sets, wheres))) {

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRow(Where where, WhereValue whereValue) {
        StringJoiner sets = new StringJoiner(", ");
        for (DataValue dataValue : whereValue.getDataValues()) {
            sets.add(dataValue.name + " = '" + dataValue.value + "'");
        }

        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue dataValue : where.getDataValues()) {
            wheres.add(dataValue.name + " = '" + dataValue.value + "'");
        }

        try (PreparedStatement statement = database.getConnection().prepareStatement(
                "UPDATE %s SET %s WHERE %s".formatted(tableName, sets, wheres))) {

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRows(Row... rows) {
        for (Row row : rows) {
            addRow(row);
        }
    }

    public void addRow(Row row) {
        StringJoiner names = new StringJoiner(",");
        StringJoiner values = new StringJoiner(",");

        for (DataValue value : row.getDataValues()) {
            names.add(value.name);
            values.add("?");
        }

        try (PreparedStatement statement = database.getConnection().prepareStatement(
                "INSERT INTO %s (%s) VALUES (%s)".formatted(tableName, names, values))) {

            int num = 1;
            for (DataValue value : row.getDataValues()) {
                if (value.value instanceof String)
                    statement.setString(num, (String) value.value);
                else if (value.value instanceof Integer)
                    statement.setInt(num, (Integer) value.value);
                else if (value.value instanceof Double)
                    statement.setDouble(num, (Double) value.value);
                else if (value.value instanceof Boolean)
                    statement.setBoolean(num, (Boolean) value.value);
                num++;
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(Row row) {
        StringJoiner whereValues = new StringJoiner(" AND ");
        for (DataValue dataValue : row.getDataValues()) {
            whereValues.add(dataValue.name + " = '" + dataValue.value + "'");
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(
                "DELETE FROM "
                        + tableName
                        + " WHERE "
                        + whereValues)) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRows(Row... rows) {
        for (Row row : rows) {
            deleteRow(row);
        }
    }

    public void getColumn(String name, DataValue... whereValues) {
        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue whereValue : whereValues) {
            wheres.add(whereValue.name + " = '" + whereValue.value + "'");
        }

        try (PreparedStatement statement = database.getConnection().prepareStatement(
                "SELECT " +
                        name +
                        " FROM " +
                        tableName +
                        " WHERE " +
                        wheres
        ); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                System.out.println(resultSet.getString(name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DataValue> getColumn(String name) {
        List<DataValue> output = new ArrayList<>();

        try (PreparedStatement statement = database.getConnection().prepareStatement(
                "SELECT " +
                        name +
                        " FROM " +
                        tableName
        ); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                output.add(DataValue.with(name, resultSet.getString(name)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    public List<DataValue> getColumns() {
        List<DataValue> output = new ArrayList<>();

        try (PreparedStatement statement = database.getConnection().prepareStatement(
                "SELECT " +
                        "*" +
                        " FROM " +
                        tableName
        ); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();

                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    output.add(DataValue.with(metaData.getColumnName(i), resultSet.getObject(i)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static Table with(String tableName, Column... columns) {
        return new Table(tableName, columns);
    }
}
