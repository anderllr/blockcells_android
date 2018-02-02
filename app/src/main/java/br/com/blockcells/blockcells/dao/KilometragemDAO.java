package br.com.blockcells.blockcells.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import br.com.blockcells.blockcells.modelo.Kilometragem;

/**
 * Created by anderson on 26/11/16.
 */

public class KilometragemDAO {

    private final BlockCellsDAL dal;

    public KilometragemDAO(Context context) {
        //Inicia a Data Access Layer
        dal = new BlockCellsDAL(context);
    }

    public void insere(Kilometragem km) {
        ContentValues dados = getContentValues(km);

        dal.insere("Kilometragem", dados);

    }

    @NonNull
    private ContentValues getContentValues(Kilometragem km) {
        ContentValues dados = new ContentValues();
        dados.put("kmMinimo", km.getKmMinimo());
        dados.put("kmMaximo", km.getKmMaximo());
        dados.put("kmAlerta", km.getKmAlerta());
        dados.put("ativarBip", km.isAtivarBip());
        dados.put("enviarAlerta", km.isEnviarAlerta());

        return dados;
    }

    public Kilometragem buscaKilometragem() {

        String sql = "SELECT * FROM Kilometragem;";

        Cursor c = dal.buscaCursor(sql);

        //já aponta o cursor para a próxima pois ele sempre tras a primeira linha em branco
        c.moveToFirst();

        if (c.getCount() > 0) {
            Kilometragem km = new Kilometragem();
            km.setId(c.getLong(c.getColumnIndex("id")));
            km.setKmMinimo(c.getInt(c.getColumnIndex("kmMinimo")));
            km.setKmMaximo(c.getInt(c.getColumnIndex("kmMaximo")));
            km.setKmAlerta(c.getInt(c.getColumnIndex("kmAlerta")));
            km.setAtivarBip(c.getInt(c.getColumnIndex("ativarBip")) == 1);
            km.setEnviarAlerta(c.getInt(c.getColumnIndex("enviarAlerta")) == 1);
            c.close();
            return km;
        }
        else
            return null;
    }

    public void deleta(Kilometragem km) {

        String[] params = {km.getId().toString()};

        dal.deletaID("Kilometragem", params);
    }

    public void altera(Kilometragem km) {
        ContentValues dados = getContentValues(km);

        String[] params = {km.getId().toString()};
        dal.alteraID("Kilometragem", dados, params);
    }

    public void inserePrimeiro() {
        //No caso dessa tela de configuração sempre iniciará com um registro e ele sempre será editado
        Kilometragem km = new Kilometragem();
        km.setKmMinimo(20);
        km.setKmMaximo(110);
        km.setKmAlerta(95);
        km.setAtivarBip(false);
        km.setEnviarAlerta(false);
        this.insere(km);
    }

    public void close(){
        dal.close();
    }
}
