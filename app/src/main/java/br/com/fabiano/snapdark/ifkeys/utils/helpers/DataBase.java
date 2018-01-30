package br.com.fabiano.snapdark.ifkeys.utils.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {
    public static final String NOME_DB = "IFKeys";
    public static final int VERSAO =1;


    public static final String TABLE_LOGGED = "logged";
    public static final String ID_LOGGED = "_id_logged";
    public static final String NOME = "nome";
    public static final String NOME_COMPLETO = "nomeCompleto";
    public static final String EMAIL = "email";
    public static final String MATRICULA = "matricula";
    public static final String CAMPUS = "campus";
    public static final String FOTO = "foto";
    public static final String TIPO_VINCULO = "tipo_vinculo";
    public static final String CURSO = "curso";


    Context context;
    public DataBase(Context context) {
        super(context, NOME_DB, null,VERSAO );
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_LOGGED+" ("+
                ID_LOGGED+"  INTEGER PRIMARY KEY AUTOINCREMENT,"+
                EMAIL+" text not null ,"+
                NOME_COMPLETO +" text not null ,"+
                MATRICULA+" text not null ,"+
                CAMPUS+" text not null ,"+
                FOTO+" text not null ,"+
                TIPO_VINCULO+" text not null ,"+
                CURSO+" text not null ,"+
                NOME+" text not null);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*try {
            if(oldVersion < 17){
                db.execSQL("DROP TABLE IF EXISTS "+TABLE_LOGGED+";");
            }
        }catch (Exception e){e.printStackTrace();}*/
        onCreate(db);
    }
}

