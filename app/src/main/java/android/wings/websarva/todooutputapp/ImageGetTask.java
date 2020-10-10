package android.wings.websarva.todooutputapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

class ImageGetTask extends AsyncTask<String,Void, Bitmap> {
    private ImageView image;

    public ImageGetTask(ImageView _image) {
        image = _image;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap image;
        try {
            Log.d("画像開始", "画像取得開始");
            URL imageUrl = new URL(params[0]);
            InputStream imageIs;
            System.out.println("Pk");
            imageIs = imageUrl.openStream();
            System.out.println("OK1");
            image = BitmapFactory.decodeStream(imageIs);
            System.out.println("OK2");
            Log.d("修了", "画像取得修了");
            return image;
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        // 取得した画像をImageViewに設定します。
        image.setImageBitmap(result);
        System.out.println(image);
    }

}