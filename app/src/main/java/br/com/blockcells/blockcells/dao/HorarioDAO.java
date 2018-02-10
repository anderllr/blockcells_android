package br.com.blockcells.blockcells.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import br.com.blockcells.blockcells.modelo.Horario;

/**
 * Created by anderson on 09/12/16.
 */

public class HorarioDAO {

    private final BlockCellsDAL dal;

    public HorarioDAO(Context context) {
        //Inicia a Data Access Layer
        dal = new BlockCellsDAL(context);
    }

    public void insere(Horario horario) {
        ContentValues dados = getContentValues(horario);

        dal.insere("Horario", dados);

    }

    @NonNull
    private ContentValues getContentValues(Horario horario) {
        ContentValues dados = new ContentValues();
        dados.put("usefulMonday", horario.isSegunda());
        dados.put("usefulTuesday", horario.isTerca());
        dados.put("usefulWednesday", horario.isQuarta());
        dados.put("usefulThursday", horario.isQuinta());
        dados.put("usefulFriday", horario.isSexta());
        dados.put("hourUsefulStart", horario.getUtil_inicio());
        dados.put("hourUsefulEnd", horario.getUtil_fim());
        dados.put("weekendSaturday", horario.isSabado());
        dados.put("weekendSunday", horario.isDomingo());
        dados.put("hourWeekendStart", horario.getFds_inicio());
        dados.put("hourWeekendEnd", horario.getFds_fim());
        return dados;
    }

    public Horario buscaHorario() {

        String sql = "SELECT * FROM Horario;";

        Cursor c = dal.buscaCursor(sql);

        //já aponta o cursor para a próxima pois ele sempre tras a primeira linha em branco
        c.moveToFirst();

        if (c.getCount() > 0) {
            Horario horario = new Horario();
            horario.setSegunda(c.getInt(c.getColumnIndex("usefulMonday")) == 1);
            horario.setTerca(c.getInt(c.getColumnIndex("usefulTuesday")) == 1);
            horario.setQuarta(c.getInt(c.getColumnIndex("usefulWednesday")) == 1);
            horario.setQuinta(c.getInt(c.getColumnIndex("usefulThursday")) == 1);
            horario.setSexta(c.getInt(c.getColumnIndex("usefulFriday")) == 1);
            horario.setUtil_inicio(c.getString(c.getColumnIndex("hourUsefulStart")));
            horario.setUtil_fim(c.getString(c.getColumnIndex("hourUsefulEnd")));
            horario.setSabado(c.getInt(c.getColumnIndex("weekendSaturday")) == 1);
            horario.setDomingo(c.getInt(c.getColumnIndex("weekendSunday")) == 1);
            horario.setFds_inicio(c.getString(c.getColumnIndex("hourWeekendStart")));
            horario.setFds_fim(c.getString(c.getColumnIndex("hourWeekendEnd")));
            c.close();
            return horario;
        }
        else
            return null;
    }

    public void deleta(Horario horario) {

        String[] params = {"1"};

        dal.deletaID("Horario", params);
    }

    public void altera(Horario horario) {

        ContentValues dados = getContentValues(horario);

        String[] params = {"1"};
        dal.alteraID("Horario", dados, params);
    }

    public void close(){
        dal.close();
    }

    public void inserePrimeiro() {
        //No caso dessa tela de configuração sempre iniciará com um registro e ele sempre será editado
        Horario horario = new Horario();
        horario.setSegunda(true);
        horario.setTerca(true);
        horario.setQuarta(true);
        horario.setQuinta(true);
        horario.setSexta(true);
        horario.setUtil_inicio("00:00");
        horario.setUtil_fim("23:59");
        horario.setSabado(true);
        horario.setDomingo(true);
        horario.setFds_inicio("00:00");
        horario.setFds_fim("23:59");
        this.insere(horario);
    }
}
