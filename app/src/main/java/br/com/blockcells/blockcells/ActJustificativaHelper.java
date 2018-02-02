package br.com.blockcells.blockcells;

import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.blockcells.blockcells.modelo.Justificativa;

/**
 * Created by anderson on 25/01/2018.
 */

public class ActJustificativaHelper {

    private final EditText campoDataHora;
    private final EditText campoEvento;
    private final EditText campoJustificativa;

    private Justificativa jus;

    public ActJustificativaHelper(ActJustificativa activity){
        //Constructor já para trazer o valor do campo do formulario
        campoDataHora = (EditText) activity.findViewById(R.id.justificativa_datahora);
        campoEvento = (EditText) activity.findViewById(R.id.justificativa_evento);
        campoJustificativa = (EditText) activity.findViewById(R.id.justificativa_justificativa);
    }

    public Justificativa pegaJustificativa() {
        jus.setJustificado(true);
        jus.setDesc_justificativa(campoJustificativa.getText().toString());
        return jus;
    }

    public void preencheJustificativa(Justificativa jus) {
        campoDataHora.setText(converteData(jus.getData_hora()));
        campoEvento.setText(jus.getEvento());
        campoJustificativa.setText(jus.getDesc_justificativa());

        this.jus = jus;
    }

    private String converteData(String dataHora) {
        //função para converter a data e hora que vem no padrão americano para o padrão brasileiro
        SimpleDateFormat americaFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date dtAmerica = null;
        try {
            dtAmerica = americaFormat.parse(dataHora);
        } catch (ParseException e) {}

        SimpleDateFormat brasilFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        return brasilFormat.format(dtAmerica);
    }
}
