package me.raven;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Where {

    private List<DataValue> dataValues;

    public Where(DataValue... dataValues) {
        this.dataValues = List.of(dataValues);
    }

    public static Where with(DataValue... dataValues) {
        return new Where(dataValues);
    }
}
