package com.example.finpro.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finpro.Adaptor.DateAdaptor;
import com.example.finpro.Adaptor.TimeBookAdaptor;
import com.example.finpro.Domain.DateDomain;
import com.example.finpro.Domain.TimeBookDomain;
import com.example.finpro.R;

import java.util.ArrayList;

public class BookingServices extends AppCompatActivity {

    private TextView next;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewDate;
    RecyclerView recyclerView;
    ArrayList<TimeBookDomain> arrayList = new ArrayList<>();
    String[] data = new String[]{"09.00", "10.00", "11.00", "13.00", "14.00","15.00","16.00"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_services);

        next = findViewById(R.id.Next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingServices.this, SummaryOrder.class);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.RecyclerViewTimeBook);
        TimeBookAdaptor timeBookAdaptor = new TimeBookAdaptor(this, getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(timeBookAdaptor);

        recyclerViewDate();
    }

    private ArrayList<TimeBookDomain> getData(){
        for (int i = 0; i<data.length; i++){
            arrayList.add(new TimeBookDomain(data[i], false));
        }
        return arrayList;
    }

    private void recyclerViewDate() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDate=findViewById(R.id.RecyclerViewBs);
        recyclerViewDate.setLayoutManager(linearLayoutManager);

        ArrayList<DateDomain> date=new ArrayList<>();
        date.add(new DateDomain("Senin","1","Available"));
        date.add(new DateDomain("Selasa","2","Available"));
        date.add(new DateDomain("Rabu","3","Not Available"));
        date.add(new DateDomain("Kamis","4","Available"));
        date.add(new DateDomain("Jumat","5","Not Available"));
        date.add(new DateDomain("Sabtu","6","Available"));
        date.add(new DateDomain("Minggu","7","Available"));

        adapter=new DateAdaptor(date);
        recyclerViewDate.setAdapter(adapter);
    }
}
