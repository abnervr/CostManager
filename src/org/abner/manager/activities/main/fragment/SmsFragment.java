package org.abner.manager.activities.main.fragment;

import org.abner.manager.activities.main.MainFragment;
import org.abner.manager.activities.main.adapter.SmsAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SmsFragment extends MainFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View onCreateView = super.onCreateView(inflater, container, savedInstanceState);
        setListAdapter(new SmsAdapter(getActivity()));
        return onCreateView;
    }
}
