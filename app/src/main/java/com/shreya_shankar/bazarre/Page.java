package com.shreya_shankar.bazarre;

/**
 * Created by shreyashankar on 6/27/16.
 */
public class Page {

    private String text;
    private Choice choice1, choice2;
    boolean need;

    public Page(String text, Choice choice1, Choice choice2) {
        this.text = text;
        this.choice1 = choice1;
        this.choice2 = choice2;
    }

    public Page(String text, Choice choice1, Choice choice2, boolean need) {
        this.text = text;
        this.need = need;
        this.choice1 = choice1;
        if (!need) {choice1 = new Choice(choice1.getText(), 6);}
        this.choice2 = choice2;
    }

    public Page(String text, Choice choice1) {
        this.text = text;
        this.choice1 = choice1;
        choice2 = null;
    }

    public boolean getNeed() {return need;}

    public String getText() {
        return text;
    }

    public void setText(String mText) {
        text = mText;
    }

    public Choice getChoice1() {
        return choice1;
    }

    public void setChoice1(Choice mChoice1) {
        choice1 = mChoice1;
    }

    public Choice getChoice2() {
        return choice2;
    }

    public void setChoice2(Choice mChoice2) {
        choice2 = mChoice2;
    }
}
