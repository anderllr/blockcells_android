package br.com.blockcells.blockcells;

import android.widget.CheckBox;
import android.widget.EditText;

import br.com.blockcells.blockcells.modelo.Kilometragem;

/**
 * Created by anderson on 26/11/16.
 */
public class ActKilometragemHelper {

    private final EditText campoKMMinimo;
    private final EditText campoKMMaximo;
    private final EditText campoKMAlerta;
    private final CheckBox campoAtivarBip;
    private final CheckBox campoEnviarAlerta;

    private Kilometragem km;

    public ActKilometragemHelper(ActKilometragem activity){
        //Constructor já para trazer o valor do campo do formulario
        campoKMMinimo = (EditText) activity.findViewById(R.id.kilometragem_kmMinimo);
        campoKMMaximo = (EditText) activity.findViewById(R.id.kilometragem_kmMaximo);
        campoKMAlerta = (EditText) activity.findViewById(R.id.kilometragem_kmAlerta);
        campoAtivarBip = (CheckBox) activity.findViewById(R.id.kilometragem_ativarBip);
        campoEnviarAlerta = (CheckBox) activity.findViewById(R.id.kilometragem_enviaAlerta);
    }

    public Kilometragem pegaKilometragem() {
        km.setVelocidade_min(Integer.parseInt(campoKMMinimo.getText().toString()));
        km.setVelocidade_max(Integer.parseInt(campoKMMaximo.getText().toString()));
        km.setVelocidade_alerta(Integer.parseInt(campoKMAlerta.getText().toString()));
        km.setEmite_bip(campoAtivarBip.isChecked());
        km.setEnvia_alerta(campoEnviarAlerta.isChecked());
        return km;
    }

    public void preencheKilometragem(Kilometragem km) {
        campoKMMinimo.setText(km.getVelocidade_min().toString());
        campoKMMaximo.setText(km.getVelocidade_max().toString());
        campoKMAlerta.setText(km.getVelocidade_alerta().toString());
        campoAtivarBip.setChecked(km.isEmite_bip());
        campoEnviarAlerta.setChecked(km.isEnvia_alerta());

        this.km = km;

    }
}
