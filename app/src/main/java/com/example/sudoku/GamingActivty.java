package com.example.sudoku;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

public class GamingActivty extends AppCompatActivity {
    private ConstraintLayout parentLayout;
    private ImageView originBlock_1;
    private ImageView originBlock_2;
    private ImageView originBlock_3;
    private ImageView originBlock_4;
    private ImageView originBlock_5;
    private ImageView originBlock_6;
    private ImageView originBlock_7;
    private ImageView originBlock_8;
    private ImageView originBlock_9;
    private ImageView[] originBlocks;
    private ImageView tempImg;
    private GridLayout gridLayout;
    private Button btn_reset;
    private Button btn_back;
    private LottieAnimationView lottie_end_1;
    private ConstraintLayout.LayoutParams templayout;
    private ImageView[][] blocks;
    private int[][] curNum;
    private int tempNum;
    private int count=0;
    private int cur_level=0;
    private Chronometer timer;
    private  AnimatorSet bloackAniSet;
    private Bitmap cur_bm;
    private int[] pictureNum={R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four,R.drawable.five,
            R.drawable.six,R.drawable.seven,R.drawable.eight,R.drawable.nine};

    public GamingActivty() {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);

        Intent intent=getIntent();
        cur_level=intent.getIntExtra("level",0);
        setUi();
        dynamicGenerateBlocks();
        //////
        int test[][]=new int[9][9];
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                test[i][j]=3;
            }
        }
        Log.d("1111"," "+cur_level);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initBlocks(SudoHelper.testNum);
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cur_bm=BitmapHelper.getBitmapFromView(gridLayout);
                    }
                },4000);
            }
        });

        //////





    }
    //set UI in activity
    private void setUi(){
        originBlock_1=findViewById(R.id.origin_block1);
        originBlock_2=findViewById(R.id.origin_block2);
        originBlock_3=findViewById(R.id.origin_block3);
        originBlock_4=findViewById(R.id.origin_block4);
        originBlock_5=findViewById(R.id.origin_block5);
        originBlock_6=findViewById(R.id.origin_block6);
        originBlock_7=findViewById(R.id.origin_block7);
        originBlock_8=findViewById(R.id.origin_block8);
        originBlock_9=findViewById(R.id.origin_block9);
        btn_back=findViewById(R.id.button_button_back);
        btn_reset=findViewById(R.id.button_reset);
        lottie_end_1=findViewById(R.id.lottie_end_1);
        parentLayout=findViewById(R.id.parent_layout);
        timer=findViewById(R.id.chro_timer);
        gridLayout=findViewById(R.id.gridlayout_main);


        setUiListener();
        startTimer();
    }


    //add Listeners to UI
    @SuppressLint("ClickableViewAccessibility")
    private void setUiListener(){
        originBlocks=new ImageView[]{originBlock_1,originBlock_2,originBlock_3,originBlock_4
        ,originBlock_5,originBlock_6,originBlock_7,originBlock_8,originBlock_9};
        for(int i=0;i<9;i++){

            originBlocks[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int j;


                    for(j=0;j<9;j++)
                        if(originBlocks[j]==v) break;


                    switch (event.getAction()){
                        case MotionEvent.ACTION_MOVE:
                            changeWidgetLocation(event.getRawX(),event.getRawY());
                            Log.d("1111","move"+event.getRawX()+"  "+event.getRawY());
                            break;
                        case MotionEvent.ACTION_DOWN:
                            /////////////
                            /* A abandoned ani method

                            if(tempNum!=j) {
                                ObjectAnimator blockAni_1 = ObjectAnimator.ofFloat(originBlocks[j], "alpha", 0f, 1f);
                                ObjectAnimator blockAni_2 = ObjectAnimator.ofFloat(originBlocks[j], "alpha", 1f, 0f);
                                blockAni_1.setDuration(1800);
                                blockAni_2.setDuration(500);
                                bloackAniSet=new AnimatorSet();

                                bloackAniSet.play(blockAni_2).before(blockAni_1);
                                bloackAniSet.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        bloackAniSet.start();
                                    }
                                });
                                bloackAniSet.start();
                            }

                             */
                            /////////////////////
                            generateWidget(event.getRawX(),event.getRawY(),j);
                            Log.d("1111","start"+event.getRawX()+"  "+event.getRawY());
                            break;
                        case MotionEvent.ACTION_UP:
                            parentLayout.removeView(tempImg);
                            changeBlock((int)event.getRawX(),(int)event.getRawY());
                            break;
                    }
                    return true;
                }
            });
        }
        // set Listener to button
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GamingActivty.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                timer.stop();
                startTimer();
                initBlocks(SudoHelper.generateSudoWithLevel(cur_level));
            }
        });
    }
    private void generateWidget(float x, float y,int i){
        tempNum=i;
        tempImg=new ImageView(this);
        tempImg.setImageResource(pictureNum[i]);


        templayout = new ConstraintLayout.LayoutParams(100, 100);
        templayout.height = 100;
        templayout.width = 100;
        tempImg.setX(x-100);
        tempImg.setY(y-100);
        tempImg.setLayoutParams(templayout);

        parentLayout.addView(tempImg);


    }
    private void changeWidgetLocation(float x,float y){
        tempImg.setX(x-100);
        tempImg.setY(y-100);
    }
    private void dynamicGenerateBlocks(){
        blocks=new ImageView[9][9];
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                blocks[i][j]=new ImageView(this);
                GridLayout.LayoutParams param=new GridLayout.LayoutParams();
                param.height=120;
                param.width=110;
                param.rowSpec=GridLayout.spec(i);
                param.columnSpec=GridLayout.spec(j);
                param.setMargins(0,2,1,4);
                param.setGravity(Gravity.CENTER);


                gridLayout.addView(blocks[i][j],param);
            }
        }
    }
    private void changeBlock(int x,int y){
        //130 920  247 920
        //1045
        for(int i=0;i<9;i++){
            if(112*i+130<x+30&&112*i+130>x-30){
                for(int j=0;j<9;j++){
                    if(125*j+920>y-40&&125*j+920<y+40){
                        if((int)blocks[j][i].getTag()==SudoHelper.BLOCK_NOTOORIGIN) {


                            Log.d("111"," "+tempNum);

                            if(SudoHelper.isValid(curNum,j,i,tempNum+1)){
                                curNum[j][i]=tempNum+1;
                                blocks[j][i].setImageResource(pictureNum[tempNum]);
                                if(isFull(curNum)){
                                    if(SudoHelper.solveSudoku(curNum)){
                                        timer.stop();
                                        saveImg();
                                        lottie_end_1.setAnimation("congratu_1.json");
                                        lottie_end_1.setVisibility(View.VISIBLE);
                                        lottie_end_1.playAnimation();
                                        lottie_end_1.addAnimatorListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                lottie_end_1.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                    }
                                }

                            }else{
                                curNum[j][i]=tempNum+1;
                                blocks[j][i].setImageResource(pictureNum[tempNum]);
                                final WrongBorder wrongBorder = new WrongBorder(this);
                                ConstraintLayout.LayoutParams params=new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                wrongBorder.setX(130+112*(i-1));
                                wrongBorder.setY(920+125*(j-1));
                                parentLayout.addView(wrongBorder,params);

                                ObjectAnimator wrongAni_show=ObjectAnimator.ofFloat(wrongBorder,"alpha",0f,1f);
                                ObjectAnimator wrongAni_off=ObjectAnimator.ofFloat(wrongBorder,"alpha",1f,0f);
                                wrongAni_off.setDuration(2000);
                                wrongAni_show.setDuration(700);
                                wrongAni_off.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        //delete to avoid surplus storage usage
                                        parentLayout.removeView(wrongBorder);

                                    }
                                });
                                AnimatorSet animatorSet=new AnimatorSet();
                                animatorSet.play(wrongAni_show).before(wrongAni_off);
                                animatorSet.start();
                            }

                            /////



                        }
                        else {
                            Toast.makeText(this, "This block is origin!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initBlocks(final int origin[][]){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                curNum=new int[9][9];
                for(int i=0;i<9;i++){
                    for(int j=0;j<9;j++){
                        curNum[i][j]=origin[i][j];
                        if(origin[i][j]!=0){
                            blocks[i][j].setImageResource(pictureNum[origin[i][j]-1]);
                            blocks[i][j].setTag(SudoHelper.BLOCK_ISORIGIN);


                        }else{
                            blocks[i][j].setImageResource(R.drawable.zero);
                            blocks[i][j].setTag(SudoHelper.BLOCK_NOTOORIGIN);

                        }
                    }
                }

            }
        });


    }
    private void startTimer(){
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
    }
    //A FOOL METHOD
    private boolean isFull(int num[][]){
        for(int k=0;k<9;k++){
            for(int m=0;m<9;m++){
                if(num[k][m]==0)
                    return false;
            }
        }
        return true;
    }



    ////SAVE IMG
    private void saveImg(){
        Connector.getDatabase();
        RecordBitmap newRecord=new RecordBitmap();


        Bitmap img=BitmapHelper.getBitmapFromView(gridLayout);
        Date date=new Date();


        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
        String bm_answer="level_"+cur_level+"answer"+simpleDateFormat.format(date);
        String bm_origin="level_"+cur_level+"origin"+simpleDateFormat.format(date);
        newRecord.setDate(""+simpleDateFormat.format(date));
        newRecord.setCompletedate(""+timer.getText());
        newRecord.setLevel(cur_level);
        newRecord.setAnswerurl( ""+BitmapHelper.saveImg(img,bm_answer,this));
        newRecord.setOriginurl( ""+BitmapHelper.saveImg(cur_bm,bm_origin,this));

        Log.d("111",""+ newRecord.save());
        Log.d("111",""+ newRecord.isSaved());

    }






}
