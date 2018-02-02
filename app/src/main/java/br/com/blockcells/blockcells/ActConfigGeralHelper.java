package br.com.blockcells.blockcells;

import android.widget.ToggleButton;

import br.com.blockcells.blockcells.modelo.ConfigGeral;

/**
 * Created by anderson on 16/11/16.
 * Classe responsável pela manipulação dos dados do formulário ActConfigGeral
 */

public class ActConfigGeralHelper {
    private final ToggleButton campoControleRemoto;
    private final ToggleButton campoInformaLocal;
    private final ToggleButton campoAtivado;
    private final ToggleButton campoEnviasms;

    private ConfigGeral configGeral;

    public ActConfigGeralHelper(ActConfigGeral activity){
        //Constructor já para trazer o valor do campo do formulario
        campoControleRemoto = (ToggleButton) activity.findViewById(R.id.configgeral_controleremoto);
        campoInformaLocal = (ToggleButton) activity.findViewById(R.id.configgeral_informalocalizacao);
        campoAtivado = (ToggleButton) activity.findViewById(R.id.configgeral_aplicativoativado);
        campoEnviasms = (ToggleButton) activity.findViewById(R.id.configgeral_enviasms);
    }

    public ConfigGeral pegaConfigGeral() {
        configGeral.setControleRemoto(campoControleRemoto.isChecked());
        configGeral.setInformaLocal(campoInformaLocal.isChecked());
        configGeral.setAtivado(campoAtivado.isChecked());
        configGeral.setEnviasms(campoEnviasms.isChecked());
        return configGeral;
    }

    public void preencheConfigGeral(ConfigGeral configGeral) {
        campoControleRemoto.setChecked(configGeral.getControleRemoto());
        campoInformaLocal.setChecked(configGeral.getInformaLocal());
        campoAtivado.setChecked(configGeral.getAtivado());
        campoEnviasms.setChecked(configGeral.isEnviasms());

        this.configGeral = configGeral;

    }


}
