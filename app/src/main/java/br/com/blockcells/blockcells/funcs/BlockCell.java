package br.com.blockcells.blockcells.funcs;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.blockcells.blockcells.dao.ConfigGeralDAO;
import br.com.blockcells.blockcells.dao.ContatosExcecaoDAO;
import br.com.blockcells.blockcells.dao.HorarioDAO;
import br.com.blockcells.blockcells.dao.KilometragemDAO;
import br.com.blockcells.blockcells.dao.LogGeralDAO;
import br.com.blockcells.blockcells.modelo.ConfigGeral;
import br.com.blockcells.blockcells.modelo.Horario;
import br.com.blockcells.blockcells.modelo.Kilometragem;

/**
 * Created by anderson on 02/01/17.
 * This class is responsible for verify if system can block cellphone
 */

public class BlockCell {
    private static final String TAG = "BlockService";
    private static int speed;
    private String lastNumberCall;
    private Context context;
    final GlobalSpeed globalSpeed;

    public BlockCell(Context context) {

        this.context = context;

        this.globalSpeed = (GlobalSpeed) context.getApplicationContext();

        this.speed = globalSpeed.getSpeed();
        this.lastNumberCall = globalSpeed.getLastNumberCall();
    }

    public boolean isBlocked(String number) {
        //Here is the code that verify if system can block cellphone
        boolean blocked = false;

        //Verify if app is activated
        ConfigGeralDAO daoCfg = new ConfigGeralDAO(context);
        ConfigGeral cfg = daoCfg.buscaConfigGeral();
        daoCfg.close();

        //Grava o log do bloqueio
//        LogGeralDAO daoLog = new LogGeralDAO(context);
   //     daoLog.insereLog("FUNÇÃO: ", "Entrou na função isBlocked");

      //  Log.i(TAG, "Entrou na função isBlocked");

        if (cfg.getAtivado()) {
      //      daoLog.insereLog("getAtivado: ", "Aplicativo está ativado");
        //    Log.i(TAG, "Aplicativo está ativado");
            //Verify minimum speed
            KilometragemDAO daoKM = new KilometragemDAO(context);
            Kilometragem km = daoKM.buscaKilometragem();
            daoKM.close();

         //   daoLog.insereLog("SPEED: ", String.valueOf(speed));
            if (km.getKmMinimo() < speed) {
                blocked = true;
            }

            if (blocked) {
          //      daoLog.insereLog("SPEED: ", "Bloqueio por velocidade");
          //      Log.i(TAG, "KM acima");
                //Now verify hour of app is permitted
                HorarioDAO daoHorario = new HorarioDAO(context);
                Horario hr = daoHorario.buscaHorario();
                daoHorario.close();

                //First verify the day of the week
                Calendar today = Calendar.getInstance();
                int weekDay = today.get(Calendar.DAY_OF_WEEK);

                switch (weekDay) {
                    case 1:
                        blocked = (hr.isWeekendSunday());
                        break;
                    case 2:
                        blocked = (hr.isUsefulMonday());
                        break;
                    case 3:
                        blocked = (hr.isUsefulTuesday());
                        break;
                    case 4:
                        blocked = (hr.isUsefulWednesday());
                        break;
                    case 5:
                        blocked = (hr.isUsefulThursday());
                        break;
                    case 6:
                        blocked = (hr.isUsefulFriday());
                        break;
                    case 7:
                        blocked = (hr.isWeekendSaturday());
                        break;
                }

                Date now = today.getTime();

                //if (blocked = true) means the day of the week is marked
                if (blocked){
            //        daoLog.insereLog("SEMANA: ", "Está dentro da semana");
            //        Log.i(TAG, "Está dentro da semana, dia: " + weekDay);
                    //convert string to time
                    Date start = null;
                    Date end = null;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh:mm");
                    SimpleDateFormat fToday = new SimpleDateFormat("yyyy-MM-dd");
                    String dateHour = fToday.format(now);

              //      Log.i(TAG, "DateHour: " + dateHour);

                    if ((weekDay > 1) && (weekDay < 7)) { //Usefulday

                        try {
                //            Log.i(TAG, "É dia de semana " + dateHour);
                            start = format.parse(dateHour + "-" + hr.getHourUsefulStart());
                            end = format.parse(dateHour + "-" + hr.getHourUsefulEnd());

                        } catch (ParseException e) {
                        }

                    } else { //Weekend

                        try {
                  //          Log.i(TAG, "É final de semana " + dateHour);
                            start = format.parse(dateHour + "-" + hr.getHourWeekendStart());
                            end = format.parse(dateHour + "-" + hr.getHourWeekendEnd());

                        } catch (ParseException e) {
                        }
                    }

                    //Verify if in permitted hour
                    if ((now.getTime() >= start.getTime()) && (now.getTime() <= end.getTime())) {
                        blocked = true;
                    } else {
                        blocked = false;
                    }

                } //end of second if (blocked)

            } //end of first if (blocked) hour...

            //Now we verify if the contact is in favorite list
            //
            if (blocked && (number !=null)){
             //   daoLog.insereLog("HORÁRIO: ", "Está dentro do horário");
                ContatosExcecaoDAO ce = new ContatosExcecaoDAO(context);
                if (ce.isContatoExcecao(number)) {
                    if (lastNumberCall != null) {
                        if (lastNumberCall.equals(number)) {
                            //Is the second call
                            blocked = false;
       //                     Log.i("VIP", "É VIP: Anterior: " + lastNumberCall + " Atual: " + number + " " + String.valueOf(blocked));
                        } else {
                            globalSpeed.setLastNumberCall(number);
                        }
                    } else globalSpeed.setLastNumberCall(number);
                }
                else //zera a variável para não ficar sujeira
                    globalSpeed.setLastNumberCall("");
                ce.close();

            }

     //       if (blocked) {
    //            daoLog.insereLog("VIP: ", "Não é VIP");
     //       }
            //

        } //end of cfg.getAtivado
   //     daoLog.close();
  //      Log.i("RET", "Anterior: " + lastNumberCall + " Atual: " + number + " " + String.valueOf(blocked));
        return blocked;
    }

}
