package me.raven.interfaces;

import me.raven.Row;
import me.raven.records.Set;
import me.raven.records.Sets;
import me.raven.records.Where;
import me.raven.records.Wheres;

import java.util.List;
import java.util.Optional;

public interface QueryStatements {

    boolean rowExists(String tableName, Where where);

    Optional<Row> getRow(String tableName, Where where);

    List<Optional<Row>> getRows(String tableName, Wheres wheres);

    void update(String tableName, Set set, Where where);

    void update(String tableName, Sets sets, Wheres wheres);

    void addRows(String tableName, Row... rows);

    void addRow(String tableName, Row row);

    void delete(String tableName, Where where);

    void delete(String tableName, Wheres wheres);
}
