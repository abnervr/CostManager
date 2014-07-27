package org.abner.manager.activities.cadastro.movimentoitem;

import java.math.BigDecimal;

import org.abner.manager.R;
import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.movimento.MovimentoItem;
import org.abner.manager.repository.movimento.MovimentoItemRepository;
import org.abner.manager.repository.movimento.dao.MovimentoItemDao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MovimentoItemActivity extends Activity {

    public static final String ARG_MOVIMENTO = "arg_movimento";
    public static final String ARG_MOVIMENTO_ITEM = "arg_movimento_item";

    private MovimentoItem movimentoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimento_item);

        Intent intent = getIntent();
        if (intent != null && intent.getSerializableExtra(ARG_MOVIMENTO_ITEM) != null) {
            movimentoItem = (MovimentoItem) intent.getSerializableExtra(ARG_MOVIMENTO_ITEM);
        } else {
            movimentoItem = new MovimentoItem();

            if (intent != null && intent.getSerializableExtra(ARG_MOVIMENTO) != null) {
                Movimento movimento = (Movimento) intent.getSerializableExtra(ARG_MOVIMENTO);
                movimentoItem.setMovimento(movimento);
            } else {
                throw new RuntimeException("Movimento não especificado");
            }
        }

        Bundle args = new Bundle();
        args.putSerializable("movimentoItem", movimentoItem);

        Fragment fragment = new MovimentoItemFragment();
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().add(R.id.movimento_item_content, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.control, menu);
        if (movimentoItem.getId() == null) {
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
                if (movimentoItem.getId() == null) {
                    if (movimentoItem.getValor() != null) {
                        MovimentoItemRepository itemRepository = new MovimentoItemDao(this);
                        itemRepository.insert(movimentoItem);
                        finish();
                    } else {
                        Toast.makeText(this, "Preencha o campo valor", Toast.LENGTH_LONG).show();
                    }
                } else {
                    MovimentoItemRepository itemRepository = new MovimentoItemDao(this);
                    itemRepository.update(movimentoItem);
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
                        MovimentoItemRepository itemRepository = new MovimentoItemDao(MovimentoItemActivity.this);
                        itemRepository.remove(movimentoItem);
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
        EditText textValor = (EditText) findViewById(R.id.movimento_valor);
        try {
            movimentoItem.setValor(new BigDecimal(textValor.getText().toString()));
        } catch (NumberFormatException e) {
            movimentoItem.setValor(null);
        }
    }

}
