package com.example.finpro.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finpro.Adaptor.DateAdaptor;
import com.example.finpro.Adaptor.ServiceTypeAdaptor;
import com.example.finpro.Domain.DateDomain;
import com.example.finpro.Domain.ServiceTypeDomain;
import com.example.finpro.R;

import java.util.ArrayList;

public class ServiceType extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type);

        recyclerViewType();
    }

    private void recyclerViewType() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerViewType=findViewById(R.id.recyclerViewType);
        recyclerViewType.setLayoutManager(linearLayoutManager);

        ArrayList<ServiceTypeDomain> type=new ArrayList<>();
        type.add(new ServiceTypeDomain("Service Berkala","Rp 50.000"));
        type.add(new ServiceTypeDomain("Isi Freon AC","Rp 50.000"));
        type.add(new ServiceTypeDomain("Pemasangan GPS","Rp 50.000"));
        type.add(new ServiceTypeDomain("Jasa Ganti Lampu","Rp 50.000"));
        type.add(new ServiceTypeDomain("Service Interior","Rp 50.000"));
        type.add(new ServiceTypeDomain("Turun Mesin","Rp 50.000"));
        type.add(new ServiceTypeDomain("Turun Mesin","Rp 50.000"));
        type.add(new ServiceTypeDomain("Turun Mesin","Rp 50.000"));
        type.add(new ServiceTypeDomain("Turun Mesin","Rp 50.000"));
        type.add(new ServiceTypeDomain("Turun Mesin","Rp 50.000"));
        type.add(new ServiceTypeDomain("Turun Mesin","Rp 50.000"));
        type.add(new ServiceTypeDomain("Turun Mesin","Rp 50.000"));
        type.add(new ServiceTypeDomain("Turun Mesin","Rp 50.000"));
        type.add(new ServiceTypeDomain("Turun Mesin","Rp 50.000"));
        type.add(new ServiceTypeDomain("Turun Mesin","Rp 50.000"));

        adapter=new ServiceTypeAdaptor(type);
        recyclerViewType.setAdapter(adapter);

    }
}
