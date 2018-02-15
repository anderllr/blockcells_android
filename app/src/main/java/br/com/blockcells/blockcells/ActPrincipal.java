package br.com.blockcells.blockcells;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.blockcells.blockcells.dao.BlockCellsFire;
import br.com.blockcells.blockcells.dao.ConfigGeralDAO;
import br.com.blockcells.blockcells.dao.HorarioDAO;
import br.com.blockcells.blockcells.dao.KilometragemDAO;
import br.com.blockcells.blockcells.dao.MensagemDAO;
import br.com.blockcells.blockcells.funcs.BlockService;
import br.com.blockcells.blockcells.funcs.GlobalSpeed;
import br.com.blockcells.blockcells.funcs.PrefIntro;
import br.com.blockcells.blockcells.funcs.PrefTravado;
import br.com.blockcells.blockcells.modelo.ConfigGeral;
import br.com.blockcells.blockcells.modelo.Horario;
import br.com.blockcells.blockcells.modelo.Kilometragem;
import br.com.blockcells.blockcells.modelo.Mensagem;
import br.com.blockcells.blockcells.modelo.Solicitacao;
import me.leolin.shortcutbadger.ShortcutBadger;

public class ActPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener, GpsStatus.Listener {

    private static final int REQUEST_MODIFY_PHONE_STATE = 0;
    static public final int REQUEST_LOCATION = 1;
    private LocationManager mLocationManager;
    private TextView txtSpeed;
    private PrefIntro prefIntro;
    private PrefTravado prefTravado;
    public static ToggleButton btnTravar;
    private int jus_number = 0;
    private TextView count_jus = null;
    private View mInfoNotificationBadge;
    private Context context;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("celulares");

