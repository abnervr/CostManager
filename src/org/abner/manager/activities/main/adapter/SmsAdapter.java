package org.abner.manager.activities.main.adapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.abner.manager.R;
import org.abner.manager.model.sms.Sms;
import org.abner.manager.repository.sms.dao.SmsDao;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SmsAdapter extends AbstractRefreshableAdapter<Sms> {

    private final LayoutInflater inflater;

    public SmsAdapter(Activity context) {
        super(context);
        this.inflater = context.getLayoutInflater();
    }

    @Override
    protected List<Sms> getItems() {
        return new SmsDao(getContext()).find();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_row, parent, false);
        }

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
        text += DateFormat.getDateFormat(getContext()).format(sms.getDateSent());
        text += " ";
        text += DateFormat.getTimeFormat(getContext()).format(sms.getDateSent());

        TextView tv = (TextView) convertView.findViewById(R.id.name);
        tv.setText(text);

        TextView subname = (TextView) convertView.findViewById(R.id.subname);
        subname.setText(sms.getBody());
        return convertView;
    }
}
