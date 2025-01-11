package com.example.finpro.Adaptor;

import android.graphics.Color;
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
import java.util.List;

public class DateAdaptor extends RecyclerView.Adapter<DateAdaptor.ViewHolder> {
    private ArrayList<DateDomain> dateDomains;
    private OnDateClickListener listener;
    private int selectedPosition = -1;
    private DateDomain selectedDate;

    public DateDomain getSelectedDate() {
        return selectedDate;
    }

    public interface OnDateClickListener {
        void onDateClick(DateDomain date);
    }

    public DateAdaptor(ArrayList<DateDomain> dateDomains) {
        this.dateDomains = dateDomains;
    }

    public void setOnDateClickListener(OnDateClickListener listener) {
        this.listener = listener;
    }

    public void updateDates(List<DateDomain> newDates) {
        this.dateDomains.clear();
        this.dateDomains.addAll(newDates);
        notifyDataSetChanged();  // Notify the adapter to refresh the list
    }

    public void setSelectedDate(DateDomain date) {
        this.selectedDate = date;
        int position = -1;
        for (int i = 0; i < dateDomains.size(); i++) {
            if (dateDomains.get(i).getDate().equals(date.getDate())) {
                position = i;
                break;
            }
        }

        if (position != -1) {
            selectedPosition = position;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_date, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateDomain date = dateDomains.get(position);
        holder.dayBook.setText(date.getDay());
        holder.dateBook.setText(date.getDate());

        String status = date.getStatus();
        if (status.equalsIgnoreCase("Available")) {
            holder.statusBook.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.status_date_bg_available));

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    selectedPosition = holder.getAdapterPosition();
                    listener.onDateClick(date);
                    notifyDataSetChanged();
                }
            });
        } else if (status.equalsIgnoreCase("Not Available")) {
            holder.statusBook.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.status_date_bg_not_available));
            holder.itemView.setOnClickListener(null);  // Disable click for unavailable dates
        }

        if (selectedPosition == position && status.equalsIgnoreCase("Available")) {
            holder.mainLayout.setBackgroundResource(R.drawable.date_book_cheked);
            holder.dayBook.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.dateBook.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        } else {
            holder.mainLayout.setBackgroundResource(R.drawable.date_book_normal);
            holder.dayBook.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.dark_gray));
            holder.dateBook.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.dark_gray));
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
        ConstraintLayout mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateBook = itemView.findViewById(R.id.dateBook);
            dayBook = itemView.findViewById(R.id.dayBook);
            statusBook = itemView.findViewById(R.id.statusBook);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}