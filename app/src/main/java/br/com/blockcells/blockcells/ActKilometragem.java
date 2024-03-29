package br.com.blockcells.blockcells;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.blockcells.blockcells.dao.BlockCellsFire;
import br.com.blockcells.blockcells.dao.ConfigGeralDAO;
import br.com.blockcells.blockcells.dao.KilometragemDAO;
import br.com.blockcells.blockcells.modelo.ConfigGeral;
import br.com.blockcells.blockcells.modelo.Kilometragem;

public class ActKilometragem extends AppCompatActivity {

    private ActKilometragemHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kilometragem);

        //Para utilizar a classe é necessário criar o helper
        helper = new ActKilometragemHelper(this);

        //Pega a intent que foi usada para abrir o formulário
        Intent intent = getIntent();
        Kilometragem km = (Kilometragem) intent.getSerializableExtra("km");

        if (km != null){
            helper.preencheKilometragem(km);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //método para carregar na tela o layout xml criado para os menus superiores
        //Verifica se está marcado para apenas controle remoto
        ConfigGeralDAO dao = new ConfigGeralDAO(this);
        ConfigGeral cfg = dao.buscaConfigGeral();
        if (cfg.getControle_remoto()) {
            getMenuInflater().inflate(R.menu.menu_voltar, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_confirma, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //captura a seleção dos menus superiores
        switch (item.getItemId()){
            case R.id.menu_confirma:
                Kilometragem km = helper.pegaKilometragem();
                KilometragemDAO dao = new KilometragemDAO(this);

                //Save on firebase
                BlockCellsFire fire = new BlockCellsFire(getApplicationContext());
                fire.salvaFirebase(km, "km");

                dao.altera(km);
                dao.close();
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
