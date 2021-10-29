package com.example.keyboardvalut.data;

import java.util.ArrayList;
import java.util.List;

public class SpinnerQuestionData {

    public static List<String> questionsData() {
        List<String> questionsList = new ArrayList<>();

        questionsList.add("What is the name of your first school?");
        questionsList.add(" What was the make of your first car?");
        questionsList.add("What was your favorite food as a child?");


        return questionsList;
    }
}
