package org.abner.manager.activities.main.fragment;

import org.abner.manager.R;
import org.abner.manager.SmsReadTask;
import org.abner.manager.activities.main.MainFragment;
import org.abner.manager.activities.main.adapter.SmsAdapter;
import org.abner.manager.activities.settings.SettingsActivity;
import org.abner.manager.db.DBAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SmsFragment extends MainFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        restoreActionBar("Sms");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                SmsReadTask task = new SmsReadTask(getActivity());
                task.execute();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(intent, 0);
                return true;
            case R.id.action_clear:
                new DBAdapter(getActivity()).clear();
                Toast.makeText(getActivity(), "As informações referente ao sms foram removidas com sucesso.", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_drop:
                new DBAdapter(getActivity()).drop();
                Toast.makeText(getActivity(), "Todas as informações foram removidas com sucesso.", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setListAdapter(new SmsAdapter(getActivity()));
        return view;
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub

    }
}
