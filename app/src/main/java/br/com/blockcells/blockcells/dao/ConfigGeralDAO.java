package br.com.blockcells.blockcells.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import br.com.blockcells.blockcells.modelo.ConfigGeral;

/**
 * Created by anderson on 17/11/16.
 */

public class ConfigGeralDAO {

    private final BlockCellsDAL dal;

    public ConfigGeralDAO(Context context) {
        //Inicia a Data Access Layer
        dal = new BlockCellsDAL(context);
    }

    public void insere(ConfigGeral configGeral) {

        ContentValues dados = getContentValues(configGeral);

        dal.insere("ConfigGeral", dados);

    }

    @NonNull
    private ContentValues getContentValues(ConfigGeral configGeral) {
        ContentValues dados = new ContentValues();
        dados.put("controleRemoto", configGeral.getControleRemoto());
        dados.put("informaLocal", configGeral.getInformaLocal());
        dados.put("ativado", configGeral.getAtivado());
        dados.put("enviasms", configGeral.isEnviasms());
        return dados;
    }

    public ConfigGeral buscaConfigGeral() {
        //Como sempre haverá apenas um registro da configuração então não será necessário lista

        String sql = "SELECT * FROM ConfigGeral;";
        Cursor c = dal.buscaCursor(sql);

        //dal.close();

        //já aponta o cursor para a próxima pois ele sempre tras a primeira linha em branco
        c.moveToFirst();

        if (c.getCount() > 0) {
            ConfigGeral configGeral = new ConfigGeral();
            configGeral.setId(c.getLong(c.getColumnIndex("id")));
            configGeral.setControleRemoto(c.getInt(c.getColumnIndex("controleRemoto")) == 1);
            configGeral.setInformaLocal(c.getInt(c.getColumnIndex("informaLocal")) == 1);
            configGeral.setAtivado(c.getInt(c.getColumnIndex("ativado")) == 1);
            configGeral.setEnviasms(c.getInt(c.getColumnIndex("enviasms")) == 1);
            c.close();
            return configGeral;
        }
        else
            return null;
    }

    public void deleta(ConfigGeral configGeral) {

        String[] params = {configGeral.getId().toString()};

        dal.deletaID("ConfigGeral", params);

        //dal.close();
    }

    public void altera(ConfigGeral configGeral) {

        ContentValues dados = getContentValues(configGeral);

        String[] params = {configGeral.getId().toString()};
        dal.alteraID("ConfigGeral", dados, params);

        //dal.close();
    }

    public void inserePrimeiro() {
        //No caso dessa tela de configuração sempre iniciará com um registro e ele sempre será editado
        ConfigGeral configGeral = new ConfigGeral();
        configGeral.setAtivado(false);
        configGeral.setControleRemoto(false);
        configGeral.setInformaLocal(false);
        configGeral.setEnviasms(false);
        this.insere(configGeral);
    }

    public void close(){
        dal.close();
    }

}
