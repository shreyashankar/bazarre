package com.shreya_shankar.bazarre;

/**
 * Created by shreyashankar on 6/27/16.
 */
public class Choice {

    private String text;
    private int nextPage;

    public Choice (String text, int nextPage) {
        this.text = text;
        this.nextPage = nextPage;
    }

    public String getText() {
        return text;
    }

    public void setText(String mText) {
        text = mText;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int mNextPage) {
        nextPage = mNextPage;
    }
}