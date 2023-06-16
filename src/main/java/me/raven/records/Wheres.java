package me.raven.records;

public record Wheres(Where... wheres) {

    public static Wheres with(Where... wheres) {
        return new Wheres(wheres);
    }
}
