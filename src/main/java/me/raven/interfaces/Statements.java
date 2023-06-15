package me.raven.interfaces;

import me.raven.Row;
import me.raven.records.Set;
import me.raven.records.Sets;
import me.raven.records.Where;
import me.raven.records.Wheres;

import java.util.List;
import java.util.Optional;

public interface Statements {
    boolean rowExists(Where where);

    Optional<Row> getRow(Where where);

    List<Optional<Row>> getRows(Wheres wheres);

    void update(Set set, Where where);

    void update(Sets sets, Wheres wheres);

    void addRows(Row... rows);

    void addRow(Row row);

    void delete(Where where);

    void delete(Wheres wheres);
}
