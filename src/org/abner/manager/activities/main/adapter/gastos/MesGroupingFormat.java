package org.abner.manager.activities.main.adapter.gastos;

import java.util.Calendar;
import java.util.Locale;

import android.text.format.DateFormat;

public class MesGroupingFormat extends GroupingFormat {

    @Override
    public String format(String value) {
        Calendar cal = Calendar.getInstance();
        String[] parts = value.split("\\s");
        if (parts.length == 2) {
            cal.set(Calendar.YEAR, Integer.parseInt(parts[0]));
            cal.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);

            String formatMonth = DateFormat.format("MMMM - yyyy", cal).toString();
            formatMonth = formatMonth.substring(0, 1).toUpperCase(Locale.US) + formatMonth.substring(1);

            return formatMonth;
        }
        return value;
    }

}
