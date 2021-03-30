package com.example.sudoku;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.sudoku.recycleutil.MyItemDecroation;
import com.example.sudoku.recycleutil.RecycBlockAdapter;

import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.Date;


public class GamingActivty extends AppCompatActivity {
    private ConstraintLayout parentLayout;  //parent layout
    //the initialization of originblock could have a better way like array...
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
    private ImageView tempImg;              //img when moving
    private Button btn_reset;
    private Button btn_back;
    private LottieAnimationView lottie_end_1;
    private ConstraintLayout.LayoutParams templayout;
    private ImageView[][] blocks;           //all blocks (9*9)
    private int[][] curNum;                 //the current logic numbers
    private int[][] answerNum;              //final numbers of the curNum
    private int tempNum;                    //number of fingers moving
    private int cur_level = 0;                //current level
    private Chronometer timer;              //to calculate the time the game spend
    private Button btn_showAnswer;
    private RecyclerView recycle_blocks;    //recyclerview to hold all blocks
    private Bitmap cur_bm;                  //bitmap to store current view bitmap
    //rec_x:the x of recyclerview in all screen(left top)
    //rec_y:the y of recyclerview in all screen(right bottom)
    //height_tran: length of each child view in recyclerview in height
    //width_tran:  length of each child view in recyclerview in width
    private int rec_x, rec_y, height_tran, width_tran;
    //int Id to save pictures of numbers in AS storage (Id)
    private int[] pictureNum = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five,
            R.drawable.six, R.drawable.seven, R.drawable.eight, R.drawable.nine};

    public GamingActivty() {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);
        //get param sending from the last activity
        Intent intent = getIntent();
        cur_level = intent.getIntExtra("level", 0);

        setUi();
        dynamicGenerateBlocks();
        //asynchronous step to run a relatively time-consuming task
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initBlocks(SudoHelper.generateSudoWithLevel(cur_level));
                        Handler handler_1 = new Handler();
                        handler_1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cur_bm = BitmapHelper.getBitmapFromView(recycle_blocks);
                            }
                        }, 300);
                    }
                }, 700);

                //to save the bitmap in asynchronous step
                //could have a better way to handle this
            }
        });


    }

    //set UI in activity
    private void setUi() {
        originBlock_1 = findViewById(R.id.origin_block1);
        originBlock_2 = findViewById(R.id.origin_block2);
        originBlock_3 = findViewById(R.id.origin_block3);
        originBlock_4 = findViewById(R.id.origin_block4);
        originBlock_5 = findViewById(R.id.origin_block5);
        originBlock_6 = findViewById(R.id.origin_block6);
        originBlock_7 = findViewById(R.id.origin_block7);
        originBlock_8 = findViewById(R.id.origin_block8);
        originBlock_9 = findViewById(R.id.origin_block9);
        btn_back = findViewById(R.id.button_button_back);
        btn_showAnswer = findViewById(R.id.button_showanswer);
        btn_reset = findViewById(R.id.button_reset);
        lottie_end_1 = findViewById(R.id.lottie_end_1);
        parentLayout = findViewById(R.id.parent_layout);
        timer = findViewById(R.id.chro_timer);
        recycle_blocks = findViewById(R.id.recycleview_blocks);

        setUiListener();
        startTimer();
    }


    //add Listeners to UI
    @SuppressLint("ClickableViewAccessibility")
    private void setUiListener() {
        originBlocks = new ImageView[]{originBlock_1, originBlock_2, originBlock_3, originBlock_4
                , originBlock_5, originBlock_6, originBlock_7, originBlock_8, originBlock_9};
        for (int i = 0; i < 9; i++) {
            //add touchListener to each originBlocks
            originBlocks[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int j;
                    //find the originNum when click the origin block
                    for (j = 0; j < 9; j++)
                        if (originBlocks[j] == v) break;

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            changeWidgetLocation(event.getRawX(), event.getRawY());
                            break;

                        case MotionEvent.ACTION_DOWN:
                            generateWidget(event.getRawX(), event.getRawY(), j);
                            break;

                        case MotionEvent.ACTION_UP:
                            //remove the tempImg in parent view
                            parentLayout.removeView(tempImg);
                            changeBlock((int) event.getRawX(), (int) event.getRawY());
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
                Intent intent = new Intent(GamingActivty.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                timer.stop();
                initBlocks(SudoHelper.generateSudoWithLevel(cur_level));
                startTimer();
            }
        });
        /**
         * find the blocks which num equals 0 and fill pictures of right num temporarily
         *
         */
        btn_showAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (curNum[i][j] == 0) {
                            final ImageView iv = blocks[i][j];
                            blocks[i][j].setImageResource(pictureNum[answerNum[i][j] - 1]);
                            //add property animation to the block
                            ObjectAnimator ani = ObjectAnimator.ofFloat(blocks[i][j], "alpha", 0f, 1f, 0f, 1f, 0f);
                            ani.setDuration(3500);
                            ani.start();
                            ani.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    iv.setImageResource(R.drawable.zero);
                                    iv.setAlpha(1f);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    /**
     * to generate the tempImg view when touching origin block
     */
    private void generateWidget(float x, float y, int i) {
        tempNum = i;
        tempImg = new ImageView(this);
        tempImg.setImageResource(pictureNum[i]);

        templayout = new ConstraintLayout.LayoutParams(100, 100);
        templayout.height = 100;
        templayout.width = 100;
        tempImg.setX(x - 100);
        tempImg.setY(y - 100);
        tempImg.setLayoutParams(templayout);
        parentLayout.addView(tempImg);
    }

    //change location of tempimg
    private void changeWidgetLocation(float x, float y) {
        tempImg.setX(x - 100);
        tempImg.setY(y - 100);
    }

    /**
     * dynamic create whole recyclerview and initial blocks when first creating
     */
    private void dynamicGenerateBlocks() {
        blocks = new ImageView[9][9];
        //add Decroation to recyclerview
        MyItemDecroation itemDecroation = new MyItemDecroation();
        //make style of recyclerview to ensure it's 9*9
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(9, StaggeredGridLayoutManager.VERTICAL);
        RecycBlockAdapter adapter = new RecycBlockAdapter(blocks);
        recycle_blocks.setLayoutManager(manager);
        recycle_blocks.addItemDecoration(itemDecroation);
        recycle_blocks.setAdapter(adapter);
        /*to add global listener to recyclerview
          trigger methods when recyclerview drown first
          initialize the blocks[][]
         */
        recycle_blocks.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
               /* DisplayMetrics displayMetrics=getResources().getDisplayMetrics();
                int  width=displayMetrics.widthPixels;
                int  height=displayMetrics.heightPixels;*/
                height_tran = (recycle_blocks.getBottom() - recycle_blocks.getTop()) / 9;
                Log.d("heightt",height_tran*9+"");
                width_tran = (recycle_blocks.getRight() - recycle_blocks.getLeft()) / 9;
                Log.d("heightt",width_tran*9+"");
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        ImageView imageView = recycle_blocks.getChildViewHolder(recycle_blocks.getChildAt(i * 9 + j)).itemView.findViewById(R.id.img_singleblock);
                        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();

                        layoutParams.height = height_tran-3 ;
                        layoutParams.width = width_tran - 3 ;
                        imageView.setLayoutParams(layoutParams);
                        blocks[i][j] = imageView;
                    }
                }

                //remove this listener
                recycle_blocks.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //initialize variable of recyclerview

                rec_x = recycle_blocks.getLeft();
                rec_y = recycle_blocks.getTop();
            }
        });
    }

    /**
     * to search and detect the relative block of current position
     *
     * @param x the current x of view
     * @param y the current y of view
     *          not good writing style :
     *          60:   ErrorTolerance
     *          SudoHelper.BLOCK_NOTOORIGIN: to ensure it's not a origin num
     */
    private void changeBlock(int x, int y) {
        for (int i = 0; i < 9; i++) {
            if (x > width_tran * (i + 1) + rec_x - 60 && x < width_tran * (i + 1) + rec_x + 60) {
                for (int j = 0; j < 9; j++) {
                    if (height_tran * (j + 1) + rec_y + 60 > y && height_tran * (j + 1) + rec_y - 60 < y) {
                        if ((int) blocks[j][i].getTag() == SudoHelper.BLOCK_NOTOORIGIN) {
                            //a another way of dealing this
                            //if(SudoHelper.isValid(curNum, j, i, tempNum + 1)) --but it's much slower
                            if (SudoHelper.isValid(curNum, j, i, tempNum + 1)) {
                                curNum[j][i] = tempNum + 1;
                                blocks[j][i].setImageResource(pictureNum[tempNum]);
                                //judge if numbers have 0?
                                if (isFull(curNum)) {
                                    // like the writing style above ,can write in:
                                    // if (SudoHelper.solveSudoku(curNum, 0)) { --but no need to add this
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

                            } else {
                                curNum[j][i] = 0;
                                final ImageView temp = blocks[j][i];
                                blocks[j][i].setImageResource(pictureNum[tempNum]);
                                ObjectAnimator block_off = ObjectAnimator.ofFloat(temp, "alpha", 1f, 0f);
                                block_off.setDuration(2500);
                                block_off.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        temp.setImageResource(R.drawable.zero);
                                        temp.setAlpha(1f);
                                    }
                                });

                                block_off.start();
                                //dynamically set wrongborder view to current child view to show its wrong
                                final WrongBorder wrongBorder = new WrongBorder(this);
                                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                wrongBorder.setX(rec_x + width_tran * (i));
                                wrongBorder.setY(rec_y + height_tran * (j) + 20);
                                parentLayout.addView(wrongBorder, params);

                                ObjectAnimator wrongAni_show = ObjectAnimator.ofFloat(wrongBorder, "alpha", 0f, 1f);
                                ObjectAnimator wrongAni_off = ObjectAnimator.ofFloat(wrongBorder, "alpha", 1f, 0f);
                                wrongAni_off.setDuration(2000);
                                wrongAni_show.setDuration(700);
                                wrongAni_off.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        //delete to avoid surplus storage usage
                                        parentLayout.removeView(wrongBorder);

                                    }
                                });
                                AnimatorSet animatorSet = new AnimatorSet();
                                animatorSet.play(wrongAni_show).before(wrongAni_off);
                                animatorSet.start();
                            }

                        } else {
                            Toast.makeText(this, "This block is origin!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initBlocks(final int origin[][]) {
        //initialize
        answerNum = new int[9][9];
        curNum = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                curNum[i][j] = origin[i][j];
                answerNum[i][j] = origin[i][j];
                if (origin[i][j] != 0) {
                    blocks[i][j].setImageResource(pictureNum[origin[i][j] - 1]);
                    blocks[i][j].setTag(SudoHelper.BLOCK_ISORIGIN);

                } else {
                    blocks[i][j].setImageResource(R.drawable.zero);
                    blocks[i][j].setTag(SudoHelper.BLOCK_NOTOORIGIN);

                }
            }
            cur_bm = BitmapHelper.getBitmapFromView(recycle_blocks);

        }
        SudoHelper.solveSudoku(answerNum, 0,-1,-1);


    }

    private void startTimer() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
    }

    //A FOOL METHOD to judge if num[][] have any 0 element
    private boolean isFull(int num[][]) {
        for (int k = 0; k < 9; k++) {
            for (int m = 0; m < 9; m++) {
                if (num[k][m] == 0)
                    return false;
            }
        }
        return true;
    }


    //method to SAVE IMG
    private void saveImg() {
        Connector.getDatabase();
        RecordBitmap newRecord = new RecordBitmap();

        Bitmap img = BitmapHelper.getBitmapFromView(recycle_blocks);

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
        String bm_answer = "level_" + cur_level + "answer" + simpleDateFormat.format(date);
        String bm_origin = "level_" + cur_level + "origin" + simpleDateFormat.format(date);

        newRecord.setDate("" + simpleDateFormat.format(date));
        newRecord.setCompletedate("" + timer.getText());
        newRecord.setLevel(cur_level);
        newRecord.setAnswerurl("" + BitmapHelper.saveImg(img, bm_answer, this));
        newRecord.setOriginurl("" + BitmapHelper.saveImg(cur_bm, bm_origin, this));
        Log.d("uriimg1","123"+newRecord.getAnswerurl());
        newRecord.save();
    }

    private int getPixelsFromDp(int size){

        DisplayMetrics metrics =new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.d("heightt",""+metrics.density);
        return(size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;

    }
}
