package org.abner.manager.activities.main.fragment;

import org.abner.manager.activities.main.MainFragment;
import org.abner.manager.activities.main.adapter.SmsAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

public class SmsFragment extends MainFragment {

    @Override
    public ListView onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView onCreateView = super.onCreateView(inflater, container, savedInstanceState);
        setListAdapter(new SmsAdapter(getActivity()));
        return onCreateView;
    }
}
