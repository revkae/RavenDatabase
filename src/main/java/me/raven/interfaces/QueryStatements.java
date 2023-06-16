package me.raven.interfaces;

import me.raven.Row;
import me.raven.records.Set;
import me.raven.records.Sets;
import me.raven.records.Where;
import me.raven.records.Wheres;

import java.util.List;
import java.util.Optional;

public interface QueryStatements {

    boolean oneRowExists(String tableName, Where where);
    boolean multipleRowExists(String tableName, Wheres wheres);

    Optional<Row> getOneRow(String tableName, Where where);

    List<Optional<Row>> getMultipleRow(String tableName, Wheres wheres);

    void updateOne(String tableName, Set set, Where where);

    void updateMultiple(String tableName, Sets sets, Wheres wheres);

    void addMultipleRow(String tableName, Row... rows);

    void addOneRow(String tableName, Row row);

    void removeOneRow(String tableName, Where where);

    void removeMultipleRow(String tableName, Wheres wheres);

    void clearTable(String tableName);
}
