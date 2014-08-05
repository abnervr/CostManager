package org.abner.manager.activities.cadastro.movimento;

import java.util.Locale;

import org.abner.manager.R;
import org.abner.manager.model.movimento.Movimento;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MovimentoActivity extends Activity {

    public static final String ARG_MOVIMENTO = "arg_movimento";

    public static final String ARG_MOVIMENTO_PAI = "arg_parent_movimento";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private Movimento movimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimento);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ARG_MOVIMENTO)) {
            movimento = (Movimento) intent.getSerializableExtra(ARG_MOVIMENTO);
        } else {
            movimento = new Movimento();
            if (intent != null && intent.hasExtra(ARG_MOVIMENTO_PAI)) {
                movimento.setMovimentoPai((Movimento) intent.getSerializableExtra(ARG_MOVIMENTO_PAI));
            }
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new MovimentoFragment();
                    break;
                case 1:
                    fragment = new MovimentoItemFragment();
                    break;
                default:
                    fragment = null;
                    break;
            }
            if (fragment != null) {
                Bundle args = new Bundle();
                args.putSerializable("movimento", movimento);
                fragment.setArguments(args);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            if (movimento != null && movimento.getId() != null) {
                return 2;
            } else {
                return 1;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.movimentos).toUpperCase(l);
                case 1:
                    return getString(R.string.produto).toUpperCase(l);
            }
            return null;
        }
    }

}
