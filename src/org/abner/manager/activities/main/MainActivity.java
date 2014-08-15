package org.abner.manager.activities.main;

import org.abner.manager.R;
import org.abner.manager.Settings;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;

public class MainActivity extends Activity
                implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

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
        Program program = Program.values()[position];

        Fragment fragment;

        fragment = getFragmentManager().findFragmentByTag(program.toString());
        if (fragment == null) {
            fragment = MainFragment.buildFragment(program);
        }

        getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment, program.toString()).commit();
    }

    public void onSectionAttached(int number) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            menu.clear();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

}
