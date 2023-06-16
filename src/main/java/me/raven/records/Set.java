package me.raven.records;

/**
 * @param dataValues
 * The data values for updating row
 */
public record Set(DataValue... dataValues) {

    public static Set with(DataValue... dataValues) {
        return new Set(dataValues);
    }
}
