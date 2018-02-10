package br.com.blockcells.blockcells;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.blockcells.blockcells.adapter.LogGeralAdapter;
import br.com.blockcells.blockcells.dao.LogGeralDAO;
import br.com.blockcells.blockcells.funcs.GlobalSpeed;
import br.com.blockcells.blockcells.modelo.LogGeral;

public class ActLogGeral extends AppCompatActivity {

    private ListView listaLog;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("celulares");

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
//        LogGeralDAO dao = new LogGeralDAO(this);
//        List<LogGeral> lista = dao.buscaLogGeral();
//        dao.close();
        final List<LogGeral> lista = new ArrayList<LogGeral>();

        final LogGeralAdapter adapter = new LogGeralAdapter(this, lista);
        listaLog.setAdapter(adapter);

        //Agora cria o listener que irá ler os logs
        final GlobalSpeed globalSpeed = (GlobalSpeed) getApplicationContext();
        DatabaseReference fireLog = myRef.child(globalSpeed.getTelefone()).child("log_eventos");

        fireLog.limitToLast(30).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lista.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()) {
                    LogGeral lg = dados.getValue(LogGeral.class);
                    lista.add(lg);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
