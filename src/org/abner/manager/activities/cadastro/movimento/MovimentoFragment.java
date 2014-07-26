package org.abner.manager.activities.cadastro.movimento;

import java.math.RoundingMode;
import java.util.Calendar;

import org.abner.manager.R;
import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.movimento.TipoMovimento;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class MovimentoFragment extends Fragment {

    private Movimento movimento;

    private EditText dataPicker;

    private EditText timePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movimento = (Movimento) getArguments().getSerializable("movimento");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movimento, container, false);

        dataPicker = (EditText) rootView.findViewById(R.id.movimento_data);
        timePicker = (EditText) rootView.findViewById(R.id.movimento_time);

        Calendar cal = Calendar.getInstance();
        if (movimento.getData() != null) {
            cal.setTime(movimento.getData());
        } else {
            movimento.setData(cal.getTime());
        }
        java.text.DateFormat df = DateFormat.getDateFormat(getActivity());
        dataPicker.setText(df.format(cal.getTime()));

        java.text.DateFormat tf = DateFormat.getTimeFormat(getActivity());
        timePicker.setText(tf.format(cal.getTime()));

        dataPicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }

        });

        timePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");
            }

        });

        CheckBox chkCredito = (CheckBox) rootView.findViewById(R.id.movimento_credito);
        chkCredito.setChecked(movimento.getTipo() == TipoMovimento.CREDITO);

        if (movimento.getValor() != null) {
            EditText textValor = (EditText) rootView.findViewById(R.id.movimento_valor);
            textValor.setText(movimento.getValor().setScale(2, RoundingMode.HALF_UP).toString());
        }
        return rootView;
    }

    public class DatePickerFragment extends DialogFragment
                    implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            if (movimento.getData() != null) {
                c.setTime(movimento.getData());
            }
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);

            movimento.setData(c.getTime());

            java.text.DateFormat df = DateFormat.getDateFormat(getActivity());
            dataPicker.setText(df.format(c.getTime()));
        }
    }

    public class TimePickerFragment extends DialogFragment
                    implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            if (movimento.getData() != null) {
                c.setTime(movimento.getData());
            }
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                            DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar c = Calendar.getInstance();
            if (movimento.getData() != null) {
                c.setTime(movimento.getData());
            }
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            movimento.setData(c.getTime());

            java.text.DateFormat tf = DateFormat.getTimeFormat(getActivity());
            timePicker.setText(tf.format(c.getTime()));
        }
    }
}
