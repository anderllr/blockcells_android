package br.com.blockcells.blockcells;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import br.com.blockcells.blockcells.dao.BlockCellsFire;
import br.com.blockcells.blockcells.dao.ConfigGeralDAO;
import br.com.blockcells.blockcells.dao.HorarioDAO;
import br.com.blockcells.blockcells.modelo.ConfigGeral;
import br.com.blockcells.blockcells.modelo.Horario;

public class ActHorario extends AppCompatActivity {

    private ActHorarioHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        //Para utilizar a classe é necessário criar o helper
        helper = new ActHorarioHelper(ActHorario.this);

        //Pega a intent que foi usada para abrir o formulário
        Intent intent = getIntent();
        Horario horario = (Horario) intent.getSerializableExtra("horario");

        if (horario != null){
            helper.preencheHorario(horario);

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
                Horario horario = helper.pegaHorario();
                HorarioDAO dao = new HorarioDAO(this);

                //Save on firebase
                BlockCellsFire fire = new BlockCellsFire(getApplicationContext());
                fire.salvaFirebase(horario, "horario");

                dao.altera(horario);
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
