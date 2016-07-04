package com.shreya_shankar.bazarre;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.view.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: add a final method to Page instead of relying on page 100 to mark end
//TODO: clean up code
//TODO: figure out how to make layout of pages better
//TODO: integrate database
//TODO: jump from page 0 to 1 or 2 based on availability
//TODO: put better text instead of dashboard

public class BookNameActivity extends ActionBarActivity {

    private Page[] pages;
    private int pageNumber;
    boolean need;
    private Button choice1, choice2;
    private Page currentPage;
    private TextView textView;
    private EditText editText;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_name);

        //Intents connect activities to each other -- pass in variable that represents
        // whether we need book or get book
        //Firebase.setAndroidContext(this);
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        if(intent.getStringExtra("name").equals("need")) {
            need = true;
        } else {
            need = false;
        }

        if (need) {
            pages = new Page[6];
            pages[0] = new Page("What's your class name?", new Choice("OK", 1), new Choice("QUIT", 100), need);
            pages[1] = new Page("We have the book!", new Choice ("OK", 3), new Choice("QUIT", 100));
            pages[2] = new Page("We don't have the book yet. Do you want us to let you know when we get it?", new Choice ("OK", 4), new Choice("QUIT", 100));
            pages[3] = new Page("You can email _____ at _____ to get this book. Are you interested?", new Choice ("OK", 5), new Choice("QUIT", 100));
            pages[4] = new Page("Thanks! We'll let you know when it's available.", new Choice ("OK", 100));
            pages[5] = new Page("Thanks! You have 72 hours to get your book.", new Choice ("OK", 100));
        } else {
            pages = new Page[2];
            pages[0] = new Page("Class name?", new Choice("OK", 1), new Choice("QUIT", 100), need);
            pages[1] = new Page("Thanks! We'll let you know if someone needs your book.", new Choice ("OK", 100));
        }

        choice1 = (Button) findViewById(R.id.ok_button);
        choice2 = (Button) findViewById(R.id.quit_button);
        textView = (TextView) findViewById(R.id.text_prompt);
        editText = (EditText) findViewById(R.id.class_name);
        loadPage(0);
    }

    private void loadPage(int page) {
        currentPage = pages[page];
        pageNumber = page;

        String pageText = currentPage.getText();
        textView.setText(pageText);

        //only first page should have textbox
        if (page != 0) {
            editText.setVisibility(View.INVISIBLE);
        }

        choice1.setText(currentPage.getChoice1().getText());
        //if there is no second choice on page we should not display it
        if (currentPage.getChoice2() != null) {
            choice2.setText(currentPage.getChoice2().getText());
        } else {
            choice2.setVisibility(View.INVISIBLE);
        }

        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPage = currentPage.getChoice1().getNextPage();
                performChoice(nextPage);
            }
        });

        if (currentPage.getChoice2() != null) {
            choice2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextPage = currentPage.getChoice2().getNextPage();
                    performChoice(nextPage);
                }
            });
        }
    }

    private void performChoice(int nextPage) {
        if (pageNumber == 0) {
            String class_name = editText.getText().toString();
            DatabaseReference myRef;
            Calendar cal = Calendar.getInstance();
            //converts back into date: http://stackoverflow.com/questions/7953725/how-to-convert-milliseconds-to-date-format-in-android
            long date = cal.getTimeInMillis();
            if (need) {
                myRef = database.getReference("need");
            }
            else {
                myRef = database.getReference("done");
            }
            Request r = new Request("Reese", date);
            myRef.child(class_name).push().setValue(r);
        }
        if (nextPage != 100) {
            loadPage(nextPage);
        } else {
            finish();
        }
    }


}