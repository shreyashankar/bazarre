package com.shreya_shankar.bazarre;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private Button need_book;
    private Button done_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        need_book = (Button)findViewById(R.id.need_book_button);
        done_book = (Button)findViewById(R.id.done_book_button);

        need_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookEntry(true);
            }
        });

        done_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookEntry(false);
            }
        });

    }

    private void bookEntry(boolean need) {
        Intent intent = new Intent(this, BookNameActivity.class);
        if (need) {intent.putExtra("name", "need");}
        else {intent.putExtra("name", "done");}
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
    }


}
