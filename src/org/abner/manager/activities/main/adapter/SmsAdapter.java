package org.abner.manager.activities.main.adapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.abner.manager.R;
import org.abner.manager.model.sms.Sms;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SmsAdapter extends ArrayAdapter<Sms> {

    private final Activity context;

    public SmsAdapter(Activity context, List<Sms> smsList) {
        super(context, android.R.id.text1, smsList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.layout_row, null, true);

        Sms sms = getItem(position);

        String text = "";
        BigDecimal value = sms.findValue();
        if (value != null) {
            NumberFormat format = DecimalFormat.getCurrencyInstance();

            if (sms.getDebito() != null) {
                if (sms.getDebito()) {
                    value = value.negate();
                }
            }
            String formatedValue = format.format(value);
            text += formatedValue + " ";

        }
        text += DateFormat.getDateFormat(context).format(sms.getDateSent());
        text += " ";
        text += DateFormat.getTimeFormat(context).format(sms.getDateSent());

        TextView tv = (TextView) rowView.findViewById(R.id.name);
        tv.setText(text);

        TextView subname = (TextView) rowView.findViewById(R.id.subname);
        subname.setText(sms.getBody());
        return rowView;
    }
}
