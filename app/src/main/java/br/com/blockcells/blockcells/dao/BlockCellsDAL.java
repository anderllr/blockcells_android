package br.com.blockcells.blockcells.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anderson on 26/11/16.
 */

public class BlockCellsDAL extends SQLiteOpenHelper {
    public BlockCellsDAL(Context context) {
        super(context, "BlockCells", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Cria a tabela de Configuração Geral
        String sql = "CREATE TABLE ConfigGeral " +
                " (id INTEGER PRIMARY KEY, controleRemoto INTEGER, informaLocal INTEGER, ativado INTEGER, enviasms INTEGER) ";
        sqLiteDatabase.execSQL(sql);

        //Agora cria a tabela de Kilometragem
        sql = "CREATE TABLE Kilometragem (id INTEGER PRIMARY KEY, kmMinimo INTEGER, kmMaximo INTEGER, kmAlerta INTEGER, " +
                "ativarBip INTEGER, enviarAlerta INTEGER) ";

        sqLiteDatabase.execSQL(sql);

        //Agora cria a tabela de mensagem
        sql = "CREATE TABLE Mensagem (id INTEGER PRIMARY KEY, mensagem TEXT, mensagemVIP TEXT, localizacao INTEGER) ";
        sqLiteDatabase.execSQL(sql);

        //Agora cria a tabela de horario -- Versão 2
        sql = "CREATE TABLE Horario (id INTEGER PRIMARY KEY, usefulMonday INTEGER, usefulTuesday INTEGER, usefulWednesday INTEGER, " +
                "usefulThursday INTEGER, usefulFriday INTEGER, hourUsefulStart TEXT, hourUsefulEnd TEXT, " +
                "weekendSaturday INTEGER, weekendSunday INTEGER, hourWeekendStart TEXT, hourWeekendEnd TEXT)";

        sqLiteDatabase.execSQL(sql);

        sql = "CREATE TABLE ContatosExcecao (id INTEGER PRIMARY KEY, nome TEXT, fone TEXT, foto BLOB, foneNormalize TEXT);";
        sqLiteDatabase.execSQL(sql);

        sql = "CREATE TABLE LogGeral (id INTEGER PRIMARY KEY, topico TEXT, ocorrencia TEXT, dataHora TEXT, latitude REAL, longitude REAL);";
        sqLiteDatabase.execSQL(sql);

        //Agora cria a tabela da justificativa - Versão 2
        sql = "CREATE TABLE Justificativa (id INTEGER PRIMARY KEY, data_hora TEXT, desc_justificativa TEXT, evento TEXT, " +
                "justificado INTEGER, latitude REAL, longitude REAL);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
      //  String sql = "DROP TABLE ConfigGeral";
      //  sqLiteDatabase.execSQL(sql);

        String sql = "";
        switch (oldVersion ){
            case 1 :
                sql = "CREATE TABLE Horario (id INTEGER PRIMARY KEY, usefulMonday INTEGER, usefulTuesday INTEGER, usefulWednesday INTEGER, " +
                        "usefulThursday INTEGER, usefulFriday INTEGER, hourUsefulStart TEXT, hourUsefulEnd TEXT, " +
                        "weekendSaturday INTEGER, weekendSunday INTEGER, hourWeekendStart TEXT, hourWeekendEnd TEXT)";

                sqLiteDatabase.execSQL(sql);
                //break; //tira o break para tratar quando tiver mais de uma versão, pois ai o switch continua executando
            case 2 :
                sql = "ALTER TABLE Kilometragem ADD kmAlerta INTEGER";
                sqLiteDatabase.execSQL(sql);
            case 3 :
                sql = "ALTER TABLE LogGeral ADD latitude REAL";
                sqLiteDatabase.execSQL(sql);
                sql = "ALTER TABLE LogGeral ADD longitude REAL";
                sqLiteDatabase.execSQL(sql);
          }

    }

    public void insere(String tabela, ContentValues dados) {
        //Não usar direto por causa do SQL Injection
        //String sql = "INSERT INTO Alunos (nome, endereco, telefone, site, nota)"
        SQLiteDatabase db = getWritableDatabase();

        db.insert(tabela, null, dados);

    }

    public Cursor buscaCursor(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        return c;


    }

    public void deletaID(String tabela, String [] params) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(tabela, "id = ?", params);

    }

    public void alteraID(String tabela, ContentValues dados, String [] params) {
        SQLiteDatabase db = getWritableDatabase();

        db.update(tabela, dados, "id = ?", params);
    }
}
