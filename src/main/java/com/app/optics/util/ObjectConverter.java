package com.app.optics.util;

import java.time.LocalDate;

public class ObjectConverter {
    public static String objToStr(Object object) {
        if (object == null || (object instanceof String str && str.isBlank()))
            return "-";
        return String.valueOf(object);
    }

    public static String str(String name, Object object) {
        return name + ": " + objToStr(object) + "\n";
    }

    public static String round(Double value, int precision) {
        if (value == null)
            return null;
        return String.valueOf(Math.round(value * (10.0 * precision)) / (10.0 * precision));
    }

    public static String currency(Double value) {
        return round(value, 3);
    }

    public static String strNoLine(String name, Object object) {
        return name + ": " + objToStr(object);
    }

    public static String readableDate(LocalDate date) {
        String[] months = {"Января", "Февраля", "Марта", "Апреля",
                "Мая", "Июня", "Июля", "Августа",
                "Сентября", "Октября", "Ноября", "Декабря"};
        if (date == null)
            return null;
        return date.getDayOfMonth() + " " + months[date.getMonthValue() - 1] + " " + date.getYear();
    }
}
