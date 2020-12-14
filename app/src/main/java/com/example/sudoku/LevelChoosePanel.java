package com.example.sudoku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LevelChoosePanel extends ConstraintLayout {
    public Button btn_easy;
    public  Button btn_normal;
    public Button btn_dif;
    public Button btn_back;
    private View view;




    public LevelChoosePanel(@NonNull final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        view=View.inflate(context,R.layout.level_chose_layout,this);
        btn_dif=view.findViewById(R.id.button_level_dif);
        btn_easy=view.findViewById(R.id.button_level_easy);
        btn_normal=view.findViewById(R.id.button_level_normal);
        btn_back=view.findViewById(R.id.btn_panelBack);
        btn_easy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),GamingActivty.class);
                intent.putExtra("level",SudoHelper.LEVLE_EASY);
                v.getContext().startActivity(intent);
            }
        });
        btn_normal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),GamingActivty.class);
                intent.putExtra("level",SudoHelper.LEVEL_NORMAL);
                v.getContext().startActivity(intent);
            }
        });;
        btn_dif.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),GamingActivty.class);
                intent.putExtra("level",SudoHelper.LEVLE_DIF);
                v.getContext().startActivity(intent);
            }
        });
    }
    public void addListener(OnClickListener onClickListener){
        btn_back.setOnClickListener(onClickListener);
    }


}
