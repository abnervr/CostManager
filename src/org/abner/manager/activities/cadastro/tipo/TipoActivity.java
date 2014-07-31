package org.abner.manager.activities.cadastro.tipo;

import org.abner.manager.R;
import org.abner.manager.repository.empresa.dao.TipoDAO;

import android.app.ListActivity;
import android.os.Bundle;

public class TipoActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo);

        setListAdapter(new TipoAdapter(this, new TipoDAO(this).find()));
    }
}
