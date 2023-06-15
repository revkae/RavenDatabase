package me.raven;

import lombok.AllArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.StringJoiner;

@AllArgsConstructor
public class Query {

    private final Database database;

    public boolean rowExists(String tableName, Row row) {
        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue dataValue : row.getRow()) {
            wheres.add(dataValue.name + " = '?'");
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(
                "SELECT * FROM %s WHERE EXISTS (SELECT * FROM %s WHERE `%s`)"
                        .formatted(tableName, tableName, wheres))) {

            for (int i = 1; i < row.getRow().size() + 1; i++) {
                preparedStatement.setObject(i, row.getRow().get(i - 1).value);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                preparedStatement.close();
                resultSet.close();
                return true;
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return false;
    }

    public boolean rowExists(String tableName, DataValue... dataValues) {
        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue dataValue : dataValues) {
            wheres.add(dataValue.name + " = '?'");
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(
                "SELECT * FROM %s WHERE EXISTS (SELECT * FROM %s WHERE `%s`)"
                        .formatted(tableName, tableName, wheres))) {

            for (int i = 1; i < dataValues.length + 1; i++) {
                preparedStatement.setObject(i, dataValues[i - 1].value);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                preparedStatement.close();
                resultSet.close();
                return true;
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return false;
    }

    public Row getRow(String tableName, DataValue... dataValues) {
        Row row = new Row();

        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue dataValue : dataValues) {
            wheres.add(dataValue.name + " = '?'");
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(
                "SELECT * FROM %s WHERE %s".formatted(tableName, wheres))) {

            for (int i = 1; i < dataValues.length + 1; i++) {
                preparedStatement.setObject(i, dataValues[i - 1].value);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            int num = 0;
            Table table = database.getTable(tableName);

            while (resultSet.next()) {
                for (Column column : table.getColumns()) {

                }

                row.addData(DataValue.with(resultSet.get));resultSet.getObject(num);
                num++;
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRow(String tableName, Row row, DataValue... dataValues) {
        StringJoiner sets = new StringJoiner(", ");
        for (DataValue dataValue : dataValues) {
            sets.add(dataValue.name + " = '?'");
        }

        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue dataValue : row.getRow()) {
            wheres.add(dataValue.name + " = '" + dataValue.value + "'");
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(
                "UPDATE %s SET %s WHERE %s".formatted(tableName, sets, wheres))) {

            for (int i = 1; i < dataValues.length + 1; i++) {
                preparedStatement.setObject(i, dataValues[i - 1].value);
            }
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRows(String tableName, Row... rows) {
        for (Row row : rows) {
            addRow(tableName, row);
        }
    }

    public void addRow(String tableName, Row row) {
        StringJoiner names = new StringJoiner(",");
        StringJoiner values = new StringJoiner(",");

        for (DataValue value : row.getRow()) {
            names.add(value.name);
            values.add("?");
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(
                "INSERT INTO %s (%s) VALUES (%s)".formatted(tableName, names, values))) {

            for (int i = 1; i < row.getRow().size() + 1; i++) {
                preparedStatement.setObject(i, row.getRow().get(i - 1));
            }

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(String tableName, Row row) {
        StringJoiner whereValues = new StringJoiner(" AND ");
        for (DataValue dataValue : row.getRow()) {
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

    public void deleteRows(String tableName, Row... rows) {
        for (Row row : rows) {
            deleteRow(tableName, row);
        }
    }
}
