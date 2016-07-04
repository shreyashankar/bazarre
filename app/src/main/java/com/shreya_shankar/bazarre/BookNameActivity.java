package com.shreya_shankar.bazarre;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.view.*;

import com.firebase.client.Firebase;

import java.util.HashMap;
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
    private Firebase databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_name);

        //Intents connect activities to each other -- pass in variable that represents
        // whether we need book or get book
        Firebase.setAndroidContext(this);
        Firebase mRef = new Firebase("bazarre-1361.firebaseio.com");
        databaseRef = mRef.child("requests");

        Intent intent = getIntent();
        if(intent.getStringExtra("name").equals("need")) {
            need = true;
        } else {
            need = false;
        }

        if (need) {
            pages = new Page[7];
            pages[0] = new Page("Class name?", new Choice("OK", 1), new Choice("QUIT", 100), need);
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
                if (pageNumber == 0) { //deal with text box
                    String class_name = editText.getText().toString();
                    System.out.println(class_name);
                }
                if (nextPage != 100) { //page 100 does not exist; marks final
                    loadPage(nextPage);
                } else {
                    finish();
                }
            }
        });

        if (currentPage.getChoice2() != null) {
            choice2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextPage = currentPage.getChoice2().getNextPage();
                    if (pageNumber == 0) {
                        String class_name = editText.getText().toString();
                        System.out.println(class_name);
                        Map<String, String> userRequest = new HashMap<String, String>();
                        if (need) {
                            userRequest.put(class_name, "need");
                        } else {
                            userRequest.put(class_name, "done");
                        }
                        databaseRef.push().setValue(userRequest);
                    }
                    if (nextPage != 100) {
                        loadPage(nextPage);
                    } else {
                        finish();
                    }
                }
            });
        }
    }


}