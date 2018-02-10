package br.com.blockcells.blockcells;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.blockcells.blockcells.adapter.JustificativaAdapter;
import br.com.blockcells.blockcells.dao.JustificativaDAO;
import br.com.blockcells.blockcells.funcs.GlobalSpeed;
import br.com.blockcells.blockcells.modelo.Justificativa;

public class ActListaJustificativa extends AppCompatActivity {

    private ListView listaJus;
    private List<Justificativa> listaDados;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("celulares");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_justificativa);

        listaJus = (ListView) findViewById(R.id.lista_justificativa);

        listaJus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long l) {

                Justificativa jus = (Justificativa) listaJus.getItemAtPosition(position);

               Intent intentVaiParaFormulario = new Intent(ActListaJustificativa.this, ActJustificativa.class);
               intentVaiParaFormulario.putExtra("justificativa", jus);
               startActivity(intentVaiParaFormulario);

            }
        });
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
     /*   JustificativaDAO dao = new JustificativaDAO(this);
        listaDados = dao.openListeners();
        dao.close();
*/
        final List<Justificativa> lista = new ArrayList<>();

        final JustificativaAdapter adapter = new JustificativaAdapter(this, lista);
        listaJus.setAdapter(adapter);

        //Agora cria o listener que irá ler os logs
        final GlobalSpeed globalSpeed = (GlobalSpeed) getApplicationContext();
        DatabaseReference fireLog = myRef.child(globalSpeed.getTelefone()).child("justificativa");

        fireLog.orderByChild("justificado").equalTo(false).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lista.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()) {
                    Justificativa jus = dados.getValue(Justificativa.class);
                    lista.add(jus);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
