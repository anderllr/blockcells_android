package br.com.blockcells.blockcells.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import br.com.blockcells.blockcells.R;
import br.com.blockcells.blockcells.modelo.Mensagem;

/**
 * Created by anderson on 18/11/16.
 */

public class MensagemDAO {

    private final BlockCellsDAL dal;
    private final Context context;

    public MensagemDAO(Context context) {
        //Inicia a Data Access Layer
        dal = new BlockCellsDAL(context);
        this.context = context;
    }

    public void insere(Mensagem mensagem) {
        ContentValues dados = getContentValues(mensagem);

        dal.insere("Mensagem", dados);

    }

    @NonNull
    private ContentValues getContentValues(Mensagem mensagem) {
        ContentValues dados = new ContentValues();
        dados.put("mensagem", mensagem.getMsg());
        dados.put("mensagemVIP", mensagem.getMsg_vip());
        dados.put("localizacao", mensagem.isMsgcomlocalizacao());
        return dados;
    }

    public Mensagem buscaMensagem() {

        String sql = "SELECT * FROM Mensagem;";

        Cursor c = dal.buscaCursor(sql);

        //já aponta o cursor para a próxima pois ele sempre tras a primeira linha em branco
        c.moveToFirst();

        if (c.getCount() > 0) {
            Mensagem mensagem = new Mensagem();
            mensagem.setMsg(c.getString(c.getColumnIndex("mensagem")));
            mensagem.setMsg_vip(c.getString(c.getColumnIndex("mensagemVIP")));
            mensagem.setMsgcomlocalizacao(c.getInt(c.getColumnIndex("localizacao")) == 1);
            c.close();
            return mensagem;
        }
        else
            return null;
    }

    public void deleta(Mensagem mensagem) {

        String[] params = {"1"};

        dal.deletaID("Mensagem", params);
    }

    public void altera(Mensagem mensagem) {

        ContentValues dados = getContentValues(mensagem);

        String[] params = {"1"};
        dal.alteraID("Mensagem", dados, params);
    }

    public void close(){
        dal.close();
    }

    public void inserePrimeiro() {
        //Ever starts with any register for avoid any trouble
        Mensagem msg = new Mensagem();
        msg.setMsg(context.getString(R.string.msgDefaultSMS));
        msg.setMsg_vip(context.getString(R.string.msgDefaultSMSVIP));
        msg.setMsgcomlocalizacao(false);
        this.insere(msg);
    }
}
