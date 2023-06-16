package me.raven.records;

public record Set(DataValue... dataValues) {

    public static Set with(DataValue... dataValues) {
        return new Set(dataValues);
    }
}
