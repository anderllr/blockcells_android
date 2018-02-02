package br.com.blockcells.blockcells;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.blockcells.blockcells.dao.ConfigGeralDAO;
import br.com.blockcells.blockcells.modelo.ConfigGeral;

public class ActConfigGeral extends AppCompatActivity {

    private ActConfigGeralHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_geral);

        //Para utilizar a classe é necessário criar o helper
        helper = new ActConfigGeralHelper(this);

        //Pega a intent que foi usada para abrir o formulário
        Intent intent = getIntent();
        ConfigGeral configGeral = (ConfigGeral) intent.getSerializableExtra("configGeral");

        if (configGeral != null){
            helper.preencheConfigGeral(configGeral);

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
                ConfigGeral configGeral = helper.pegaConfigGeral();
                ConfigGeralDAO dao = new ConfigGeralDAO(this);

                if (configGeral.getId() != null) {
                    //significa que está alterando

                    dao.altera(configGeral);
                }
                else {
                    dao.insere(configGeral);
                }
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
