package sample;

import javafx.beans.property.IntegerProperty;

public class Column {
    private IntegerProperty order;
    private IntegerProperty spot;

    public Column(IntegerProperty order, IntegerProperty spot) {
        this.order = order;
        this.spot = spot;
    }

    public IntegerProperty orderProperty() {
        return order;
    }

    public IntegerProperty spotProperty() {
        return spot;
    }
}
