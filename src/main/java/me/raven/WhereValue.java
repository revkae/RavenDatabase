package me.raven;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WhereValue {

    private List<DataValue> dataValues;

    public WhereValue(DataValue... dataValues) {
        this.dataValues = List.of(dataValues);
    }

    public static WhereValue with(DataValue... dataValues) {
        return new WhereValue(dataValues);
    }
}
