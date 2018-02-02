package br.com.blockcells.blockcells.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.blockcells.blockcells.ActContatosExcecao;
import br.com.blockcells.blockcells.R;
import br.com.blockcells.blockcells.dao.BlockCellsDAL;
import br.com.blockcells.blockcells.dao.ContatosExcecaoDAO;
import br.com.blockcells.blockcells.modelo.ContatosExcecao;

/**
 * Created by anderson on 27/01/17.
 */

public class ContatosAdapter extends BaseAdapter {

    private final List<ContatosExcecao> contatos;
    private final Context context;

    public ContatosAdapter(Context context, List<ContatosExcecao> contatos) {
        this.contatos = contatos;
        this.context = context;

    }

    @Override
    public int getCount() {
        return contatos.size();
    }

    @Override
    public Object getItem(int position) {
        return contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contatos.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ContatosExcecao contato = contatos.get(position);

        //Comentário para a maneira simples de fazer
        //TextView view = new TextView(context);
        //view.setText(aluno.toString());

        //Busca transformar o xml de layout criado em algo concreto, ou seja em uma view
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;

        //Por questão de performance só faz o inflate se a convertView for igual a null, pois o Android reaproveita o código
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item, parent, false);
        }

        //Agora busca os campos para atribuir e deve buscar do xml de layout
        TextView campoNome = (TextView) view.findViewById(R.id.item_nome);
        campoNome.setText(contato.getNome());

        TextView campoTelefone = (TextView) view.findViewById(R.id.item_telefone);
        campoTelefone.setText(contato.getFone());

        ImageView campoFoto = (ImageView) view.findViewById(R.id.item_foto);


        if (contato.getFoto() != null) {
           // Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(contato.getFoto(), 100, 100, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        else{
             Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.person);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
        }


        ImageButton retirarContato = (ImageButton) view.findViewById(R.id.retirar_contato);

        retirarContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContatosExcecaoDAO ced = new ContatosExcecaoDAO(context);
                ced.deleta(contato);
                ced.close();
                contatos.remove(position);
                notifyDataSetChanged();

            }
        });
        return view;
    }
}
