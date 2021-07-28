package com.example.keyboardvalut.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.interfaces.SecurityQuestionCallback;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {
    Activity context;
    List<String> questionsList;

    SecurityQuestionCallback callback;

    public SpinnerAdapter(Context context, int textViewResourceId, Activity myActivity, List<String> questionList) {
        super(context, textViewResourceId, questionList);

        this.context = myActivity;
        this.questionsList = questionList;
        callback = (SecurityQuestionCallback) context;
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.spinner_layout, parent, false);

        TextView tvLanguage = layout.findViewById(R.id.tvQuestion);

        tvLanguage.setText(questionsList.get(position));

        callback.onSecurityQuestionCallback(questionsList.get(position));


        return layout;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}

