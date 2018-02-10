package br.com.blockcells.blockcells.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.blockcells.blockcells.modelo.LogGeral;

/**
 * Created by anderson on 28/02/17.
 */

public class LogGeralDAO {

    private final BlockCellsDAL dal;
    private final Context context;

    public LogGeralDAO(Context context) {
        //Inicia a Data Access Layer
        this.context = context;
        dal = new BlockCellsDAL(context);
    }

    public void insere(LogGeral LogGeral) {

        ContentValues dados = getContentValues(LogGeral);

        dal.insere("LogGeral", dados);

    }

    @NonNull
    private ContentValues getContentValues(LogGeral logGeral) {
        ContentValues dados = new ContentValues();
        dados.put("topico", logGeral.getEvento());
        dados.put("ocorrencia", logGeral.getDescricao());
        dados.put("dataHora", getDateTime());
        dados.put("latitude", logGeral.getLatitude());
        dados.put("longitude", logGeral.getLongitude());

        return dados;
    }

    public List<LogGeral> buscaLogGeral() {
        //Como sempre haverá apenas um registro da configuração então não será necessário lista

        String sql = "SELECT * FROM LogGeral ORDER BY dataHora DESC;";
        Cursor c = dal.buscaCursor(sql);

        List<LogGeral> loglista = new ArrayList<LogGeral>();
        //já aponta o cursor para a próxima pois ele sempre tras a primeira linha em branco
        while (c.moveToNext()){
            LogGeral loggeral = new LogGeral();
            loggeral.setId(c.getLong(c.getColumnIndex("id")));
            loggeral.setEvento(c.getString(c.getColumnIndex("topico")));
            loggeral.setDescricao(c.getString(c.getColumnIndex("ocorrencia")));
            loggeral.setData_hora(c.getString(c.getColumnIndex("dataHora")));
            loggeral.setLatitude(c.getDouble(c.getColumnIndex("latitude")));
            loggeral.setLongitude(c.getDouble(c.getColumnIndex("longitude")));
            //Agora adiciona na lista
            loglista.add(loggeral);

        }
        c.close();
        return loglista;

    }

    public void deleta(LogGeral ce) {

        String[] params = {ce.getId().toString()};

        dal.deletaID("LogGeral", params);

        //dal.close();
    }

    public void altera(LogGeral LogGeral) {

        ContentValues dados = getContentValues(LogGeral);

        String[] params = {LogGeral.getId().toString()};
        dal.alteraID("LogGeral", dados, params);

        //dal.close();
    }

    public void close(){
        dal.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void insereLog (String evento, String descricao, Double latitude, Double longitude){
        LogGeral loggeral = new LogGeral();
        loggeral.setEvento(evento);
        loggeral.setDescricao(descricao);
        loggeral.setLatitude(latitude);
        loggeral.setLongitude(longitude);
        loggeral.setData_hora(getDateTime());

        BlockCellsFire fire = new BlockCellsFire(context);
        fire.salvaLog(loggeral);
    //    insere(loggeral);

        JustificativaDAO jDao = new JustificativaDAO(this.context);
        String[] eventos = {"Excesso de Velocidade", "Efetuou Ligação", "Atendeu Ligação"};
        if (Arrays.asList(eventos).contains(loggeral.getEvento())) {
            jDao.insereJustificativa(loggeral, latitude, longitude);
        }
    }
}
