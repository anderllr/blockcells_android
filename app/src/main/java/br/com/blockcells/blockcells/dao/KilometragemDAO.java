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
        dados.put("kmMinimo", km.getVelocidade_min());
        dados.put("kmMaximo", km.getVelocidade_max());
        dados.put("kmAlerta", km.getVelocidade_alerta());
        dados.put("ativarBip", km.isEmite_bip());
        dados.put("enviarAlerta", km.isEnvia_alerta());

        return dados;
    }

    public Kilometragem buscaKilometragem() {

        String sql = "SELECT * FROM Kilometragem;";

        Cursor c = dal.buscaCursor(sql);

        //já aponta o cursor para a próxima pois ele sempre tras a primeira linha em branco
        c.moveToFirst();

        if (c.getCount() > 0) {
            Kilometragem km = new Kilometragem();
            km.setVelocidade_min(c.getInt(c.getColumnIndex("kmMinimo")));
            km.setVelocidade_max(c.getInt(c.getColumnIndex("kmMaximo")));
            km.setVelocidade_alerta(c.getInt(c.getColumnIndex("kmAlerta")));
            km.setEmite_bip(c.getInt(c.getColumnIndex("ativarBip")) == 1);
            km.setEnvia_alerta(c.getInt(c.getColumnIndex("enviarAlerta")) == 1);
            c.close();
            return km;
        }
        else
            return null;
    }

    public void deleta(Kilometragem km) {

        String[] params = {"1"};

        dal.deletaID("Kilometragem", params);
    }

    public void altera(Kilometragem km) {
        ContentValues dados = getContentValues(km);

        String[] params = {"1"};
        dal.alteraID("Kilometragem", dados, params);
    }

    public void inserePrimeiro() {
        //No caso dessa tela de configuração sempre iniciará com um registro e ele sempre será editado
        Kilometragem km = new Kilometragem();
        km.setVelocidade_min(20);
        km.setVelocidade_max(110);
        km.setVelocidade_alerta(95);
        km.setEmite_bip(false);
        km.setEnvia_alerta(false);
        this.insere(km);
    }

    public void close(){
        dal.close();
    }
}
