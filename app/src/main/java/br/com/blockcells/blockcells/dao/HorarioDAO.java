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
        dados.put("usefulMonday", horario.isUsefulMonday());
        dados.put("usefulTuesday", horario.isUsefulTuesday());
        dados.put("usefulWednesday", horario.isUsefulWednesday());
        dados.put("usefulThursday", horario.isUsefulThursday());
        dados.put("usefulFriday", horario.isUsefulFriday());
        dados.put("hourUsefulStart", horario.getHourUsefulStart());
        dados.put("hourUsefulEnd", horario.getHourUsefulEnd());
        dados.put("weekendSaturday", horario.isWeekendSaturday());
        dados.put("weekendSunday", horario.isWeekendSunday());
        dados.put("hourWeekendStart", horario.getHourWeekendStart());
        dados.put("hourWeekendEnd", horario.getHourWeekendEnd());
        return dados;
    }

    public Horario buscaHorario() {

        String sql = "SELECT * FROM Horario;";

        Cursor c = dal.buscaCursor(sql);

        //já aponta o cursor para a próxima pois ele sempre tras a primeira linha em branco
        c.moveToFirst();

        if (c.getCount() > 0) {
            Horario horario = new Horario();
            horario.setId(c.getLong(c.getColumnIndex("id")));
            horario.setUsefulMonday(c.getInt(c.getColumnIndex("usefulMonday")) == 1);
            horario.setUsefulTuesday(c.getInt(c.getColumnIndex("usefulTuesday")) == 1);
            horario.setUsefulWednesday(c.getInt(c.getColumnIndex("usefulWednesday")) == 1);
            horario.setUsefulThursday(c.getInt(c.getColumnIndex("usefulThursday")) == 1);
            horario.setUsefulFriday(c.getInt(c.getColumnIndex("usefulFriday")) == 1);
            horario.setHourUsefulStart(c.getString(c.getColumnIndex("hourUsefulStart")));
            horario.setHourUsefulEnd(c.getString(c.getColumnIndex("hourUsefulEnd")));
            horario.setWeekendSaturday(c.getInt(c.getColumnIndex("weekendSaturday")) == 1);
            horario.setWeekendSunday(c.getInt(c.getColumnIndex("weekendSunday")) == 1);
            horario.setHourWeekendStart(c.getString(c.getColumnIndex("hourWeekendStart")));
            horario.setHourWeekendEnd(c.getString(c.getColumnIndex("hourWeekendEnd")));
            c.close();
            return horario;
        }
        else
            return null;
    }

    public void deleta(Horario horario) {

        String[] params = {horario.getId().toString()};

        dal.deletaID("Horario", params);
    }

    public void altera(Horario horario) {

        ContentValues dados = getContentValues(horario);

        String[] params = {horario.getId().toString()};
        dal.alteraID("Horario", dados, params);
    }

    public void close(){
        dal.close();
    }

    public void inserePrimeiro() {
        //No caso dessa tela de configuração sempre iniciará com um registro e ele sempre será editado
        Horario horario = new Horario();
        horario.setUsefulMonday(true);
        horario.setUsefulTuesday(true);
        horario.setUsefulWednesday(true);
        horario.setUsefulThursday(true);
        horario.setUsefulFriday(true);
        horario.setHourUsefulStart("00:00");
        horario.setHourUsefulEnd("23:59");
        horario.setWeekendSaturday(true);
        horario.setWeekendSunday(true);
        horario.setHourWeekendStart("00:00");
        horario.setHourWeekendEnd("23:59");
        this.insere(horario);
    }
}
