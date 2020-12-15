package com.example.sudoku;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class WrongBorder extends ConstraintLayout {
    private View view;

    //wrongborder view class
    public WrongBorder(@NonNull Context context) {
        super(context);
        view = View.inflate(context, R.layout.wrong_border, this);
    }

}
