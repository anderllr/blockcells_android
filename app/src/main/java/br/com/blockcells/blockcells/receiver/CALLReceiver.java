package br.com.blockcells.blockcells.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

import br.com.blockcells.blockcells.ActPrincipal;
import br.com.blockcells.blockcells.R;
import br.com.blockcells.blockcells.dao.ConfigGeralDAO;
import br.com.blockcells.blockcells.dao.ContatosExcecaoDAO;
import br.com.blockcells.blockcells.dao.LogGeralDAO;
import br.com.blockcells.blockcells.dao.MensagemDAO;
import br.com.blockcells.blockcells.funcs.BlockCell;
import br.com.blockcells.blockcells.funcs.GlobalSpeed;
import br.com.blockcells.blockcells.funcs.PrefTravado;
import br.com.blockcells.blockcells.modelo.ConfigGeral;
import br.com.blockcells.blockcells.modelo.Mensagem;

/**
 * Created by anderson on 18/11/16.
 */

public class CALLReceiver extends BroadcastReceiver {
    // This String will hold the incoming phone number
    private String number;
    private PrefTravado prefTravado;

    @Override
    public void onReceive(Context context, Intent intent)
    {

        GlobalSpeed globalSpeed = (GlobalSpeed) context.getApplicationContext();
        double lat = globalSpeed.getLatitude();
        double lon = globalSpeed.getLongitude();

 //       Toast.makeText(context, "Chegou Ligação antes...", Toast.LENGTH_SHORT).show();
        // If, the received action is not a type of "Phone_State", ignore it
        if (!intent.getAction().equals("android.intent.action.PHONE_STATE")) {
   //         am.setRingerMode(ringerMode);
            return;
        }
            // Else, try to do some action
        else
        {

            BlockCell block = new BlockCell(context);

            if (!block.isBlocked(null)) {
                return;
            }
  //          Toast.makeText(context, "Chegou Ligação dentro...", Toast.LENGTH_SHORT).show();
            LogGeralDAO daoLog = new LogGeralDAO(context);

            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

 //           daoLog.insereLog("LIG", "Chegou ligação: " + number);

            if (number == null) {
                return;
            }

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

 //           daoLog.insereLog("STATE", "O state é: " + state.toString());

            Log.i("STATE", "O state é: " + state.toString());

            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE))
                return;

 //           daoLog.insereLog("STATE", "Passou do Idle ");

            prefTravado = new PrefTravado(context);

            //Se destravou o telefone para uma ligação
            if (!prefTravado.isTravado()){

                //Verifica se é ligação de saída pois ai foi liberado para fazer
                if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) || (state.toString() == "OFFHOOK")){
                    daoLog.insereLog("TRAVA: ", "Destravou para efetuar ligação para: " + number,
                            lat, lon);
 //                   Log.i("TRAVA: ", "Destravou para efetuar ligação para: " + number);
                    prefTravado.setTravado(true);
                    ActPrincipal.btnTravar.setChecked(true);
                    return;
                }
            }
            else
            {
                if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) || (state.toString() == "OFFHOOK")){
                    disconnectPhoneItelephony(context);
                    daoLog.insereLog("Efetuou Ligação", "Tentou efetuar ligação para: " + number,
                            lat, lon);
                    return;
                }
            }

            if (block.isBlocked(number)) {

                disconnectPhoneItelephony(context);

                //Grava o Log da Ligação negada
                daoLog.insereLog(context.getString(R.string.msgBlockCall), context.getString(R.string.msgBlockCallNumber) + number, lat, lon);

                ConfigGeralDAO daoCfg = new ConfigGeralDAO(context);
                ConfigGeral cfg = daoCfg.buscaConfigGeral();
                daoCfg.close();

                Log.i("MSG", "Vai verificar para enviar a mensagem...");

                //Verify if app sends sms
                if ((cfg.isEnviasms()) && (number.length() == 12)) {

                    String msgSMS = "";

                    Log.i("MSG", "Entrou para enviar a mensagem...");


                    MensagemDAO msgdao = new MensagemDAO(context);
                    Mensagem msg = msgdao.buscaMensagem();
                    msgdao.close();

                    //Send sms message
                    ContatosExcecaoDAO ce = new ContatosExcecaoDAO(context);
                    if (ce.isContatoExcecao(number)) {
                        msgSMS = msg.getDescmensagemVIP();
                    } else {
                        msgSMS = msg.getMensagem();
                    }
                    //Now Send
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, msgSMS, null, null);

                    Log.i("MSG", "Vai enviar a da localização...");
                    //Now verify about localization
                    if (cfg.getInformaLocal() && msg.isLocalizacao()){
                        String linkLoc = "http://maps.google.com/?q=" + String.valueOf(lat) + "," +
                                String.valueOf(lon);

                        Log.i("MSG", linkLoc);

                        smsManager.sendTextMessage(number, null, linkLoc, null, null);
                    }
                }

            }

            daoLog.close();


        }
   //     am.setRingerMode(ringerMode);

    }

    // Method to disconnect phone automatically and programmatically
    // Keep this method as it is
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void disconnectPhoneItelephony(Context context)
    {

        ITelephony telephonyService;

        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        try
        {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);
            telephonyService.endCall();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
