package br.com.fabiano.snapdark.ifkeys.utils.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.fabiano.snapdark.ifkeys.model.User;

/**
 * Created by fabiano on 26/09/15.
 */
public class DAO {
    private SQLiteDatabase db;
    private Context context;

    public DAO(SQLiteDatabase db){this.db = db;}
    public DAO(Context context){
        db = new DataBase(context).getWritableDatabase();
        this.context = context;
    }

    public User login(User user){
        delete(DataBase.TABLE_LOGGED,"",null);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBase.NOME, user.nome);
        contentValues.put(DataBase.EMAIL,user.email);
        contentValues.put(DataBase.TIPO_VINCULO,user.tipo_vinculo);
        contentValues.put(DataBase.NOME_COMPLETO, user.nomeCompleto);
        contentValues.put(DataBase.MATRICULA, user.matricula);
        contentValues.put(DataBase.CAMPUS, user.campus);
        contentValues.put(DataBase.FOTO, user.foto);
        contentValues.put(DataBase.CURSO, user.curso);

        db.insert(DataBase.TABLE_LOGGED, null, contentValues);

        return user;
    }

    public User getLogged(){
        User user = null;
        Cursor cursor = db.rawQuery("select * from "+DataBase.TABLE_LOGGED+";",null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                user = new User();
                user.nome = cursor.getString(cursor.getColumnIndex(DataBase.NOME));
                user.email = cursor.getString(cursor.getColumnIndex(DataBase.EMAIL));
                user.matricula = cursor.getString(cursor.getColumnIndex(DataBase.MATRICULA));

                user.nomeCompleto = cursor.getString(cursor.getColumnIndex(DataBase.NOME_COMPLETO));
                user.tipo_vinculo = cursor.getString(cursor.getColumnIndex(DataBase.TIPO_VINCULO));
                user.foto = cursor.getString(cursor.getColumnIndex(DataBase.FOTO));
                user.curso = cursor.getString(cursor.getColumnIndex(DataBase.CURSO));
                user.campus = cursor.getString(cursor.getColumnIndex(DataBase.CAMPUS));
            }while (cursor.moveToNext());
        }
        return user;
    }
    public void delete(String table, String where,String[] values){
        db.delete(table,where,values);
    }
    public boolean search(String q,String [] values){
        Cursor cursor = db.rawQuery(q,values);
        AlertUtil.log("dbSize",""+cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }
    public void update(String table,ContentValues contentValues,String where){
        db.update(table,contentValues,where,null);
    }
    public void close(){
        db.close();
    }
}











