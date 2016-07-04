package com.shreya_shankar.bazarre;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.database.IgnoreExtraProperties;
/**
 * Created by shreyashankar on 7/4/16.
 */

@IgnoreExtraProperties
public class Request {

    public String class_name;
    public String request_type;

    @JsonIgnore
    public String key;

    public Request() {
        //default
    }

    public Request(String class_name, String request_type) {
        this.class_name = class_name;
        this.request_type = request_type;
    }

}
