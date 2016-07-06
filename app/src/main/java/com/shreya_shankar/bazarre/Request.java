package com.shreya_shankar.bazarre;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.database.IgnoreExtraProperties;
/**
 * Created by shreyashankar on 7/4/16.
 */

@IgnoreExtraProperties
public class Request {

    public String name;
    public long date;

    @JsonIgnore
    public String key;

    public Request() {
        //default
    }

    public Request(String name, long date) {
        this.name = name;
        this.date = date;
    }

    public String getName() { return name; }
    public long getDate() { return date; }

}