    private boolean firstfix;
//    private GlobalSpeed globalSpeed = (GlobalSpeed) this.getBaseContext();
    public void startDatabase() {

        //Cria a configuração geral se não existir
        ConfigGeralDAO dao = new ConfigGeralDAO(this);
        ConfigGeral configGeral = dao.buscaConfigGeral();
        if (configGeral == null) {
            //Se retornou nulo é pq ainda não tem valor então insere o primeiro
            dao.inserePrimeiro();
        }
        dao.close();

        MensagemDAO daoMsg = new MensagemDAO(this);
        Mensagem mensagem = daoMsg.buscaMensagem();
        if (mensagem == null) {
            //Se retornou nulo é pq ainda não tem valor então insere o primeiro
            daoMsg.inserePrimeiro();
        }
        daoMsg.close();

        KilometragemDAO daoKM = new KilometragemDAO(this);
        Kilometragem km = daoKM.buscaKilometragem();
        if (km == null) {
            //Se retornou nulo é pq ainda não tem valor então insere o primeiro
            daoKM.inserePrimeiro();
        }
        daoKM.close();

        //busca a configuração geral
        HorarioDAO daoHorario = new HorarioDAO(this);
        Horario horario = daoHorario.buscaHorario();
        if (horario == null) {
            //Se retornou nulo é pq ainda não tem valor então insere o primeiro
            daoHorario.inserePrimeiro();
        }

        daoHorario.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Starting database service
        startDatabase();

        // Checking for first time launch - before calling setContentView()
        prefIntro = new PrefIntro(this);
        if (prefIntro.isFirstTimeLaunch()) {
            startActivity(new Intent(ActPrincipal.this, WelcomeActivity.class));
            finish();
        }

        //Set to travado
        prefTravado = new PrefTravado(this);
        prefTravado.setTravado(true);

        btnTravar = (ToggleButton) findViewById(R.id.btnTravar);

        btnTravar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Se destravar tem que emitir alerta
                if (!b) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ActPrincipal.this);
                    builder1.setMessage(getString(R.string.msgAlertaTrava));
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            getString(R.string.alertYes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    prefTravado.setTravado(false);
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            getString(R.string.alertNo),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    btnTravar.setChecked(true);
                                    Toast.makeText(ActPrincipal.this, "Colocou não...", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                else
                    prefTravado.setTravado(true);
            }
        });



        //get global variable of speed
        final GlobalSpeed globalSpeed = (GlobalSpeed) getApplicationContext();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        txtSpeed = (TextView) findViewById(R.id.txt_speed);

        //Pega o telefone autenticado
        globalSpeed.setTelefone(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent myIntent = new Intent(this, BlockService.class);
        startService(myIntent);

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.MODIFY_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.MODIFY_PHONE_STATE}, REQUEST_MODIFY_PHONE_STATE);
        } else {
            Log.i("PER", "Erro na permissão");
        }

        FloatingActionButton btnSpeed = (FloatingActionButton) findViewById(R.id.principal_speed);
        btnSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle(getResources().getString(R.string.title_permitir_remoto));
                builder.setMessage(getResources().getString(R.string.msg_permitir));
                builder.setPositiveButton(getResources().getString(R.string.button_permitir),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ActPrincipal.this, "Permitiu...", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.setNegativeButton(getResources().getString(R.string.button_cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ActPrincipal.this, "Colocou não...", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
/*
                if (globalSpeed.getSpeed() == 80) {
                    globalSpeed.setSpeed(10);
                    txtSpeed.setText("10");
                }
                else {
                    globalSpeed.setSpeed(80);
                    txtSpeed.setText("80");
                }
*/
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act_principal, menu);
    //    return true;

        MenuItem infoItem = menu.findItem(R.id.menu_justificativa);
        mInfoNotificationBadge = MenuItemCompat.getActionView(infoItem).findViewById(R.id.count_justify);
        count_jus = (TextView) MenuItemCompat.getActionView(infoItem).findViewById(R.id.count_justify);
        jus_number = 0;
        updateJustificativa(jus_number);
        MenuItemCompat.getActionView(infoItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Aqui serão executadas as ações para ir para a tela de justificativa

                if (jus_number > 0) {
                    abreListaJustificativa();
                }

            }
        });



        return super.onCreateOptionsMenu(menu);

    }

    protected void abreListaJustificativa() {
        Intent vaiParaJustificativa = new Intent(this, ActListaJustificativa.class);
        startActivity(vaiParaJustificativa);
    }

    protected void openListeners() {
        final GlobalSpeed  globalSpeed = (GlobalSpeed) getApplicationContext();
        DatabaseReference fireJus = myRef.child(globalSpeed.getTelefone()).child("justificativa");

        fireJus.orderByChild("justificado").equalTo(false).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateJustificativa((int) dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final BlockCellsFire fire = new BlockCellsFire(getApplicationContext());

        //Agora abre outro listener para o acesso remoto
        final DatabaseReference fireSol = myRef.child(globalSpeed.getTelefone()).child("solicitacao");
        fireSol.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    final Solicitacao sol = dataSnapshot.getValue(Solicitacao.class);

                    final ConfigGeralDAO dao = new ConfigGeralDAO(context);
                    final ConfigGeral c = dao.buscaConfigGeral();

                    //verifica se tem uma solicitação de acesso remoto
                    if (sol.getAceito()) {

                        if (!c.getControle_remoto()) { //se já estiver sendo controlado remotamente não há o que ajustar

                            AlertDialog.Builder builder = new AlertDialog.Builder(ActPrincipal.this);
                            builder.setCancelable(true);
                            builder.setTitle(getResources().getString(R.string.title_permitir_remoto));
                            builder.setMessage(sol.getNome() + " " + getResources().getString(R.string.msg_permitir));
                            builder.setPositiveButton(getResources().getString(R.string.button_permitir),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            c.setControle_remoto(true);
                                            dao.altera(c);
                                            fire.salvaFirebase(c, "config_geral");
                                        }
                                    });
                            builder.setNegativeButton(getResources().getString(R.string.button_cancelar), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    c.setControle_remoto(false);
                                    sol.setAceito(false);
                                    fire.salvaFirebase(sol, "solicitacao");
                                    dao.altera(c);
                                    fire.salvaFirebase(c, "config_geral");
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }

                    } else {
                        c.setControle_remoto(false);
                        dao.altera(c);
                        fire.salvaFirebase(c, "config_geral");
                    }

                } else { //se for nulo grava com forma 0
                    Solicitacao sol = new Solicitacao();
                    sol.setAceito(false);
                    sol.setNome("");
                    sol.setUsername("");

                    fire.salvaFirebase(sol, "solicitacao");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    // call the updating code on the main thread,
// so we can call this asynchronously
    public void updateJustificativa(final int new_jus_number) {
        jus_number = new_jus_number;
        if (count_jus == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_jus_number == 0)
                    count_jus.setVisibility(View.INVISIBLE);
                else {
                    count_jus.setVisibility(View.VISIBLE);
                    count_jus.setText(Integer.toString(new_jus_number));
                }
            }
        });

    //    ShortcutBadger.with(getApplicationContext()).count(jus_number); //for 1.1.3
        ShortcutBadger.applyCount(getApplicationContext(), jus_number);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.chamaConfig:
                //busca a configuração geral
                ConfigGeralDAO dao = new ConfigGeralDAO(this);
                ConfigGeral configGeral = dao.buscaConfigGeral();
                if (configGeral == null) {
                    //Se retornou nulo é pq ainda não tem valor então insere o primeiro
                    dao.inserePrimeiro();
                    //tenta buscar de novo
                    configGeral = dao.buscaConfigGeral();
                }
                dao.close();

                Intent vaiParaConfig = new Intent(this, ActConfigGeral.class);
                vaiParaConfig.putExtra("configGeral", configGeral);
                startActivity(vaiParaConfig);
                break;
            case R.id.chamaMensagem:
                MensagemDAO daoMsg = new MensagemDAO(this);
                Mensagem mensagem = daoMsg.buscaMensagem();
                daoMsg.close();

                Intent vaiParaMensagem = new Intent(this, ActMensagem.class);
                vaiParaMensagem.putExtra("mensagem", mensagem);
                startActivity(vaiParaMensagem);
                break;
            case R.id.chamaKm:
                //busca a configuração geral
                KilometragemDAO daoKM = new KilometragemDAO(this);
                Kilometragem km = daoKM.buscaKilometragem();
                if (km == null) {
                    //Se retornou nulo é pq ainda não tem valor então insere o primeiro
                    daoKM.inserePrimeiro();
                    //tenta buscar de novo
                    km = daoKM.buscaKilometragem();
                }

                daoKM.close();
                Intent vaiParaKm = new Intent(this, ActKilometragem.class);
                vaiParaKm.putExtra("km", km);
                startActivity(vaiParaKm);
                break;
            case R.id.chamaHorario:
                //busca a configuração geral
                HorarioDAO daoHorario = new HorarioDAO(this);
                Horario horario = daoHorario.buscaHorario();
                if (horario == null) {
                    //Se retornou nulo é pq ainda não tem valor então insere o primeiro
                    daoHorario.inserePrimeiro();
                    //tenta buscar de novo
                    horario = daoHorario.buscaHorario();
                }

                daoHorario.close();
                Intent vaiParaHorario = new Intent(this, ActHorario.class);
                vaiParaHorario.putExtra("horario", horario);
                startActivity(vaiParaHorario);
                break;
            case R.id.chamaContatos:
                Intent vaiParaContatos = new Intent(this, ActContatosExcecao.class);
                startActivity(vaiParaContatos);
                break;
            case R.id.chamaLog:
                Intent vaiParaLog = new Intent(this, ActLogGeral.class);
                startActivity(vaiParaLog);
                break;
            case R.id.chamaJustificativa:
                abreListaJustificativa();
                break;
