package android.wings.websarva.todooutputapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    ListView myListView;
    private int id = 0;
    private int subId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        myListView = (ListView)findViewById(R.id.bookList);

        DatabaseHelper helper = new DatabaseHelper(this);

        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query("BookList", new String[]{"_id", "bookname", "bookImage"}, null, null,
                null, null, null);

        startManagingCursor(cursor);

        BaseAdapter adapter = new ImageCursorAdapter(this, R.layout.book_list, cursor, new String[] {"_id", "bookname", "bookImage"}, new int[] {R.id.book_id_view, R.id.book_name_view, R.id.bookImage});


        myListView.setAdapter(adapter);

        myListView.setOnItemClickListener(new ListItemClickListener());





    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            LinearLayout linear = (LinearLayout)view;

            TextView id = (TextView)linear.findViewById(R.id.book_id_view);

            String idText = id.getText().toString();

            int idInt = Integer.parseInt(idText);

            Intent intent = new Intent(MainActivity.this, Book_detail.class);

            intent.putExtra("_id", idInt);

            startActivity(intent);


        }
    }

    public void onBookRegister(View view){
        Intent intent = new Intent(this, Book_RegisterActivity.class);
        intent.putExtra("_id", id);
        startActivity(intent);
    }




}
