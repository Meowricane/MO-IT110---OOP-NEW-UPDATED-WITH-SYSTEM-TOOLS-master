package com.motorph.view;

import javax.swing.JFormattedTextField;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

    private final String datePattern = "yyyy-MM-dd";
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parse(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {

        if (value != null) {
            Calendar cal = (Calendar) value;
            Date date = cal.getTime();
            return dateFormatter.format(date);
        }

        return "";
    }
}