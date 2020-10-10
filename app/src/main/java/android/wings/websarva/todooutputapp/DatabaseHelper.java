package android.wings.websarva.todooutputapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BookName.db";

    private static final int DATABASE_VERSION  = 1;

    public DatabaseHelper(Context  context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
       StringBuilder sb = new StringBuilder();

       sb.append("CREATE TABLE BookList (");
       sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
       sb.append("bookname TEXT, ");
       sb.append("deadline TEXT, ");
       sb.append("bookNotice TEXT, ");
       sb.append("bookActionplan TEXT,");
       sb.append("bookImage BLOB,");
       sb.append("bookidCount INTEGER");
       sb.append(");");
       String sql = sb.toString();

       db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
