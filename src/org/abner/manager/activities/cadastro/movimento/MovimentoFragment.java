package org.abner.manager.activities.cadastro.movimento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

import org.abner.manager.R;
import org.abner.manager.activities.cadastro.common.EmpresaListFragment;
import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.movimento.TipoMovimento;
import org.abner.manager.model.sms.Sms;
import org.abner.manager.repository.movimento.MovimentoRepository;
import org.abner.manager.repository.movimento.dao.MovimentoDao;
import org.abner.manager.repository.sms.dao.SmsDao;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class MovimentoFragment extends Fragment {

    private static final int CHANGE_EMPRESA = 11;

    private Movimento movimento;

    private EditText dataPicker;

    private EditText timePicker;

    private BigDecimal maxValue;

    private boolean readOnly;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movimento = (Movimento) getArguments().getSerializable("movimento");

        if (movimento.getMovimentoPai() != null) {
            maxValue = movimento.getMovimentoPai().getValor();
            if (movimento.getValor() != null) {
                maxValue = maxValue.add(movimento.getValor());
            }
        } else {
            readOnly = !new MovimentoDao(getActivity()).getMovimentosFilhos(movimento).isEmpty();
        }

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

        if (!readOnly) {
            dataPicker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }

            });
        }

        if (!readOnly) {
            timePicker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.show(getFragmentManager(), "timePicker");
                }

            });
        }

        CheckBox chkCredito = (CheckBox) rootView.findViewById(R.id.movimento_credito);
        chkCredito.setChecked(movimento.getTipo() == TipoMovimento.CREDITO);
        chkCredito.setEnabled(!readOnly);

        EditText textValor = (EditText) rootView.findViewById(R.id.movimento_valor);
        textValor.setEnabled(!readOnly);
        if (movimento.getValor() != null) {
            textValor.setText(movimento.getValor().setScale(2, RoundingMode.HALF_UP).toString());
        }

        TextView view = (TextView) rootView.findViewById(R.id.movimento_empresa);
        if (movimento.getEmpresa() != null) {
            view.setText(movimento.getEmpresa().getNome());
        }

        if (!readOnly) {
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    DialogFragment dialog = new EmpresaListFragment();

                    Bundle args = new Bundle();
                    args.putSerializable("movimento", movimento);
                    dialog.setArguments(args);

                    dialog.setTargetFragment(MovimentoFragment.this, CHANGE_EMPRESA);
                    dialog.show(getFragmentManager(), "Empresa");
                }
            });
        }

        Sms sms = new SmsDao(getActivity()).findByMovimento(movimento);
        if (sms != null) {
            view = (TextView) rootView.findViewById(R.id.sms_body);
            view.setText(sms.getBody());
        }

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHANGE_EMPRESA) {
            if (movimento.getId() != null) {
                movimento = new MovimentoDao(getActivity()).find(movimento.getId());
            }

            TextView view = (TextView) getActivity().findViewById(R.id.movimento_empresa);
            if (movimento.getEmpresa() != null) {
                view.setText(movimento.getEmpresa().getNome());
                Log.i("MovimentoFragment", "update " + movimento.getEmpresa().getNome());
            } else {
                view.setText(null);
                Log.i("MovimentoFragment", "update empresa");
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.control, menu);
        if (movimento.getId() == null || readOnly) {
            MenuItem item = menu.findItem(R.id.action_delete);
            item.setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_save:
                updateMovimentoFromUI();

                if (movimento.getValor() != null && maxValue != null
                                && movimento.getValor().compareTo(maxValue) > 0) {
                    movimento.setValor(maxValue);
                }

                if (movimento.getValor() == null) {
                    EditText textValor = (EditText) getActivity().findViewById(R.id.movimento_valor);
                    textValor.setError("Preencha o campo valor");
                } else if (movimento.getId() == null) {
                    MovimentoRepository movimentoRepository = new MovimentoDao(getActivity());
                    movimentoRepository.insert(movimento);
                    getActivity().finish();
                } else {
                    MovimentoRepository movimentoRepository = new MovimentoDao(getActivity());
                    movimentoRepository.update(movimento);
                    getActivity().finish();
                }
                return true;
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setMessage("Deseja remover o movimento?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MovimentoRepository movimentoRepository = new MovimentoDao(getActivity());
                        movimentoRepository.remove(movimento);
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("Não", null);
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMovimentoFromUI() {
        CheckBox chkCredito = (CheckBox) getActivity().findViewById(R.id.movimento_credito);
        if (chkCredito.isChecked()) {
            movimento.setTipo(TipoMovimento.CREDITO);
        } else {
            movimento.setTipo(TipoMovimento.DEBITO);
        }

        EditText textValor = (EditText) getActivity().findViewById(R.id.movimento_valor);
        try {
            movimento.setValor(new BigDecimal(textValor.getText().toString()));
        } catch (NumberFormatException e) {
            movimento.setValor(null);
        }
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
