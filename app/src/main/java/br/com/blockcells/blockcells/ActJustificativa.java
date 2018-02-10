package br.com.blockcells.blockcells;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.blockcells.blockcells.dao.BlockCellsFire;
import br.com.blockcells.blockcells.dao.JustificativaDAO;
import br.com.blockcells.blockcells.modelo.Justificativa;

public class ActJustificativa extends AppCompatActivity {

    private ActJustificativaHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_justificativa);

        //Para utilizar a classe é necessário criar o helper
        helper = new ActJustificativaHelper(this);

        //Pega a intent que foi usada para abrir o formulário
        Intent intent = getIntent();
        Justificativa jus = (Justificativa) intent.getSerializableExtra("justificativa");

        if (jus != null){
            helper.preencheJustificativa(jus);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //método para carregar na tela o layout xml criado para os menus superiores
        getMenuInflater().inflate(R.menu.menu_confirma, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //captura a seleção dos menus superiores
        switch (item.getItemId()){
            case R.id.menu_confirma:
                Justificativa jus = helper.pegaJustificativa();

                //Set justificado = true for mark as done
                jus.setJustificado(true);

                BlockCellsFire fire = new BlockCellsFire(getApplicationContext());
                fire.salvaFirebaseChild(jus, "justificativa", String.valueOf(jus.getId()));

                Toast.makeText(this, getString(R.string.action_salvar), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.menu_voltar:
                Toast.makeText(this, getString(R.string.action_sair), Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
