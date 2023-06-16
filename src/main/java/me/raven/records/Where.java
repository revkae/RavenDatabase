package me.raven.records;

/**
 * @param dataValues
 * The data values for knowing where the row or value is
 */
public record Where(DataValue... dataValues) {

    public static Where with(DataValue... dataValues) {
        return new Where(dataValues);
    }
}
