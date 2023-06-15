package me.raven;

import lombok.AllArgsConstructor;
import me.raven.records.Set;
import me.raven.records.Sets;
import me.raven.records.Where;
import me.raven.records.Wheres;

import javax.swing.text.html.Option;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@AllArgsConstructor
public class Query {

    private final Database database;

    public boolean rowExists(String tableName, Where where) {
        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue dataValue : where.dataValues()) {
            wheres.add(dataValue.name + " = '?'");
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(
                "SELECT * FROM %s WHERE EXISTS (SELECT * FROM %s WHERE `%s`)"
                        .formatted(tableName, tableName, wheres))) {

            for (int i = 1; i < where.dataValues().length + 1; i++) {
                preparedStatement.setObject(i, where.dataValues()[i - 1].value);
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

    public Optional<Row> getRow(String tableName, Where where) {
        Row row = new Row();

        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue dataValue : where.dataValues()) {
            wheres.add(dataValue.name + " = '?'");
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(
                "SELECT * FROM %s WHERE %s".formatted(tableName, wheres))) {

            for (int i = 1; i < where.dataValues().length + 1; i++) {
                preparedStatement.setObject(i, where.dataValues()[i - 1].value);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            Table table = database.getTable(tableName);

            while (resultSet.next()) {
                for (Column column : table.getColumns()) {
                    String name = column.getName();
                    if (resultSet.getObject(name) == null) continue;

                    row.addData(DataValue.with(name, resultSet.getObject(name)));
                }
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if (row.getRow().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(row);
    }

    public List<Optional<Row>> getRows(String tableName, Wheres wheres) {
        List<Optional<Row>> rows = new ArrayList<>();

        for (Where where : wheres.wheres()) {
            Optional<Row> row = getRow(tableName, where);

            rows.add(row);
        }
        return rows;
    }

    public void update(String tableName, Set set, Where where) {
        StringJoiner sets = new StringJoiner(", ");
        for (DataValue dataValue : set.dataValues()) {
            sets.add(dataValue.name + " = '?'");
        }

        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue dataValue : where.dataValues()) {
            wheres.add(dataValue.name + " = '" + dataValue.value + "'");
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(
                "UPDATE %s SET %s WHERE %s".formatted(tableName, sets, wheres))) {

            for (int i = 1; i < set.dataValues().length + 1; i++) {
                preparedStatement.setObject(i,  set.dataValues()[i - 1].value);
            }
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String tableName, Sets sets, Wheres wheres) {
        for (int i = 0; i < sets.sets().length; i++) {
            update(tableName, sets.sets()[i], wheres.wheres()[i]);
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

    public void delete(String tableName, Where where) {
        StringJoiner wheres = new StringJoiner(" AND ");
        for (DataValue dataValue : where.dataValues()) {
            wheres.add(dataValue.name + " = '?'");
        }

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(
                "DELETE FROM "
                        + tableName
                        + " WHERE "
                        + wheres)) {

            for (int i = 1; i < where.dataValues().length + 1; i++) {
                preparedStatement.setObject(i, where.dataValues()[i - 1]);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String tableName, Where... wheres) {
        for (Where row : wheres) {
            delete(tableName, row);
        }
    }
}
