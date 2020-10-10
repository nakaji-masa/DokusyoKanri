package android.wings.websarva.todooutputapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;


public class Book_detail extends AppCompatActivity {

    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Intent intent= getIntent();

         id = intent.getIntExtra("_id", 0);

        TextView bookname = findViewById(R.id.book_name_view);
        TextView deadline = findViewById(R.id.book_deadline_view);
        TextView Notice = findViewById(R.id.book_notice_view);
        TextView Actionplan = findViewById(R.id.book_actionplan_view);
        ImageView bookimage = findViewById(R.id.book_image_view);

        DatabaseHelper helper = new DatabaseHelper(Book_detail.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {

            String sql = "SELECT bookname, deadline, bookNotice, bookActionplan, bookImage FROM BookList WHERE _id = " + id;
            Cursor cursor = db.rawQuery(sql, null);


            String booknamenote = "";
            String deadlinenote = "";
            String Noticenote = "";
            String Actionplannote = "";
            byte[] ArrayByte = new byte[0];


            while (cursor.moveToNext()) {
                int idxBookname = cursor.getColumnIndex("bookname");
                int idxdeadline = cursor.getColumnIndex("deadline");
                int idxNotice = cursor.getColumnIndex("bookNotice");
                int idxActionPlan = cursor.getColumnIndex("bookActionplan");
                int idxImage = cursor.getColumnIndex("bookImage");

                booknamenote = cursor.getString(idxBookname);
                deadlinenote = cursor.getString(idxdeadline);
                Noticenote = cursor.getString(idxNotice);
                Actionplannote = cursor.getString(idxActionPlan);
                ArrayByte = cursor.getBlob(idxImage);

            }

            bookname.setText(booknamenote);
            deadline.setText(deadlinenote);
            Notice.setText(Noticenote);
            Actionplan.setText(Actionplannote);
            bookimage.setImageBitmap(BitmapFactory.decodeByteArray(ArrayByte, 0, ArrayByte.length));
        }
        finally{
            db.close();
        }




    }

    public void onBackButton(View view){
       Intent intent = new Intent(Book_detail.this, MainActivity.class);

       startActivity(intent);


    }

    public void onChangeButton(View view){

        Intent intent = new Intent(Book_detail.this, book_Detail_Change.class);

        intent.putExtra("_id", id);

        startActivity(intent);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 ,outputStream);
        return outputStream.toByteArray();
    }


}
