package br.com.blockcells.blockcells.funcs;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.multidex.MultiDex;

import br.com.blockcells.blockcells.R;
import br.com.blockcells.blockcells.dao.ConfigGeralDAO;
import br.com.blockcells.blockcells.dao.KilometragemDAO;
import br.com.blockcells.blockcells.dao.LogGeralDAO;
import br.com.blockcells.blockcells.modelo.ConfigGeral;
import br.com.blockcells.blockcells.modelo.Kilometragem;

/**
 * Created by anderson on 03/01/17.
 */

public class GlobalSpeed extends Application {
    private int speed;
    private double latAnt;
    private double lonAnt;
    private double latitude;
    private double longitude;
    private String lastNumberCall;
    private PrefSpeed prefSpeed;
    private AudioManager am;
    private static int ringerMode;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        prefSpeed = new PrefSpeed(this);

        am = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);

        ringerMode = -1;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public String getLastNumberCall() {
        return lastNumberCall;
    }

    public void setLastNumberCall(String lastNumberCall) {
        this.lastNumberCall = lastNumberCall;
    }

    public double getLatAnt() {
        return latAnt;
    }

    public void setLatAnt(double latAnt) {
        this.latAnt = latAnt;
    }

    public double getLonAnt() {
        return lonAnt;
    }

    public void setLonAnt(double lonAnt) {
        this.lonAnt = lonAnt;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        verSpeedMax(speed);
    }

    private void verSpeedMax(int speed) {
        //Para registro do log de excesso de velocidade
        LogGeralDAO daoLog = new LogGeralDAO(this
        );

        KilometragemDAO daoKM = new KilometragemDAO(this);
        Kilometragem km = daoKM.buscaKilometragem();
        daoKM.close();

        //Verify if app is activated
        ConfigGeralDAO daoCfg = new ConfigGeralDAO(this);
        ConfigGeral cfg = daoCfg.buscaConfigGeral();
        daoCfg.close();

        if (cfg.getAtivado()) {
            if (speed > km.getKmAlerta()) {
                MediaPlayer ringAlerta = MediaPlayer.create(this, R.raw.beepblock);
                ringAlerta.start();
            }

            if (speed > km.getKmMaximo()) {
                MediaPlayer ring = MediaPlayer.create(this, R.raw.sirene);
                ring.start();
                daoLog.insereLog("Excesso de velocidade", "Ultrapassou velocidade permitida", this.latitude, this.longitude);
            }
        }

        //method that verify if speed is major than minimun limit for block mobile data
        if (speed > km.getKmMinimo()) {
            //se já foi tratado não mexe mais, isso para não confundir o acesso aos dados duas vezes
            if (prefSpeed.isSpeedChanged())
                return;

            //agora verifica se deve ser bloqueado para travar os dados
            BlockCell block = new BlockCell(this);

            //se já está no silencioso não muda
            ringerMode = am.getRingerMode();

            if (ringerMode ==AudioManager.RINGER_MODE_SILENT){
                return;
            }

            //se passou é porque não estava silencioso

            am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            prefSpeed.setSpeedChanged(true);

        } else {
            //se a velocidade é menor ai tem que voltar o estado dos dados
            if (!prefSpeed.isSpeedChanged()) //vê se havia sido ajustado
                return;

            //volta o anterior
            if (ringerMode >= 0)
                am.setRingerMode(ringerMode);

            prefSpeed.setSpeedChanged(false);
        }

    }


}
