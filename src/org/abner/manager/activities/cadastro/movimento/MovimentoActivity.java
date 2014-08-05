package org.abner.manager.activities.cadastro.movimento;

import java.math.BigDecimal;
import java.util.Locale;

import org.abner.manager.R;
import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.movimento.TipoMovimento;
import org.abner.manager.repository.movimento.MovimentoRepository;
import org.abner.manager.repository.movimento.dao.MovimentoDao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MovimentoActivity extends Activity {

    public static final String ARG_MOVIMENTO = "arg_movimento";

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

    Movimento movimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimento);

        Intent intent = getIntent();
        if (intent != null && intent.getSerializableExtra(ARG_MOVIMENTO) != null) {
            movimento = (Movimento) intent.getSerializableExtra(ARG_MOVIMENTO);
        } else {
            movimento = new Movimento();
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.control, menu);
        if (movimento.getId() == null) {
            MenuItem item = menu.findItem(R.id.action_delete);
            item.setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_save:
                updateMovimentoFromUI();

                if (movimento.getId() == null) {
                    if (movimento.getData() != null && movimento.getValor() != null) {
                        MovimentoRepository movimentoRepository = new MovimentoDao(this);
                        movimentoRepository.insert(movimento);
                        finish();
                    } else {
                        Toast.makeText(this, "Preencha o campo valor", Toast.LENGTH_LONG).show();
                    }
                } else {
                    MovimentoRepository movimentoRepository = new MovimentoDao(this);
                    movimentoRepository.update(movimento);
                    finish();
                }
                return true;
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setMessage("Deseja remover o movimento?");
                builder.setPositiveButton("Sim", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MovimentoRepository movimentoRepository = new MovimentoDao(MovimentoActivity.this);
                        movimentoRepository.remove(movimento);
                        finish();
                    }
                });
                builder.setNegativeButton("Não", null);
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMovimentoFromUI() {
        CheckBox chkCredito = (CheckBox) findViewById(R.id.movimento_credito);
        if (chkCredito.isChecked()) {
            movimento.setTipo(TipoMovimento.CREDITO);
        } else {
            movimento.setTipo(TipoMovimento.DEBITO);
        }

        EditText textValor = (EditText) findViewById(R.id.movimento_valor);
        try {
            movimento.setValor(new BigDecimal(textValor.getText().toString()));
        } catch (NumberFormatException e) {
            movimento.setValor(null);
        }
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
                case 2:
                    fragment = new SmsFragment();
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
                // Show 3 total pages.
                return 3;
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
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

}
