package sada.sadamall.utils;

import java.io.Serializable;

class SerializableClass implements Serializable {
    private String strField;
    private int intField;

    SerializableClass(String strField, int intField) {
        this.strField = strField;
        this.intField = intField;
    }

    int getIntField() {
        return intField;
    }

    String getStrField() {
        return strField;
    }


}
