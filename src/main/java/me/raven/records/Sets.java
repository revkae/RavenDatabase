package me.raven.records;

/**
 * @param sets
 * The sets for updating multiple rows
 */
public record Sets(Set... sets) {

    public static Sets with(Set... sets) {
        return new Sets(sets);
    }
}
