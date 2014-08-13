package org.abner.manager.activities.main.fragment;

import org.abner.manager.R;
import org.abner.manager.activities.main.MainFragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class RelatorioFragment extends MainFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main2, menu);
        restoreActionBar("Relatórios");
    }

}
