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

import br.com.blockcells.blockcells.dao.HorarioDAO;
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
        getMenuInflater().inflate(R.menu.menu_confirma, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //captura a seleção dos menus superiores
        switch (item.getItemId()){
            case R.id.menu_confirma:
                Horario horario = helper.pegaHorario();
                HorarioDAO dao = new HorarioDAO(this);

                if (horario.getId() != null) {
                    //significa que está alterando

                    dao.altera(horario);
                }
                else {
                    dao.insere(horario);
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
