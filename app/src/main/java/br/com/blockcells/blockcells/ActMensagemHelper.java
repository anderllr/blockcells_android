package br.com.blockcells.blockcells;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import br.com.blockcells.blockcells.modelo.Mensagem;

/**
 * Created by anderson on 18/11/16.
 */

public class ActMensagemHelper {
    private final EditText campoMensagem;
    private final EditText campoMensagemVIP;
    private final CheckBox campoLocalizacao;

    private Mensagem mensagem;

    public ActMensagemHelper(ActMensagem activity){
        //Constructor já para trazer o valor do campo do formulario
        campoMensagem = (EditText) activity.findViewById(R.id.mensagem_mensagem);
        campoMensagemVIP = (EditText) activity.findViewById(R.id.mensagem_mensagemVIP);
        campoLocalizacao = (CheckBox) activity.findViewById(R.id.mensagem_localizacao);
    }

    public Mensagem pegaMensagem() {
        if (mensagem == null) {
            mensagem = new Mensagem();
        }
        mensagem.setMensagem(campoMensagem.getText().toString());
        mensagem.setDescmensagemVIP(campoMensagemVIP.getText().toString());
        mensagem.setLocalizacao(campoLocalizacao.isChecked());
        return mensagem;
    }

    public void preencheMensagem(Mensagem mensagem) {
        campoMensagem.setText(mensagem.getMensagem());
        campoMensagemVIP.setText(mensagem.getDescmensagemVIP());
        campoLocalizacao.setChecked(mensagem.isLocalizacao());

        this.mensagem = mensagem;

    }

}
