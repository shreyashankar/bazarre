package com.shreya_shankar.bazarre;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.view.*;


public class BookNameActivity extends ActionBarActivity {

    private Page[] pages;
    private int pageNumber;
    boolean need;
    private Button choice1, choice2;
    private Page currentPage;
    private TextView textView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_name);

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

        if (page != 0) {
            editText.setVisibility(View.INVISIBLE);
        }

        choice1.setText(currentPage.getChoice1().getText());
        if (currentPage.getChoice2() != null) {
            choice2.setText(currentPage.getChoice2().getText());
        } else {
            choice2.setVisibility(View.INVISIBLE);
        }

        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPage = currentPage.getChoice1().getNextPage();
                if (pageNumber == 0) {
                    String class_name = editText.getText().toString();
                    System.out.println(class_name);
                }
                if (nextPage != 100) {
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
