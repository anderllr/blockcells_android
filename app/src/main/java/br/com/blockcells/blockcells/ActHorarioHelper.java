package br.com.blockcells.blockcells;

import android.widget.CheckBox;
import android.widget.EditText;

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
        horario.setSegunda(campoMonday.isChecked());
        horario.setTerca(campoTuesday.isChecked());
        horario.setQuarta(campoWednesday.isChecked());
        horario.setQuinta(campoThursday.isChecked());
        horario.setSexta(campoFriday.isChecked());
        horario.setUtil_inicio(campoUsefulStart.getText().toString());
        horario.setUtil_fim(campoUsefulEnd.getText().toString());
        horario.setSabado(campoSaturday.isChecked());
        horario.setDomingo(campoSunday.isChecked());
        horario.setFds_inicio(campoWeekendStart.getText().toString());
        horario.setFds_fim(campoWeekendEnd.getText().toString());
        return horario;
    }

    public void preencheHorario(Horario horario) {
        campoMonday.setChecked(horario.isSegunda());
        campoTuesday.setChecked(horario.isTerca());
        campoWednesday.setChecked(horario.isQuarta());
        campoThursday.setChecked(horario.isQuinta());
        campoFriday.setChecked(horario.isSexta());
        campoUsefulStart.setText(horario.getUtil_inicio());
        campoUsefulEnd.setText(horario.getUtil_fim());
        campoSaturday.setChecked(horario.isSabado());
        campoSunday.setChecked(horario.isDomingo());
        campoWeekendStart.setText(horario.getFds_inicio());
        campoWeekendEnd.setText(horario.getFds_fim());

        this.horario = horario;

    }

}
