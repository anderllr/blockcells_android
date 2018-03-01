package br.com.blockcells.blockcells;


import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.blockcells.blockcells.adapter.ContatosAdapter;
import br.com.blockcells.blockcells.dao.BlockCellsFire;
import br.com.blockcells.blockcells.dao.ConfigGeralDAO;
import br.com.blockcells.blockcells.dao.ContatosExcecaoDAO;
import br.com.blockcells.blockcells.modelo.ConfigGeral;
import br.com.blockcells.blockcells.modelo.ContatosExcecao;

public class ActContatosExcecao extends AppCompatActivity {

    private ListView listaContatos;
    public final int PICK_CONTACT = 2017;
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos_excecao);

        listaContatos = (ListView) findViewById(R.id.lista_contatos);

 /*       listaContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long l) {
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(position);
                //Toast.makeText(ListaAlunosActivity.this, "Aluno: " + aluno.getNome(), Toast.LENGTH_SHORT).show();
                Intent intentVaiParaFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intentVaiParaFormulario.putExtra("aluno", aluno);
                startActivity(intentVaiParaFormulario);
            }
        });
*/

        FloatingActionButton novo_contato = (FloatingActionButton) findViewById(R.id.novo_contato);
        novo_contato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verifica se está marcado para apenas controle remoto
                ConfigGeralDAO dao = new ConfigGeralDAO(ActContatosExcecao.this);
                ConfigGeral cfg = dao.buscaConfigGeral();

                if (cfg.getControle_remoto()) {
                    Snackbar.make(view, getString(R.string.alertRemote), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    //AQUI VAI ABRIR A LISTA DE CONTATOS SE HOUVER MENOS QUE 3
                    if (listaContatos.getCount() < 3) {
                        showContacts();
                    } else {
                        Snackbar.make(view, getString(R.string.alertContacts), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }

            }
        });

     //   registerForContextMenu(listaContatos);
    }

    /**
     * Show the contacts in the ListView.
     */
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(i, PICK_CONTACT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Sem permissão aos contatos você não poderá salvá-los", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            int cNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int cName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int cN = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
            String photo = ContactsContract.Contacts.Photo.PHOTO_THUMBNAIL_URI;

            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            Bitmap b = getFoto(id);


            ContatosExcecaoDAO cDao = new ContatosExcecaoDAO(this);
            //After get contact insert in list
            ContatosExcecao ce = new ContatosExcecao();
            ce.setId(cDao.getMax());
            ce.setNome(cursor.getString(cName));
            ce.setFone(cursor.getString(cNumber));
            ce.setFoneNormalize(cursor.getString(cN));
            ce.setFoto(b);

            cDao.insere(ce);

            Map<String, String> dic = new HashMap<String, String>();
            dic.put("nome", ce.getNome());
            dic.put("fone", ce.getFone());

            BlockCellsFire fire = new BlockCellsFire(getApplicationContext());
            fire.salvaFirebaseChild(dic, "contatovip", getChild(ce.getId()));

            carregaLista("gravando");
        }
    }

    private String getChild(Long id) {
        //função usada para normalizar a string passada para o firebase
        String child = String.valueOf(id);

        switch(child.length()) {
            case 1:
                child = "00" + child;
                break;
            case 2:
                child = "0" + child;
                break;
        }

        return child;
    }


    // Retorna a Foto do contato
    public Bitmap getFoto(String id) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(id));

        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), uri);

        if (input == null) {
            return null;
        }
        else
          return BitmapFactory.decodeStream(input);
    }

    private void carregaLista(String fase) {
        //A fase vai determinar se será gravação ou leitura inicial
        ContatosExcecaoDAO dao = new ContatosExcecaoDAO(this);
        List<ContatosExcecao> contatos = dao.buscaContatosExcecao();
        dao.close();

        ContatosAdapter adapter = new ContatosAdapter(this, contatos);
        listaContatos.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista("inicio");
    }

    //Agora sobrescreve o método para utilizar o menu criado no xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //infla ele
        getMenuInflater().inflate(R.menu.menu_voltar, menu);

        return true;
    }

    //Para ter o comportamento do clique sobrescreve o método abaixo
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_voltar:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
