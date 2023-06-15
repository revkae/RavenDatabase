package me.raven;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataType {
    BIGINT("BIGINT"),
    BINARY("BINARY"),
    BIT("BIT"),
    BLOB("BLOB"),
    BOOL("BOOL"),
    CHAR("CHAR"),
    DATE("DATE"),
    DATETIME("DATETIME"),
    DECIMAL("DECIMAL"),
    DOUBLE("DOUBLE"),
    ENUM("ENUM"),
    FLOAT("FLOAT"),
    INT("INT"),
    JSON("JSON"),
    LONGBLOB("LONGBLOB"),
    LONGTEXT("LONGTEXT"),
    MEDIUMBLOB("MEDIUMBLOB"),
    MEDIUMINT("MEDIUMINT"),
    MEDIUMTEXT("MEDIUMTEXT"),
    SET("SET"),
    SMALLINT("SMALLINT"),
    TEXT("TEXT"),
    TIME("TIME"),
    TIMESTAMP("TIMESTAMP"),
    TINYBLOB("TINYBLOB"),
    TINYINT("TINYINT"),
    TINYTEXT("TINYTEXT"),
    VARBINARY("VARBINARY"),
    VARCHAR("VARCHAR"),
    YEAR("YEAR");

    private final String type;
}
