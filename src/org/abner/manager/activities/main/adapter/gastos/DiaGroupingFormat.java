package org.abner.manager.activities.main.adapter.gastos;

import java.util.Calendar;

import android.text.format.DateFormat;

public class DiaGroupingFormat extends GroupingFormat {

    @Override
    public String format(String value) {
        String[] parts = value.split("\\s");
        if (parts.length == 3) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, Integer.parseInt(parts[0]));
            cal.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[2]));

            return DateFormat.format("E, dd/MM/yyyy", cal.getTime()).toString();
        }
        return value;
    }

}
