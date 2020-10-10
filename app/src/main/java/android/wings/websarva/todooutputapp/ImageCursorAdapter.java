package android.wings.websarva.todooutputapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ImageCursorAdapter extends SimpleCursorAdapter {
    private Cursor c;
    private Context context;


    public ImageCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to){
       super(context, layout, c, from, to);
       this.c = c;
       this.context = context;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.book_list, null);
        }

        this.c.moveToPosition(pos);
        int id = this.c.getInt(this.c.getColumnIndex("_id"));
        String bookname = this.c.getString(this.c.getColumnIndex("bookname"));
        byte[] image = this.c.getBlob(this.c.getColumnIndex("bookImage"));
        ImageView iv = v.findViewById(R.id.bookImage);
        if (image != null) {
            // If there is no image in the database "NA" is stored instead of a blob
            // test if there more than 3 chars "NA" + a terminating char if more than
            // there is an image otherwise load the default

                iv.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));



        }

        String idstr = new Integer(id).toString();
        TextView idText = (TextView) v.findViewById(R.id.book_id_view);
        idText.setText(idstr);

        TextView booknameText = (TextView) v.findViewById(R.id.book_name_view);
        booknameText.setText(bookname);


        return(v);
    }
}
