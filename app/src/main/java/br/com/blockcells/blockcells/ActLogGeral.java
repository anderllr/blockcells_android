package br.com.blockcells.blockcells;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import br.com.blockcells.blockcells.adapter.LogGeralAdapter;
import br.com.blockcells.blockcells.dao.LogGeralDAO;
import br.com.blockcells.blockcells.modelo.LogGeral;

public class ActLogGeral extends AppCompatActivity {

    private ListView listaLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_geral);

        listaLog = (ListView) findViewById(R.id.lista_log);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //infla ele
        getMenuInflater().inflate(R.menu.menu_voltar, menu);

        return true;
    }

    //Para ter o comportamento do clique sobrescreve o método abaixo
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_voltar:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void carregaLista() {
        LogGeralDAO dao = new LogGeralDAO(this);
        List<LogGeral> lista = dao.buscaLogGeral();
        dao.close();

        LogGeralAdapter adapter = new LogGeralAdapter(this, lista);
        listaLog.setAdapter(adapter);

    }
}
