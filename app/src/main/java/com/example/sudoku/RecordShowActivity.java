package com.example.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sudoku.recycleutil.RecordViewAdapter;

import org.litepal.crud.DataSupport;

import java.util.List;

public class RecordShowActivity extends AppCompatActivity {
    private Button button_back;
    private RecyclerView recyclerView;
    private List myRecords;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        recyclerView=findViewById(R.id.recycleview);
        //to ensure the recyclerview can be scrolled in pages ,rather than scrolling randomly
        PagerSnapHelper snapHelper=new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        button_back=findViewById(R.id.button_record_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecordShowActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        getConnectWithArray();
    }
    private void getConnectWithArray(){

        myRecords= DataSupport.findAll(RecordBitmap.class);
        // judge if we don't have any record

        RecordViewAdapter viewAdapter=new RecordViewAdapter(myRecords);
        LinearLayoutManager manager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(manager);
    }
}
