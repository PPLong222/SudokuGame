package com.example.sudoku.recycleutil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sudoku.R;

import java.util.List;

public class RecycBlockAdapter extends RecyclerView.Adapter<RecycBlockAdapter.ViewHolder> {

    private ImageView[][] imageViews;

    public RecycBlockAdapter(ImageView[][] imageViews) {
        this.imageViews=imageViews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_block,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(R.drawable.zero);

        int row=position/9;
        int col=position%9;
        imageViews[row][col]=holder.imageView;
    }



    @Override
    public int getItemCount() {
        return 81;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.img_singleblock);
        }
    }
}
