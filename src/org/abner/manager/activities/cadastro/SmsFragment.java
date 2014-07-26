package org.abner.manager.activities.cadastro;

import org.abner.manager.R;
import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.sms.Sms;
import org.abner.manager.repository.sms.dao.SmsDao;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SmsFragment extends Fragment {

    private Sms sms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Movimento movimento = (Movimento) getArguments().getSerializable("movimento");
        sms = new SmsDao(getActivity()).findByMovimento(movimento);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sms, container, false);

        TextView view = (TextView) rootView.findViewById(R.id.sms_body);
        if (sms != null) {
            view.setText(sms.getBody());
        } else {
            view.setText("Esse movimento não foi gerado a partir de um sms.");
        }

        return rootView;
    }
}
