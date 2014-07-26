package org.abner.manager.activities.main;

import org.abner.manager.R;
import org.abner.manager.Settings;
import org.abner.manager.SmsReadTask;
import org.abner.manager.activities.cadastro.movimento.MovimentoActivity;
import org.abner.manager.activities.main.adapter.GastosAdapter.Grouping;
import org.abner.manager.activities.settings.SettingsActivity;
import org.abner.manager.db.DBAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity
                implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private Program program;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.loadSetting(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                        getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                        R.id.navigation_drawer,
                        (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        program = Program.values()[position];
        updateFragment();
    }

    private void updateFragment() {
        updateFragment(Grouping.MES);
    }

    private void updateFragment(Grouping grouping) {
        Bundle arguments = new Bundle();
        arguments.putString(MainFragment.ARG_ITEM_ID, program.toString());
        arguments.putString(MainFragment.GROUPING_ID, grouping.toString());

        MainFragment fragment = new MainFragment();
        fragment.setArguments(arguments);

        getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).commit();
    }

    public void onSectionAttached(int number) {
        mTitle = Program.values()[number].toString();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            switch (program) {
                case CADASTRO:
                    getMenuInflater().inflate(R.menu.cadastro, menu);
                    restoreActionBar();
                    return true;
                case GASTOS:
                    getMenuInflater().inflate(R.menu.gastos, menu);
                    restoreActionBar();
                    return true;
                case SMS:
                    getMenuInflater().inflate(R.menu.main, menu);
                    restoreActionBar();
                    return true;
                case RELATORIOS:
                    // Only show items in the action bar relevant to this screen
                    // if the drawer is not showing. Otherwise, let the drawer
                    // decide what to show in the action bar.
                    getMenuInflater().inflate(R.menu.main2, menu);
                    restoreActionBar();
                    return true;
                default:
                    break;
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_gasto_ano:
                updateFragment(Grouping.ANO);
                return true;
            case R.id.action_gasto_mes:
                updateFragment(Grouping.MES);
                return true;
            case R.id.action_gasto_semana:
                updateFragment(Grouping.SEMANA);
                return true;
            case R.id.action_gasto_dia:
                updateFragment(Grouping.DIA);
                return true;
            case R.id.action_adicionar:
                Intent intent = new Intent(this, MovimentoActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                SmsReadTask task = new SmsReadTask(this);
                task.execute();
                return true;

            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, 0);
                return true;
            case R.id.action_clear:
                new DBAdapter(this).clear();
                Toast.makeText(this, "As informações referente ao sms foram removidas com sucesso.", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_drop:
                new DBAdapter(this).drop();
                Toast.makeText(this, "Todas as informações foram removidas com sucesso.", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
