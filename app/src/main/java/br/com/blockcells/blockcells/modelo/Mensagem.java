package br.com.blockcells.blockcells.modelo;

import java.io.Serializable;

/**
 * Created by anderson on 18/11/16.
 */

public class Mensagem implements Serializable {
    private String msg;
    private String msg_vip;
    private boolean msgcomlocalizacao;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String descmensagem) {
        this.msg = descmensagem;
    }

    public String getMsg_vip() {
        return msg_vip;
    }

    public void setMsg_vip(String msg_vip) {
        this.msg_vip = msg_vip;
    }

    public boolean isMsgcomlocalizacao() {
        return msgcomlocalizacao;
    }

    public void setMsgcomlocalizacao(boolean msgcomlocalizacao) {
        this.msgcomlocalizacao = msgcomlocalizacao;
    }
}