/*            case R.id.chamaControle:
                Intent vaiParaControle = new Intent(this, ActControle.class);
                startActivity(vaiParaControle);
                break; */
            case R.id.chamaWelcome:
                PrefIntro prefManager = new PrefIntro(getApplicationContext());

                // make first time launch TRUE
                prefManager.setFirstTimeLaunch(true);

                startActivity(new Intent(ActPrincipal.this, WelcomeActivity.class));
                finish();
                break;
        }
 /*       if (id == R.id.chamaConfig) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setSpeed(int sp) {

        String speed = String.valueOf(sp);

        txtSpeed.setText(speed);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstfix = true;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED){// Check Permissions Now
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else{
            startLocation(); // <-- Start Location here
        }

        //Agora executa o observe do Firebase que irá atualizar os dados das tabelas
        BlockCellsFire fire = new BlockCellsFire(getApplicationContext());
        fire.startRemoteData();

        openListeners();
    }

    protected void startLocation(){
        //Double verification because Android studio causes error
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
            return;
        }
        if (mLocationManager.getAllProviders().indexOf(LocationManager.GPS_PROVIDER) >= 0) {

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
        } else {
            Log.w("MainActivity", "No GPS location provider found. GPS data display will not be available.");
        }

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGpsDisabledDialog();
        }

        mLocationManager.addGpsStatusListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocation(); // <-- Start Location here
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
            return;
        }
        mLocationManager.removeUpdates(this);
        mLocationManager.removeGpsStatusListener(this);
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location.hasAccuracy()) {

            if (firstfix) {
                setSpeed(0);

                firstfix = false;
            }
        } else {
            firstfix = true;
        }

        if (location.hasSpeed()) {
            setSpeed((int) (location.getSpeed() * 3.6));

        }

    }

    public void onGpsStatusChanged(int event) {

        switch (event) {
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
                int satsUsed = 0;
                Iterable<GpsSatellite> sats = gpsStatus.getSatellites();
                for (GpsSatellite sat : sats) {
                    if (sat.usedInFix()) {
                        satsUsed++;
                    }
                }

                if (satsUsed == 0) {

                    setSpeed(0);
                    stopService(new Intent(getBaseContext(), BlockService.class));
               //     status.setText(getResources().getString(R.string.waiting_for_fix));
                    firstfix = true;
                }
                break;

            case GpsStatus.GPS_EVENT_STOPPED:
                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showGpsDisabledDialog();
                }
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                break;
        }
    }

    public void showGpsDisabledDialog(){
       // Dialog dialog = new Dialog(this, getResources().getString(R.string.gps_disabled), getResources().getString(R.string.please_enable_gps));

    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}
}

