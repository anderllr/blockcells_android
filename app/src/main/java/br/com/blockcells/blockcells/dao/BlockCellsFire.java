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
import java.util.List;

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

                if (dataSnapshot.hasChild("config_geral")) {
                    ConfigGeralDAO dao = new ConfigGeralDAO(getBaseContext());
                    ConfigGeral configGeral = dataSnapshot.child("config_geral").getValue(ConfigGeral.class);
                    dao.altera(configGeral);
                }

                if (dataSnapshot.hasChild("horario")) {
                    HorarioDAO daoHorario = new HorarioDAO(getBaseContext());
                    Horario horario = dataSnapshot.child("horario").getValue(Horario.class);
                    daoHorario.altera(horario);
                }

                if (dataSnapshot.hasChild("km")) {
                    KilometragemDAO daoKM  = new KilometragemDAO(getBaseContext());
                    Kilometragem km = dataSnapshot.child("km").getValue(Kilometragem.class);
                    daoKM.altera(km);
                }

                if (dataSnapshot.hasChild("mensagem_retorno")) {
                    MensagemDAO daoMsg = new MensagemDAO(getBaseContext());
                    Mensagem msg = dataSnapshot.child("mensagem_retorno").getValue(Mensagem.class);
                    daoMsg.altera(msg);
                }

                if (dataSnapshot.hasChild("contatovip")) {

                    ContatosExcecaoDAO cDao = new ContatosExcecaoDAO(getBaseContext());
                    cDao.deleteAll();

                    for (DataSnapshot dados: dataSnapshot.child("contatovip").getChildren()) {
                        ContatosExcecao con = dados.getValue(ContatosExcecao.class);
                        con.setId(Long.valueOf(dados.getKey()));
                        cDao.insere(con);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
