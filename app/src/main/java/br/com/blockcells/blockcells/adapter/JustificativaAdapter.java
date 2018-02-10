package br.com.blockcells.blockcells.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.blockcells.blockcells.R;
import br.com.blockcells.blockcells.modelo.Justificativa;

/**
 * Created by anderson on 23/01/2018.
 */

public class JustificativaAdapter extends BaseAdapter {

    private final List<Justificativa> juslista;
    private final Context context;

    public JustificativaAdapter(Context context, List<Justificativa> juslista) {
        this.juslista = juslista;
        this.context = context;
    }

    @Override
    public int getCount() {
        return juslista.size();
    }

    @Override
    public Object getItem(int position) {
        return juslista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return juslista.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Justificativa just = juslista.get(position);

        //Busca transformar o xml de layout criado em algo concreto, ou seja em uma view
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;

        //Por questão de performance só faz o inflate se a convertView for igual a null, pois o Android reaproveita o código
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_justificativa, parent, false);
        }

        //Converter a data e hora separadamente
        //       String data = getDataHora(loggeral.getData_hora(), "D");
//        String hora = getDataHora(loggeral.getData_hora(), "H");

        String data = "";
        String hora = "";

        if (just.getData_hora() != null) {
            String[] arrayData = just.getData_hora().split(" ");
            data = arrayData[0];
            hora = arrayData[1];
        }

        //Agora busca os campos para atribuir e deve buscar do xml de layout
        TextView campoData = (TextView) view.findViewById(R.id.jus_data);
        campoData.setText(data);

        TextView campoHora = (TextView) view.findViewById(R.id.jus_hora);
        campoHora.setText(hora);

        TextView campoTopico = (TextView) view.findViewById(R.id.jus_topico);
        campoTopico.setText(just.getEvento());

        TextView campoOcorrencia = (TextView) view.findViewById(R.id.jus_ocorrencia);
        campoOcorrencia.setText(just.getEvento());

        return view;
    }

    public String getDataHora(String dataHora, String tipo){
        String result = "";

        if (dataHora != "") {

            //Start conversion of Date Hour String
            DateFormat fDataHora = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            Date dateObject = null;
            try {
                dateObject = fDataHora.parse(dataHora);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (tipo == "D") {
                DateFormat fData = new SimpleDateFormat("dd/MM/yyyy");
                result = fData.format(dateObject);
            }
            else //hour
            {
                DateFormat fHora = new SimpleDateFormat("hh:mm:ss");
                result = fHora.format(dateObject);
            }
        }
        return  result;
    }
}