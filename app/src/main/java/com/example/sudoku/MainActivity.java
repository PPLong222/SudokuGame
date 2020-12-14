package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

public class MainActivity extends AppCompatActivity {
    private Button buttonStart;
    private Button buttonRecord;
    private Button buttonAbout;
    private LevelChoosePanel choosePanel;
    private ConstraintLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestLegacy();
        getLitepal();
        buttonStart=findViewById(R.id.button_start);
        buttonRecord=findViewById(R.id.button_record);
        buttonAbout=findViewById(R.id.button_about);
        choosePanel=findViewById(R.id.choosepanel);
        mainLayout=findViewById(R.id.main_layout);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout.LayoutParams params=new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                choosePanel.setVisibility(View.VISIBLE);
                choosePanel.bringToFront();
                //choosePanel.setLayoutParams(params);
                setAnimation();
            }
        });
        choosePanel.addListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePanel.setVisibility(View.INVISIBLE);
                unLockButton();
            }
        });

        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RecordShowActivity.class);
                startActivity(intent);
            }
        });
        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setAnimation(){
        ObjectAnimator panelAni=ObjectAnimator.ofFloat(choosePanel,"alpha",0f,1f);
        panelAni.setDuration(2000);
        panelAni.start();
        lockButton();
    }
    private void lockButton(){
        buttonStart.setVisibility(View.INVISIBLE);
        buttonRecord.setVisibility(View.INVISIBLE);
        buttonAbout.setVisibility(View.INVISIBLE);
    }
    private void unLockButton(){
        buttonStart.setVisibility(View.VISIBLE);
        buttonRecord.setVisibility(View.VISIBLE);
        buttonAbout.setVisibility(View.VISIBLE);
    }
    private void requestLegacy(){
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    }
    private void getLitepal(){
        Connector.getDatabase();
    }

}