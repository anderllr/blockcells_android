package br.com.blockcells.blockcells;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.blockcells.blockcells.dao.MensagemDAO;
import br.com.blockcells.blockcells.modelo.Mensagem;

public class ActMensagem extends AppCompatActivity {

    private ActMensagemHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        //Para utilizar a classe é necessário criar o helper
        helper = new ActMensagemHelper(ActMensagem.this);

        //Pega a intent que foi usada para abrir o formulário
        Intent intent = getIntent();
        Mensagem mensagem = (Mensagem) intent.getSerializableExtra("mensagem");

        if (mensagem != null){
            helper.preencheMensagem(mensagem);
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
                Mensagem mensagem = helper.pegaMensagem();
                MensagemDAO dao = new MensagemDAO(this);

                if (mensagem.getId() != null) {
                    //significa que está alterando
                    dao.altera(mensagem);
                }
                else {
                    dao.insere(mensagem);
                }
                dao.close();
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
