package com.moaapps.mathquiz.adapters;

import android.app.AlertDialog;

import androidx.cardview.widget.CardView;

public class MyReturnClass {

    private CardView[] mMyButtons;
    private AlertDialog mMyDialog;

    public MyReturnClass(CardView[] mMyButtons, AlertDialog mMyDialog) {
        this.mMyButtons = mMyButtons;
        this.mMyDialog = mMyDialog;
    }

    public CardView[] getmMyButtons() {
        return mMyButtons;
    }

    public AlertDialog getmMyDialog() {
        return mMyDialog;
    }
}
