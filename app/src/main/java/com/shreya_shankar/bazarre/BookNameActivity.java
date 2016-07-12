package com.shreya_shankar.bazarre;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.view.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: add a final method to Page instead of relying on page 100 to mark end -- meh
//TODO: clean up code
//TODO: figure out how to make layout of pages better -- later
//TODO: integrate database -- mostly done
//TODO: put better text instead of dashboard -- later
//TODO: get correct user based on date/time -- next
//TODO: implement authentication for firebase
//TODO: implement authentication for user
//TODO: delete database entry if someone presses quit -- next
//TODO: let user go back

public class BookNameActivity extends ActionBarActivity {

    private Page[] pages;
    private int pageNumber;
    boolean need;
    private Button choice1, choice2;
    private Page currentPage;
    private TextView textView;
    private EditText editText;
    private final List<Request> matchedRequests = new ArrayList<Request>();
    FirebaseDatabase database;
    private int nextPage;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_name);

        //Intents connect activities to each other -- pass in variable that represents
        // whether we need book or get book
        //Firebase.setAndroidContext(this);
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        if (intent.getStringExtra("name").equals("need")) {
            need = true;
        } else {
            need = false;
        }

        if (need) {
            pages = new Page[6];
            //page 100 marks transition to finish()
            pages[0] = new Page("What's your class name?", new Choice("OK", 1), new Choice("QUIT", 100), need);
            pages[1] = new Page("We have the book!", new Choice("OK", 3), new Choice("QUIT", 100));
            pages[2] = new Page("We don't have the book yet. Do you want us to let you know when we get it?", new Choice("OK", 4), new Choice("QUIT", 100));
            pages[3] = new Page("You can email _____ at _____ to get this book. Are you interested?", new Choice("OK", 5), new Choice("QUIT", 100));
            pages[4] = new Page("Thanks! We'll let you know when it's available.", new Choice("OK", 100));
            pages[5] = new Page("Thanks! You have 72 hours to get your book.", new Choice("OK", 100));
        } else {
            pages = new Page[2];
            pages[0] = new Page("Class name?", new Choice("OK", 1), new Choice("QUIT", 100), need);
            pages[1] = new Page("Thanks! We'll let you know if someone needs your book.", new Choice("OK", 100));
        }

        choice1 = (Button) findViewById(R.id.ok_button);
        choice2 = (Button) findViewById(R.id.quit_button);
        textView = (TextView) findViewById(R.id.text_prompt);
        editText = (EditText) findViewById(R.id.class_name);
        loadPage(0);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void loadPage(int page) {
        if (page == 100) {
            finish();
            return;
        }
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
                nextPage = currentPage.getChoice1().getNextPage();
                performChoice(nextPage);
            }
        });

        if (currentPage.getChoice2() != null) {
            choice2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextPage = currentPage.getChoice2().getNextPage();
                    performChoice(nextPage);
                }
            });
        }
    }

    private void performChoice(int nextPage) {
        String class_name = "";
        DatabaseReference myRef;
        DatabaseReference checkRef = null;
        if (pageNumber == 0) {
            class_name = editText.getText().toString();
            Calendar cal = Calendar.getInstance();
            //converts back into date: http://stackoverflow.com/questions/7953725/how-to-convert-milliseconds-to-date-format-in-android
            long date = cal.getTimeInMillis();
            if (need) {
                myRef = database.getReference("need");
                checkRef = database.getReference("done");
            } else {
                myRef = database.getReference("done");
                checkRef = database.getReference("need");
            }
            Request r = new Request("Reese", date);
            myRef.child(class_name).push().setValue(r);
            // check if new entry matches any entries in want
            getMatchedUsers(checkRef, class_name);
        }
//        // check if new entry matches any entries in want
//        getMatchedUsers(checkRef, class_name);
        else if (pageNumber != 100) {
            loadPage(nextPage);
        }
        else {
            finish();
        }


    }

    private void getMatchedUsers(DatabaseReference checkRef, String class_name) {
        final String class_query = class_name;
        ValueEventListener v = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                for (DataSnapshot currChild : ds.child(class_query).getChildren()) {
                    matchedRequests.add(currChild.getValue(Request.class));
                }
                evaluateNotifications();
                if (nextPage != 100) {
                    loadPage(nextPage);
                } else {
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        };
        if (nextPage != 100) {
            checkRef.addListenerForSingleValueEvent(v);
            checkRef.removeEventListener(v);
        }
    }

    private void evaluateNotifications() {
        if (!matchedRequests.isEmpty()) {
            for (Request matchedRequest : matchedRequests) {
                pushNotification(matchedRequest);
            }
            System.out.println("next page???? " + nextPage);
        } else {
            System.out.println("No matches were found.");
            nextPage = 2;
        }
    }

    // TODO: find out how to not use public List matchedRequest
    // TODO: Implement version of this function that actually pushes real notification.
    private void pushNotification(Request matchedRequest) {
        String userID = matchedRequest.getName();
        System.out.println("We're sending " + userID + " a message!");
        if (need) {pages[3].setText("You can email " + userID + " to get this book. Are you interested?");}
    }
}