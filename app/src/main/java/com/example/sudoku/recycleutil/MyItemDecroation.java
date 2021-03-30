package com.example.sudoku.recycleutil;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyItemDecroation extends RecyclerView.ItemDecoration {
    private Paint myPaint;
    public MyItemDecroation() {
        super();
        myPaint=new Paint();
        myPaint.setColor(Color.BLACK);
    }

    @Override
    // to create border to each box view in recyclerview
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int count=parent.getChildCount();

        for(int i=0;i<count;i++){
            View v=parent.getChildAt(i);
            Rect rect_r=new Rect((int)v.getX(),(int)v.getY(),(int)v.getX()+v.getWidth(),(int)v.getY()+4);
            c.drawRect(rect_r,myPaint);
            Rect rect_c=new Rect((int)v.getX()+v.getWidth(),(int)v.getY(),(int)v.getX()+v.getWidth()+4,(int)v.getY()+v.getHeight());
            c.drawRect(rect_c,myPaint);

            if(i>=9*8){
                Rect rect=new Rect((int)v.getX(),(int)v.getY()+v.getHeight()-4,(int)v.getX()+v.getWidth(),(int)v.getY()+v.getHeight());
                c.drawRect(rect,myPaint);
            }
            if(i%9==0){
                myPaint.setStrokeWidth(8);
                c.drawLine(v.getX(),v.getY(),v.getX(),v.getY()+v.getHeight(),myPaint);
            }
            if(i%9==8){
                myPaint.setStrokeWidth(8);
                c.drawLine(v.getX()+v.getWidth(),v.getY(),v.getX()+v.getWidth(),v.getY()+v.getHeight(),myPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

    }
}
