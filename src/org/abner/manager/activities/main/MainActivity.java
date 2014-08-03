package org.abner.manager.activities.main;

import org.abner.manager.R;
import org.abner.manager.Settings;
import org.abner.manager.SmsReadTask;
import org.abner.manager.activities.cadastro.empresa.EmpresaActivity;
import org.abner.manager.activities.cadastro.movimento.MovimentoActivity;
import org.abner.manager.activities.cadastro.tipo.TipoActivity;
import org.abner.manager.activities.main.adapter.gastos.Grouping;
import org.abner.manager.activities.settings.SettingsActivity;
import org.abner.manager.db.DBAdapter;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class MainActivity extends Activity
                implements NavigationDrawerFragment.NavigationDrawerCallbacks, OnNavigationListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private Program program;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.loadSetting(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                        getFragmentManager().findFragmentById(R.id.navigation_drawer);

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
        Fragment fragment = MainFragment.buildFragment(program, grouping);

        getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).commit();
    }

    public void onSectionAttached(int number) {}

    public void restoreActionBar(String title) {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            switch (program) {
                case CADASTRO:
                    getMenuInflater().inflate(R.menu.cadastro, menu);
                    restoreActionBar("Cadastro");
                    return true;
                case GASTOS:
                    getMenuInflater().inflate(R.menu.gastos, menu);

                    ActionBar actionBar = getActionBar();
                    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

                    SpinnerAdapter gastosSpinnerAdapter = new ArrayAdapter<Grouping>(this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    Grouping.values());
                    actionBar.setListNavigationCallbacks(gastosSpinnerAdapter, this);
                    actionBar.setDisplayShowTitleEnabled(false);
                    actionBar.setTitle("Gastos");
                    actionBar.setSelectedNavigationItem(2);

                    return true;
                case SMS:
                    getMenuInflater().inflate(R.menu.main, menu);
                    restoreActionBar("Sms");
                    return true;
                case RELATORIOS:
                    // Only show items in the action bar relevant to this screen
                    // if the drawer is not showing. Otherwise, let the drawer
                    // decide what to show in the action bar.
                    getMenuInflater().inflate(R.menu.main2, menu);
                    restoreActionBar("Relatórios");
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
            case R.id.action_gasto_empresas:
                updateFragment(Grouping.ANO);
                return true;
            case R.id.action_gasto_tipos:
                updateFragment(Grouping.DIA);
                return true;

            case R.id.action_adicionar:
                Intent intent = new Intent(this, MovimentoActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_empresas:
                intent = new Intent(this, EmpresaActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_tipos:
                intent = new Intent(this, TipoActivity.class);
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

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        updateFragment(Grouping.values()[itemPosition]);
        return true;
    }

}
