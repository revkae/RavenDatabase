package me.raven.records;

/**
 * @param wheres
 * The wheres for knowing where multiple rows are or values
 */
public record Wheres(Where... wheres) {

    public static Wheres with(Where... wheres) {
        return new Wheres(wheres);
    }
}
