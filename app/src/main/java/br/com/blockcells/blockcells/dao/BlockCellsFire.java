package br.com.blockcells.blockcells.dao;
import android.content.Context;
import android.content.ContextWrapper;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.blockcells.blockcells.funcs.GlobalSpeed;
import br.com.blockcells.blockcells.modelo.ConfigGeral;
import br.com.blockcells.blockcells.modelo.ContatosExcecao;
import br.com.blockcells.blockcells.modelo.Horario;
import br.com.blockcells.blockcells.modelo.Justificativa;
import br.com.blockcells.blockcells.modelo.Kilometragem;
import br.com.blockcells.blockcells.modelo.LogGeral;
import br.com.blockcells.blockcells.modelo.Mensagem;


/**
 * Created by Anderson on 02/02/2018.
 * This class is responsible for interetions on Firebase
 */

public class BlockCellsFire extends ContextWrapper{
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("celulares");

    public BlockCellsFire(Context base) {
        super(base);
    }

    public void startRemoteData() {
        final GlobalSpeed  globalSpeed = (GlobalSpeed) getApplicationContext();

        myRef.child(globalSpeed.getTelefone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ConfigGeralDAO dao = new ConfigGeralDAO(getBaseContext());
                ConfigGeral configGeral = dao.buscaConfigGeral();
                if (dataSnapshot.hasChild("config_geral")) {
                     configGeral = dataSnapshot.child("config_geral").getValue(ConfigGeral.class);
                    dao.altera(configGeral);
                } else {
                    salvaFirebase(configGeral, "config_geral");
                }

                HorarioDAO daoHorario = new HorarioDAO(getBaseContext());
                Horario horario = daoHorario.buscaHorario();
                if (dataSnapshot.hasChild("horario")) {
                    horario = dataSnapshot.child("horario").getValue(Horario.class);
                    daoHorario.altera(horario);
                } else {
                    salvaFirebase(horario, "horario");
                }

                KilometragemDAO daoKM  = new KilometragemDAO(getBaseContext());
                Kilometragem km = daoKM.buscaKilometragem();
                if (dataSnapshot.hasChild("km")) {
                    km = dataSnapshot.child("km").getValue(Kilometragem.class);
                    daoKM.altera(km);
                } else {
                    salvaFirebase(km, "km");
                }

                MensagemDAO daoMsg = new MensagemDAO(getBaseContext());
                Mensagem msg = daoMsg.buscaMensagem();

                if (dataSnapshot.hasChild("mensagem_retorno")) {
                    msg = dataSnapshot.child("mensagem_retorno").getValue(Mensagem.class);
                    daoMsg.altera(msg);
                } else {
                    salvaFirebase(msg, "msg");
                }

                ContatosExcecaoDAO cDao = new ContatosExcecaoDAO(getBaseContext());
                if (dataSnapshot.hasChild("contatovip")) {
                    cDao.deleteAll();

                    for (DataSnapshot dados: dataSnapshot.child("contatovip").getChildren()) {
                        ContatosExcecao con = dados.getValue(ContatosExcecao.class);
                        con.setId(Long.valueOf(dados.getKey()));
                        cDao.insere(con);
                    }
                } else { //vai inserir

                    List<ContatosExcecao> contatos = cDao.buscaContatosExcecao();
                    for (ContatosExcecao con: contatos) {
                        Map<String, String> values = new HashMap<String, String>();
                        values.put("nome", con.getNome());
                        values.put("fone", con.getFone());

                        salvaFirebaseChild(values, "contatosvip", getChild(con.getId()));
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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


    public void salvaFirebase (Object obj, String node) {
        final GlobalSpeed  globalSpeed = (GlobalSpeed) getApplicationContext();
        myRef.child(globalSpeed.getTelefone()).child(node).setValue(obj);
    }

    public void salvaFirebaseChild (Object obj, String node, String child) {
        final GlobalSpeed  globalSpeed = (GlobalSpeed) getApplicationContext();
        myRef.child(globalSpeed.getTelefone()).child(node).child(child).setValue(obj);
    }

    public void removeFirebase (String node) {
        final GlobalSpeed  globalSpeed = (GlobalSpeed) getApplicationContext();
        myRef.child(globalSpeed.getTelefone()).child(node).removeValue();
    }

    public void removeFirebaseChild (String node, String child) {
        final GlobalSpeed  globalSpeed = (GlobalSpeed) getApplicationContext();
        myRef.child(globalSpeed.getTelefone()).child(node).child(child).removeValue();
    }

    public void salvaLog(final LogGeral lg) {
        final GlobalSpeed  globalSpeed = (GlobalSpeed) getApplicationContext();
        DatabaseReference raiz = myRef.child(globalSpeed.getTelefone());
        final DatabaseReference logE = raiz.child("log_eventos");

        //Inicializa o log como 1 para o caso de não existir
        lg.setId(Long.valueOf(1));

        //Vê sobre a questão da localização
        lg.setLocalizacao(lg.getLatitude() != 0);


        raiz.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("log_eventos")) {
                     logE.limitToLast(1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Integer id = Integer.valueOf(dataSnapshot.getKey());
                            id += 1;
                            lg.setId(Long.valueOf(id));

                            logE.removeEventListener(this);
                            salvaFirebaseChild(lg, "log_eventos", String.valueOf(id) );
                        }

                         @Override
                         public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                         }

                         @Override
                         public void onChildRemoved(DataSnapshot dataSnapshot) {

                         }

                         @Override
                         public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                         }

                         @Override
                         public void onCancelled(DatabaseError databaseError) {

                         }
                     });
                } else {
                    salvaFirebaseChild(lg, "log_eventos", String.valueOf(1) );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void salvaJustificativa(final Justificativa jus) {
        final GlobalSpeed  globalSpeed = (GlobalSpeed) getApplicationContext();
        DatabaseReference raiz = myRef.child(globalSpeed.getTelefone());
        final DatabaseReference jusE = raiz.child("justificativa");

        //Inicializa o log como 1 para o caso de não existir
        jus.setId(Long.valueOf(1));


        raiz.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("justificativa")) {
                    jusE.limitToLast(1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Integer id = Integer.valueOf(dataSnapshot.getKey());
                            id += 1;
                            jus.setId(Long.valueOf(id));

                            jusE.removeEventListener(this);
                            salvaFirebaseChild(jus, "justificativa", String.valueOf(id) );
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    salvaFirebaseChild(jus, "justificativa", String.valueOf(1) );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
