package me.raven;

import lombok.AllArgsConstructor;
import me.raven.interfaces.QueryStatements;
import me.raven.records.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@AllArgsConstructor
public class Query implements QueryStatements {

    private final Database database;

    @Override
    public boolean oneRowExists(String tableName, Where where) {
        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue dataValue : where.dataValues()) {
            wheres.add(dataValue.name() + " = ?");
        }

        String query = String.format("SELECT COUNT(1) FROM %s WHERE %s", tableName, wheres);

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(query)) {
            int parameterIndex = 1;
            for (DataValue dataValue : where.dataValues()) {
                preparedStatement.setObject(parameterIndex++, dataValue.value());
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);

                if (count > 0) {
                    preparedStatement.close();
                    resultSet.close();
                    return true;
                }
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean multipleRowExists(String tableName, Wheres wheres) {
        for (Where where : wheres.wheres()) {
            if (oneRowExists(tableName, where)) continue;

            return false;
        }

        return true;
    }

    @Override
    public Optional<Row> getOneRow(String tableName, Where where) {
        Row row = new Row();

        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue dataValue : where.dataValues()) {
            wheres.add(dataValue.name() + " = '?'");
        }

        String query = "SELECT * FROM %s WHERE %s".formatted(tableName, wheres);

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(query)) {

            int parameterIndex = 1;
            for (DataValue dataValue : where.dataValues()) {
                preparedStatement.setObject(parameterIndex++, dataValue.value());
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            Table table = database.getTable(tableName);

            while (resultSet.next()) {
                for (Column column : table.getColumns()) {
                    String name = column.getName();
                    if (resultSet.getObject(name) == null) continue;

                    row.addData(DataValue.of(name, resultSet.getObject(name)));
                }
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (row.getRow().isEmpty()) {
            return Optional.empty();
        }

        row.setTableName(tableName);
        return Optional.of(row);
    }

    @Override
    public List<Optional<Row>> getMultipleRow(String tableName, Wheres wheres) {
        List<Optional<Row>> rows = new ArrayList<>();

        for (Where where : wheres.wheres()) {
            Optional<Row> row = getOneRow(tableName, where);

            row.ifPresent(value -> value.setTableName(tableName));

            rows.add(row);
        }
        return rows;
    }

    public void updateOne(String tableName, Set set, Where where) {
        StringJoiner sets = new StringJoiner(", ");
        for (DataValue dataValue : set.dataValues()) {
            sets.add(dataValue.name() + " = ?");
        }

        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue dataValue : where.dataValues()) {
            wheres.add(dataValue.name() + " = '" + dataValue.value() + "'");
        }

        String query = "UPDATE %s SET %s WHERE %s".formatted(tableName, sets, wheres);

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(query)) {

            int parameterIndex = 1;
            for (DataValue dataValue : set.dataValues()) {
                preparedStatement.setObject(parameterIndex++, dataValue.value());
            }
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateMultiple(String tableName, Sets sets, Wheres wheres) {
        for (int i = 0; i < sets.sets().length; i++) {
            updateOne(tableName, sets.sets()[i], wheres.wheres()[i]);
        }
    }

    @Override
    public void addMultipleRow(String tableName, Row... rows) {
        for (Row row : rows) {
            addOneRow(tableName, row);
        }
    }

    @Override
    public void addOneRow(String tableName, Row row) {
        StringJoiner names = new StringJoiner(",");
        StringJoiner values = new StringJoiner(",");

        for (DataValue value : row.getRow()) {
            names.add(value.name());
            values.add("?");
        }

        String query = "INSERT INTO %s (%s) VALUES (%s)".formatted(tableName, names, values);

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(query)) {
            int parameterIndex = 1;
            for (DataValue dataValue : row.getRow()) {
                preparedStatement.setObject(parameterIndex++, dataValue.value());
            }

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeOneRow(String tableName, Where where) {
        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue dataValue : where.dataValues()) {
            wheres.add(dataValue.name() + " = ?");
        }

        String query = "DELETE FROM %s WHERE %s".formatted(tableName, wheres);

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(query)) {
            int parameterIndex = 1;
            for (DataValue dataValue : where.dataValues()) {
                preparedStatement.setObject(parameterIndex++, dataValue.value());
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeMultipleRow(String tableName, Wheres wheres) {
        for (Where where : wheres.wheres()) {
            removeOneRow(tableName, where);
        }
    }

    @Override
    public void clearTable(String tableName) {
        String query = "TRUNCATE TABLE %s".formatted(tableName);

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
