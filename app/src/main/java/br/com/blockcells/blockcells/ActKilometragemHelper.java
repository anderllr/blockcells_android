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
        km.setKmMinimo(Integer.parseInt(campoKMMinimo.getText().toString()));
        km.setKmMaximo(Integer.parseInt(campoKMMaximo.getText().toString()));
        km.setKmAlerta(Integer.parseInt(campoKMAlerta.getText().toString()));
        km.setAtivarBip(campoAtivarBip.isChecked());
        km.setEnviarAlerta(campoEnviarAlerta.isChecked());
        return km;
    }

    public void preencheKilometragem(Kilometragem km) {
        campoKMMinimo.setText(km.getKmMinimo().toString());
        campoKMMaximo.setText(km.getKmMaximo().toString());
        campoKMAlerta.setText(km.getKmAlerta().toString());
        campoAtivarBip.setChecked(km.isAtivarBip());
        campoEnviarAlerta.setChecked(km.isEnviarAlerta());

        this.km = km;

    }
}
