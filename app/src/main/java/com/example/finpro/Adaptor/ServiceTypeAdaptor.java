package com.example.finpro.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finpro.Domain.ServiceTypeDomain;
import com.example.finpro.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ServiceTypeAdaptor extends RecyclerView.Adapter<ServiceTypeAdaptor.ViewHolder> {
    private ArrayList<ServiceTypeDomain> serviceTypeDomains;
    private Set<Integer> selectedServices = new HashSet<>();
    private boolean isSelectionEnabled = true;
    private OnServiceSelectedListener listener;

    public void clearSelections() {
        selectedServices.clear();
        notifyDataSetChanged();
    }

    public void updateServices(ArrayList<ServiceTypeDomain> newServices) {
        this.serviceTypeDomains = newServices;
        selectedServices.clear();
        notifyDataSetChanged();
    }

    public interface OnServiceSelectedListener {
        void onServiceSelected(ServiceTypeDomain service, boolean isSelected);
    }

    public void setSelectionEnabled(boolean enabled) {
        isSelectionEnabled = enabled;
        notifyDataSetChanged();
    }

    public ServiceTypeAdaptor(ArrayList<ServiceTypeDomain> serviceTypeDomains, OnServiceSelectedListener listener) {
        this.serviceTypeDomains = serviceTypeDomains;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_service_type, parent, false);
        return new ViewHolder(inflate);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceTypeDomain service = serviceTypeDomains.get(position);
        holder.serviceName.setText(service.getServiceName());
        holder.servicePrice.setText(service.getServicePrice());

        holder.addService.setImageResource(selectedServices.contains(position) ?
                R.drawable.check_circle : R.drawable.add_circle);

        holder.addService.setOnClickListener(v -> {
            if (!isSelectionEnabled) return;

            if (selectedServices.contains(position)) {
                selectedServices.remove(position);
                holder.addService.setImageResource(R.drawable.add_circle);
                listener.onServiceSelected(service, false);
            } else {
                selectedServices.add(position);
                holder.addService.setImageResource(R.drawable.check_circle);
                listener.onServiceSelected(service, true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceTypeDomains != null ? serviceTypeDomains.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName, servicePrice;
        ImageView addService;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            addService = itemView.findViewById(R.id.addService);
        }
    }

    public String getSelectedServices() {
        StringBuilder services = new StringBuilder();
        for (int position : selectedServices) {
            if (position < serviceTypeDomains.size()) {
                if (services.length() > 0) services.append(", ");
                services.append(serviceTypeDomains.get(position).getServiceName());
            }
        }
        return services.toString();
    }
}