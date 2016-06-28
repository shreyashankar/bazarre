package com.shreya_shankar.bazarre;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class BookNameActivity extends ActionBarActivity {

    private Page[] pages;
    boolean need;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_name);

        Intent intent = getIntent();
        if(intent.getStringExtra("name") == "need") {
            need = true;
        } else {
            need = false;
        }

        pages = new Page[7];
        pages[0] = new Page("Class name?", new Choice ("OK", 1), new Choice("QUIT", 100), need);
        pages[1] = new Page("We have the book!", new Choice ("OK", 3), new Choice("QUIT", 100));
        pages[2] = new Page("We don't have the book yet. Do you want us to let you know when we get it?", new Choice ("OK", 4), new Choice("QUIT", 100));
        pages[3] = new Page("You can email _____ at _____ to get this book. Are you interested?", new Choice ("OK", 5), new Choice("QUIT", 100));
        pages[4] = new Page("Thanks! We'll let you know when it's available.", new Choice ("OK", 100));
        pages[5] = new Page("Thanks! You have 72 hours to get your book.", new Choice ("OK", 100));
        pages[6] = new Page("Thanks! We'll let you know if someone needs your book.", new Choice ("OK", 100));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_name, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
