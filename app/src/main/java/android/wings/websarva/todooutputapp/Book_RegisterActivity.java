package android.wings.websarva.todooutputapp;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.Calendar;


public class Book_RegisterActivity extends AppCompatActivity {

    String ActionPlan;
    String noteBookName;
    Bitmap originalbitmap;
    Bitmap bitmap;
    static int idCount;




    public static final String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__register);
        ImageView image = findViewById(R.id.ivCamera);
        originalbitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        bitmap =  ((BitmapDrawable)image.getDrawable()).getBitmap();

        if(idCount >= 1) {
            OrderConfirmDialogFragment fragment = new OrderConfirmDialogFragment();
            fragment.show(getSupportFragmentManager(), "OrderConfirmDialogFragment");
        }
    }

    public void onSaveButton(View view){

        EditText editBookName = findViewById(R.id.Book_name);
        EditText editBookDate = findViewById(R.id.Book_deadline);
        EditText editBookNotice = findViewById(R.id.Book_notice);
        EditText editBookActionPlan = findViewById(R.id.Book_actionPlan);
        ImageView ivCamera = findViewById(R.id.ivCamera);

        noteBookName = editBookName.getText().toString();
        String noteBookDate = editBookDate.getText().toString();
        String noteBookNotice = editBookNotice.getText().toString();
        String noteBookActionPlan = editBookActionPlan.getText().toString();
        ActionPlan = noteBookActionPlan;

        if(noteBookName.length() == 0 || noteBookDate.length() == 0 || noteBookNotice.length() == 0 || noteBookActionPlan.length() == 0 || originalbitmap == bitmap){
            Toast.makeText(Book_RegisterActivity.this, "未入力の項目があります", Toast.LENGTH_LONG).show();

        }else {


            DatabaseHelper helper = new DatabaseHelper(Book_RegisterActivity.this);

            SQLiteDatabase db = helper.getWritableDatabase();

            try {
                String sqlInsert = "INSERT INTO BookList (bookname, deadline, bookNotice, bookActionplan, bookImage, bookidCount) VALUES(?, ?, ?, ?, ?, ?)";

                SQLiteStatement sqLiteStatement = db.compileStatement(sqlInsert);
                byte[] Imagedata = getBitmapAsByteArray(bitmap);

                sqLiteStatement.bindString(1, noteBookName);
                sqLiteStatement.bindString(2, noteBookDate);
                sqLiteStatement.bindString(3, noteBookNotice);
                sqLiteStatement.bindString(4, noteBookActionPlan);
                sqLiteStatement.bindBlob(5, Imagedata);


                sqLiteStatement.executeInsert();
            } finally {
                db.close();
            }

            Calendar calendar = Calendar.getInstance();

            calendar.setTimeInMillis(System.currentTimeMillis());

            calendar.add(Calendar.DATE, 2);

            scheduleNotification(noteBookActionPlan, calendar);

            idCount++;


            Intent intent = new Intent(Book_RegisterActivity.this, MainActivity.class);

            startActivity(intent);

        }



    }

    public void onBackButtonRegister(View view){
        finish();
    }

    private void scheduleNotification(String content, Calendar calendar){
        Intent notificationIntent = new Intent(Book_RegisterActivity.this, AlarmReciver.class);
        notificationIntent.putExtra(AlarmReciver.NOTIFICATION_CONTENT, content);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Log.d("start", "start");

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(requestCode == 200 ) {
            bitmap = data.getParcelableExtra("data");
            ImageView ivCamera = findViewById(R.id.ivCamera);
            ivCamera.setImageBitmap(bitmap);
        }

        else if(result != null && requestCode != 200){
            String isbn= "";
            String json = "";
            isbn = result.getContents();
            System.out.println(isbn);
            OkHttpClient okHttpClient;
            okHttpClient = new OkHttpClient();


            Request.Builder builder = new Request.Builder();

            builder.url(url + isbn);


            final Request request = builder.build();

            Response response = null;
            final Handler handler = new Handler();


            okHttpClient.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("failure API Response", e.getLocalizedMessage());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    JSONObject rootJson = null;
                    try {
                        rootJson = new JSONObject(response.body().string());
                        JSONArray items = rootJson.getJSONArray("items");
                        Log.d("Success API Response", "APIから取得したデータの件数:" +
                                items.length());
                        ReflectResult  reflectresult = new ReflectResult(items);
                        handler.post(reflectresult);



                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                }
            });



        }

      else if(requestCode != 200 && result == null){
          Toast.makeText(this, "読み込み失敗", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCameraImageClick(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, 200);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 ,outputStream);
        return outputStream.toByteArray();
    }

    public void onBarCodeSearch(final View view) {


        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("Scan a barcode");
        integrator.initiateScan();




    }

    public void onImageRotation(View view){
        int imageheight = bitmap.getHeight();
        int imagewidth = bitmap.getWidth();

        Matrix matrix = new Matrix();

        matrix.setRotate(90, imagewidth/2, imageheight/2);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, imagewidth, imageheight, matrix, true);

        ImageView image = findViewById(R.id.ivCamera);

        image.setImageBitmap(bitmap);
    }

    private class ReflectResult implements Runnable {
        String title = "";
        String imagelink = "";

        public ReflectResult(JSONArray items) {
            try {
                // 蔵書リストの件数分繰り返しタイトルをログ出力する
                for (int i = 0; i < items.length(); i++) {
                    // 蔵書リストから i番目のデータを取得
                    JSONObject item = items.getJSONObject(i);
                    // 蔵書のi番目データから蔵書情報のグループを取得
                    JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                    JSONObject thumbnailUrlObject = volumeInfo.optJSONObject("imageLinks");
                    if (thumbnailUrlObject != null && thumbnailUrlObject.has("thumbnail")) {
                        imagelink= thumbnailUrlObject.getString("thumbnail");
                    }
                    title = volumeInfo.getString("title");
                    Log.d("取得", "取得完了");




                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        @Override
        public void run(){

            ImageView image = findViewById(R.id.ivCamera);
            ImageGetTask task = new ImageGetTask(image);
            task.execute(imagelink);

            EditText bookTitle = findViewById(R.id.Book_name);
            bookTitle.setText(title);

        }
    }



}
