package br.com.blockcells.blockcells;

import android.widget.CheckBox;
import android.widget.EditText;

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
        mensagem.setMsg(campoMensagem.getText().toString());
        mensagem.setMsg_vip(campoMensagemVIP.getText().toString());
        mensagem.setMsgcomlocalizacao(campoLocalizacao.isChecked());
        return mensagem;
    }

    public void preencheMensagem(Mensagem mensagem) {
        campoMensagem.setText(mensagem.getMsg());
        campoMensagemVIP.setText(mensagem.getMsg_vip());
        campoLocalizacao.setChecked(mensagem.isMsgcomlocalizacao());

        this.mensagem = mensagem;

    }

}
