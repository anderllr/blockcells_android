package br.com.blockcells.blockcells.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.blockcells.blockcells.modelo.Justificativa;
import br.com.blockcells.blockcells.modelo.LogGeral;

/**
 * Created by anderson on 23/01/2018.
 */

public class JustificativaDAO {

    private final BlockCellsDAL dal;
    private final Context context;

    public JustificativaDAO(Context context) {
        //Inicia a Data Access Layer
        this.context = context;
        dal = new BlockCellsDAL(context);
    }

    public void insere(Justificativa just) {

        ContentValues dados = getContentValues(just);

        dal.insere("Justificativa", dados);

    }

    @NonNull
    private ContentValues getContentValues(Justificativa just) {
        ContentValues dados = new ContentValues();
        dados.put("data_hora", just.getData_hora());
        dados.put("desc_justificativa", just.getDesc_justificativa());
        dados.put("evento", just.getEvento());
        dados.put("justificado", just.isJustificado());
        dados.put("latitude", just.getLatitude());
        dados.put("longitude", just.getLongitude());
        return dados;
    }

    public List<Justificativa> buscaJustificativa() {
        //Como sempre haverá apenas um registro da configuração então não será necessário lista

        String sql = "SELECT * FROM Justificativa ORDER BY data_hora DESC;";
        Cursor c = dal.buscaCursor(sql);

        List<Justificativa> juslista = new ArrayList<Justificativa>();
        //já aponta o cursor para a próxima pois ele sempre tras a primeira linha em branco
        while (c.moveToNext()){
            Justificativa just = new Justificativa();
            just.setId(c.getLong(c.getColumnIndex("id")));
            just.setData_hora(c.getString(c.getColumnIndex("data_hora")));
            just.setDesc_justificativa(c.getString(c.getColumnIndex("desc_justificativa")));
            just.setEvento(c.getString(c.getColumnIndex("evento")));
            just.setJustificado(c.getInt(c.getColumnIndex("justificado")) == 1);
            just.setLatitude(c.getDouble(c.getColumnIndex("latitude")));
            just.setLongitude(c.getDouble(c.getColumnIndex("longitude")));
            //Agora adiciona na lista
            juslista.add(just);

        }
        c.close();
        return juslista;

    }

    public void deleta(Justificativa just) {

        String[] params = {just.getId().toString()};

        dal.deletaID("Justificativa", params);

        //dal.close();
    }

    public void altera(Justificativa just) {

        ContentValues dados = getContentValues(just);

        String[] params = {just.getId().toString()};
        dal.alteraID("Justificativa", dados, params);

        //dal.close();
    }

    public void close(){
        dal.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void insereJustificativa (LogGeral loggeral, double latitude, double longitude){
        Justificativa just = new Justificativa();
        just.setData_hora(loggeral.getData_hora());
        just.setDesc_justificativa("");
        just.setEvento(loggeral.getEvento());
        just.setJustificado(false);
        just.setLatitude(latitude);
        just.setLongitude(longitude);

        BlockCellsFire fire = new BlockCellsFire(context);
        fire.salvaJustificativa(just);
 //       insere(just);
    }
}
