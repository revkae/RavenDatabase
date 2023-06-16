package me.raven.interfaces;

import me.raven.Row;
import me.raven.records.Set;
import me.raven.records.Sets;
import me.raven.records.Where;
import me.raven.records.Wheres;

import java.util.List;
import java.util.Optional;

public interface Statements {
    boolean oneRowExists(Where where);
    boolean multipleRowExists(Wheres wheres);

    Optional<Row> getOneRow(Where where);

    List<Optional<Row>> getMultipleRow(Wheres wheres);

    void updateOne(Set set, Where where);

    void updateMultiple(Sets sets, Wheres wheres);

    void addMultipleRow(Row... rows);

    void addOneRow(Row row);

    void removeOneRow(Where where);

    void removeMultipleRow(Wheres wheres);

    void clearTable();
}
