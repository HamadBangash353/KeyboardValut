package com.example.keyboardvalut.data;

import java.util.ArrayList;
import java.util.List;

public class SpinnerQuestionData {

    public static List<String> questionsData() {
        List<String> questionsList = new ArrayList<>();

        questionsList.add("What was your first name");
        questionsList.add("What is your pet name");
        questionsList.add("What waw your car model");

        return questionsList;
    }
}
