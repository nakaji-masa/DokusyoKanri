package android.wings.websarva.todooutputapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class book_Detail_Change extends AppCompatActivity {

    private int id;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__detail__change);

        Intent intent = getIntent();
        id = intent.getIntExtra("_id", 0);

        EditText bookname = findViewById(R.id.Book_name_change);
        EditText deadline = findViewById(R.id.Book_deadline_change);
        EditText notice = findViewById(R.id.Book_notice_change);
        EditText actionPlan = findViewById(R.id.Book_actionPlan_change);
        ImageView bookImage = findViewById(R.id.ImageChange);


        DatabaseHelper helper = new DatabaseHelper(book_Detail_Change.this);

        SQLiteDatabase db = helper.getWritableDatabase();

        try {

            String sql = "SELECT bookname, deadline, bookNotice, bookActionplan, bookImage FROM BookList WHERE _id = " + id;
            Cursor cursor = db.rawQuery(sql, null);


            String booknamenote = "";
            String deadlinenote = "";
            String Noticenote = "";
            String Actionplannote = "";
            byte[] byteArr = new byte[0];


            while (cursor.moveToNext()) {
                int idxBookname = cursor.getColumnIndex("bookname");
                int idxdeadline = cursor.getColumnIndex("deadline");
                int idxNotice = cursor.getColumnIndex("bookNotice");
                int idxActionPlan = cursor.getColumnIndex("bookActionplan");
                int idxBookImage = cursor.getColumnIndex("bookImage");


                booknamenote = cursor.getString(idxBookname);
                deadlinenote = cursor.getString(idxdeadline);
                Noticenote = cursor.getString(idxNotice);
                Actionplannote = cursor.getString(idxActionPlan);
                byteArr = cursor.getBlob(idxBookImage);

            }

            bookname.setText(booknamenote);
            deadline.setText(deadlinenote);
            notice.setText(Noticenote);
            actionPlan.setText(Actionplannote);
            bookImage.setImageBitmap(BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length));
            bitmap = BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length);
        }
        finally{
            db.close();
        }

    }

    public void onChangeButton(View view){
        EditText editBookName = findViewById(R.id.Book_name_change);
        EditText editBookDate = findViewById(R.id.Book_deadline_change);
        EditText editBookNotice = findViewById(R.id.Book_notice_change);
        EditText editBookActionPlan = findViewById(R.id.Book_actionPlan_change);

        String noteBookName = editBookName.getText().toString();
        String noteBookDate = editBookDate.getText().toString();
        String noteBookNotice = editBookNotice.getText().toString();
        String noteBookActionPlan = editBookActionPlan.getText().toString();
        byte[] imagedata = getBitmapAsByteArray(bitmap);


        DatabaseHelper helper = new DatabaseHelper(book_Detail_Change.this);

        SQLiteDatabase db = helper.getWritableDatabase();

        try {


            String sql = "UPDATE BookList SET bookname = ?, deadline = ?, bookNotice = ?, bookActionplan = ?, bookImage = ?  WHERE _id = ?";

           SQLiteStatement stmt = db.compileStatement(sql);

           stmt.bindString(1, noteBookName);
           stmt.bindString(2, noteBookDate);
           stmt.bindString(3, noteBookNotice);
           stmt.bindString(4, noteBookActionPlan);
           stmt.bindBlob(5, imagedata);
           stmt.bindLong(6, id);

           stmt.executeUpdateDelete();


        }

        finally{
           db.close();

        }

        Intent intent = new Intent(book_Detail_Change.this, Book_detail.class);

        intent.putExtra("_id", id);

        startActivity(intent);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 200 ){
            bitmap = data.getParcelableExtra("data");
            ImageView image = findViewById(R.id.ImageChange);
            image.setImageBitmap(bitmap);
        }
    }

    public void onCameraImageChangeClick(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, 200);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 ,outputStream);
        return outputStream.toByteArray();
    }

    public void onImageRotation(View view){
        int imageheight = bitmap.getHeight();
        int imagewidth = bitmap.getWidth();

        Matrix matrix = new Matrix();

        matrix.setRotate(90, imagewidth/2, imageheight/2);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, imagewidth, imageheight, matrix, true);

        ImageView image = findViewById(R.id.ImageChange);

        image.setImageBitmap(bitmap);
    }
}
