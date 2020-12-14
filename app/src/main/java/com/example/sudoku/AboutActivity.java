package com.example.sudoku;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class AboutActivity extends AppCompatActivity {
    private LottieAnimationView lottie_man;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        lottie_man=findViewById(R.id.lottie_littleman);
        lottie_man.setAnimation("littleman.json");
        lottie_man.playAnimation();

        lottieAni();
    }
    private void lottieAni(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final ObjectAnimator manRight=ObjectAnimator.ofFloat(lottie_man,"x",-800,1200);
                final ObjectAnimator manLeft=ObjectAnimator.ofFloat(lottie_man,"x",1200,-800);
                manRight.setDuration(6000);
                manLeft.setDuration(6000);
                manRight.start();
                manRight.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ObjectAnimator.ofFloat(lottie_man,"rotationY",0,180f).setDuration(0).start();
                        manLeft.start();
                    }
                });
                manLeft.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ObjectAnimator.ofFloat(lottie_man,"rotationY",180f,0).setDuration(0).start();
                        manRight.start();
                    }
                });
            }
        });
    }
}
