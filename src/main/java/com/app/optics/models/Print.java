package com.app.optics.models;

public class Print {
    private static final String EMPTY = "—";

    public static String get(Object object) {
        if (object == null) {
            return EMPTY;
        }
        if (object instanceof String str) {
            return str.isBlank() ? EMPTY : str;
        }

        return object.toString();
    }

    public static String get_plus(Object object) {
        if (object == null) {
            return EMPTY;
        }
        if (object instanceof Double d) {
            if (d > 0) {
                return "+" + d;
            }
            return d.toString();
        }
        return "—";
    }

}
