package br.com.blockcells.blockcells;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import br.com.blockcells.blockcells.funcs.DialogDateTime;
import br.com.blockcells.blockcells.modelo.Horario;

/**
 * Created by anderson on 02/12/16.
 */

public class ActHorarioHelper {
    private final CheckBox campoMonday;
    private final CheckBox campoTuesday;
    private final CheckBox campoWednesday;
    private final CheckBox campoThursday;
    private final CheckBox campoFriday;
    private final EditText campoUsefulStart;
    private final EditText campoUsefulEnd;
    private final CheckBox campoSaturday;
    private final CheckBox campoSunday;
    private final EditText campoWeekendStart;
    private final EditText campoWeekendEnd;

    private Horario horario;

    public ActHorarioHelper(final ActHorario activity){
        //Constructor já para trazer o valor do campo do formulario
        campoMonday = (CheckBox) activity.findViewById(R.id.horario_monday);
        campoTuesday = (CheckBox) activity.findViewById(R.id.horario_tuesday);
        campoWednesday = (CheckBox) activity.findViewById(R.id.horario_wednesday);
        campoThursday = (CheckBox) activity.findViewById(R.id.horario_thursday);
        campoFriday = (CheckBox) activity.findViewById(R.id.horario_friday);
        campoUsefulStart = (EditText) activity.findViewById(R.id.horario_edtUsefulStart);
        campoUsefulEnd = (EditText) activity.findViewById(R.id.horario_edtUsefulEnd);
        campoSaturday = (CheckBox) activity.findViewById(R.id.horario_saturday);
        campoSunday = (CheckBox) activity.findViewById(R.id.horario_sunday);
        campoWeekendStart = (EditText) activity.findViewById(R.id.horario_edtWeekendStart);
        campoWeekendEnd = (EditText) activity.findViewById(R.id.horario_edtWeekendEnd);

        DialogDateTime dialog = new DialogDateTime();
        dialog.setTimePicker(activity, campoUsefulStart);
        dialog.setTimePicker(activity, campoUsefulEnd);
        dialog.setTimePicker(activity, campoWeekendStart);
        dialog.setTimePicker(activity, campoWeekendEnd);

    }



    public Horario pegaHorario() {
        if (horario == null) {
            horario = new Horario();
        }
        horario.setUsefulMonday(campoMonday.isChecked());
        horario.setUsefulTuesday(campoTuesday.isChecked());
        horario.setUsefulWednesday(campoWednesday.isChecked());
        horario.setUsefulThursday(campoThursday.isChecked());
        horario.setUsefulFriday(campoFriday.isChecked());
        horario.setHourUsefulStart(campoUsefulStart.getText().toString());
        horario.setHourUsefulEnd(campoUsefulEnd.getText().toString());
        horario.setWeekendSaturday(campoSaturday.isChecked());
        horario.setWeekendSunday(campoSunday.isChecked());
        horario.setHourWeekendStart(campoWeekendStart.getText().toString());
        horario.setHourWeekendEnd(campoWeekendEnd.getText().toString());
        return horario;
    }

    public void preencheHorario(Horario horario) {
        campoMonday.setChecked(horario.isUsefulMonday());
        campoTuesday.setChecked(horario.isUsefulTuesday());
        campoWednesday.setChecked(horario.isUsefulWednesday());
        campoThursday.setChecked(horario.isUsefulThursday());
        campoFriday.setChecked(horario.isUsefulFriday());
        campoUsefulStart.setText(horario.getHourUsefulStart());
        campoUsefulEnd.setText(horario.getHourUsefulEnd());
        campoSaturday.setChecked(horario.isWeekendSaturday());
        campoSunday.setChecked(horario.isWeekendSunday());
        campoWeekendStart.setText(horario.getHourWeekendStart());
        campoWeekendEnd.setText(horario.getHourWeekendEnd());

        this.horario = horario;

    }

}
