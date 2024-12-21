package com.example.finpro.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finpro.Domain.DateDomain;
import com.example.finpro.Domain.ServiceTypeDomain;
import com.example.finpro.R;

import java.util.ArrayList;

public class ServiceTypeAdaptor extends RecyclerView.Adapter<ServiceTypeAdaptor.ViewHolder> {
    ArrayList<ServiceTypeDomain> serviceTypeDomains;

    public ServiceTypeAdaptor(ArrayList<ServiceTypeDomain> serviceTypeDomains) {
        this.serviceTypeDomains = serviceTypeDomains;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_service_type, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.serviceName.setText(serviceTypeDomains.get(position).getServiceName());
        holder.servicePrice.setText(serviceTypeDomains.get(position).getServicePrice());

    }

    @Override
    public int getItemCount() {
        return serviceTypeDomains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName;
        TextView servicePrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            servicePrice = itemView.findViewById(R.id.servicePrice);

        }
    }
}
