package me.raven.records;

public record Sets(Set... sets) {

    public static Sets with(Set... sets) {
        return new Sets(sets);
    }
}
