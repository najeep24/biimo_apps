package com.example.finpro.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finpro.Domain.DateDomain;
import com.example.finpro.R;

import java.util.ArrayList;

public class DateAdaptor extends RecyclerView.Adapter<DateAdaptor.ViewHolder> {
    ArrayList<DateDomain>dateDomains;

    public DateAdaptor(ArrayList<DateDomain> dateDomains) {
        this.dateDomains = dateDomains;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_date,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dayBook.setText(dateDomains.get(position).getDay());
        holder.dateBook.setText(dateDomains.get(position).getDate());
        String status = dateDomains.get(position).getStatus();

        if (status.equalsIgnoreCase("Available")) {
            holder.statusBook.setBackground(ContextCompat.getDrawable(
                    holder.itemView.getContext(), R.drawable.status_date_bg_available));
        } else if (status.equalsIgnoreCase("Not Available")) {
            holder.statusBook.setBackground(ContextCompat.getDrawable(
                    holder.itemView.getContext(), R.drawable.status_date_bg_not_available));
        }

    }

    @Override
    public int getItemCount() {
        return dateDomains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayBook;
        TextView dateBook;
        ConstraintLayout statusBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateBook=itemView.findViewById(R.id.dateBook);
            dayBook=itemView.findViewById(R.id.dayBook);
            statusBook=itemView.findViewById(R.id.statusBook);

        }
    }
}
