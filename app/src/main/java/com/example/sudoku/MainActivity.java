package com.example.sudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

public class MainActivity extends AppCompatActivity {
    private LottieAnimationView lottie_Start;
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
        lottie_Start = findViewById(R.id.lottie_start);
        buttonRecord = findViewById(R.id.button_record);
        buttonAbout = findViewById(R.id.button_about);
        choosePanel = findViewById(R.id.choosepanel);
        mainLayout = findViewById(R.id.main_layout);

        //make the choosePanel can be seen
        lottie_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePanel.setVisibility(View.VISIBLE);
                choosePanel.bringToFront();
                //choosePanel.setLayoutParams(params);
                setAnimation();
            }
        });
        // when get back from choosepanel , unlock these buttons
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
                Intent intent = new Intent(MainActivity.this, RecordShowActivity.class);
                startActivity(intent);
            }
        });
        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setAnimation() {
        ObjectAnimator panelAni = ObjectAnimator.ofFloat(choosePanel, "alpha", 0f, 1f);
        panelAni.setDuration(2000);
        panelAni.start();
        lockButton();
    }

    private void lockButton() {
        lottie_Start.setVisibility(View.INVISIBLE);
        buttonRecord.setVisibility(View.INVISIBLE);
        buttonAbout.setVisibility(View.INVISIBLE);
    }

    private void unLockButton() {
        lottie_Start.setVisibility(View.VISIBLE);
        buttonRecord.setVisibility(View.VISIBLE);
        buttonAbout.setVisibility(View.VISIBLE);
    }

    // request legacy for storage to read and save
    // for further steps of reading photo and save data
    private void requestLegacy() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    //get LitePal connection
    private void getLitepal() {
        Connector.getDatabase();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
        }
    }
}