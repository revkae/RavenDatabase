package me.raven.records;

public record Where(DataValue... dataValues) {

    public static Where with(DataValue... dataValues) {
        return new Where(dataValues);
    }
}
