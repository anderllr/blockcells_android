package br.com.blockcells.blockcells.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.blockcells.blockcells.R;
import br.com.blockcells.blockcells.modelo.ContatosExcecao;

/**
 * Created by anderson on 27/01/17.
 */

public class ContatosExcecaoDAO {


    private final BlockCellsDAL dal;
    private final Context context;

    public ContatosExcecaoDAO(Context context) {
        //Inicia a Data Access Layer
        this.context = context;
        dal = new BlockCellsDAL(context);
    }

    public void insere(ContatosExcecao contatosExcecao) {

        ContentValues dados = getContentValues(contatosExcecao);

        dal.insere("ContatosExcecao", dados);

    }

    public Long getMax() {
        return dal.buscaMax("ContatosExcecao", "id");
    }

    @NonNull
    private ContentValues getContentValues(ContatosExcecao ContatosExcecao) {
        ContentValues dados = new ContentValues();
        dados.put("id", ContatosExcecao.getId());
        dados.put("nome", ContatosExcecao.getNome());
        dados.put("fone", ContatosExcecao.getFone());
        dados.put("foneNormalize", ContatosExcecao.getFoneNormalize());

        byte[] bFoto = null;
        if (ContatosExcecao.getFoto() != null) {
            bFoto = getBytes(ContatosExcecao.getFoto());
        }

        dados.put("foto", bFoto);
        return dados;
    }

    public List<ContatosExcecao> buscaContatosExcecao() {
        //Como sempre haverá apenas um registro da configuração então não será necessário lista

        String sql = "SELECT * FROM ContatosExcecao;";
        Cursor c = dal.buscaCursor(sql);

        List<ContatosExcecao> contatos = new ArrayList<ContatosExcecao>();
        //já aponta o cursor para a próxima pois ele sempre tras a primeira linha em branco
        while (c.moveToNext()){
            ContatosExcecao ContatosExcecao = new ContatosExcecao();
            ContatosExcecao.setId(c.getLong(c.getColumnIndex("id")));
            ContatosExcecao.setNome(c.getString(c.getColumnIndex("nome")));
            ContatosExcecao.setFone(c.getString(c.getColumnIndex("fone")));
            ContatosExcecao.setFoneNormalize(c.getString(c.getColumnIndex("foneNormalize")));
            ContatosExcecao.setFoto(getImage(c.getBlob(c.getColumnIndex("foto"))));
            //Agora adiciona na lista
            contatos.add(ContatosExcecao);

        }
        c.close();
        return contatos;

    }

    public void deleta(ContatosExcecao ce) {

        String[] params = {ce.getId().toString()};

        dal.deletaID("ContatosExcecao", params);

        //dal.close();
    }

    public void deleteAll() {

        dal.deletaAll("ContatosExcecao");
    }

    public void altera(ContatosExcecao ContatosExcecao) {

        ContentValues dados = getContentValues(ContatosExcecao);

        String[] params = {ContatosExcecao.getId().toString()};
        dal.alteraID("ContatosExcecao", dados, params);

        //dal.close();
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        if (image != null) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }
        else
            return null;
    }

    public boolean isContatoExcecao (String number) {
        //Pesquisa a primeira vez pelo número normal
        String sql = "SELECT * FROM ContatosExcecao;";
        Cursor c = dal.buscaCursor(sql);

        PhoneNumberUtils pn = new PhoneNumberUtils();

        boolean exist = false;

        List<ContatosExcecao> contatos = new ArrayList<ContatosExcecao>();
        //já aponta o cursor para a próxima pois ele sempre tras a primeira linha em branco
        while (c.moveToNext()) {
            if (!exist) {
                exist = pn.compare(context, number, c.getString(c.getColumnIndex("fone")));
                //         Log.i("VIP", "Número: " + number + "  - Contato: " + c.getString(c.getColumnIndex("fone")) + String.valueOf(exist));
            }

        }
        c.close();
    /*    String sql = "SELECT * FROM ContatosExcecao WHERE fone = " + number;
        Cursor c = dal.buscaCursor(sql);
        int res = c.getCount();
        c.close();

        //se não encontrou ai pega pelo número considerando o ddd imaginando ser um celular de 9 dígitos mais 2 do ddd
        if (res == 0) {
        return res > 0;
        } */

       return exist;
    }

    public void close(){
        dal.close();
    }


}
