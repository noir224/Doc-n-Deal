package com.example.docanddeal;

import android.view.View;
import android.widget.TextView;

public class WithinView {
    private View view;
    private TextView textView;

    public WithinView(View view, TextView textView) {
        this.view = view;
        this.textView = textView;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
