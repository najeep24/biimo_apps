package com.example.finpro.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finpro.Domain.TimeBookDomain;
import com.example.finpro.R;
import java.util.ArrayList;
import java.util.List;

public class TimeBookAdaptor extends RecyclerView.Adapter<TimeBookAdaptor.ViewHolder> {
    private Context context;
    private ArrayList<TimeBookDomain> arrayList;
    private boolean isNewRadioButtonChecked = false;
    private int lastCheckedPosition = -1;

    public TimeBookAdaptor(Context context, ArrayList<TimeBookDomain> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public void updateTimeSlots(List<TimeBookDomain> newTimeSlots) {
        arrayList.clear();
        arrayList.addAll(newTimeSlots);
        notifyDataSetChanged();
    }

    public void selectFirstTimeSlot() {
        if (!arrayList.isEmpty()) {
            lastCheckedPosition = 0;
            arrayList.get(0).setSelected(true);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_time, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeBookDomain timeBookDomain = arrayList.get(position);
        holder.textTimeBook.setText(timeBookDomain.getTime());
        holder.radioButtonTimeBook.setChecked(timeBookDomain.isSelected());

        if (isNewRadioButtonChecked) {
            holder.radioButtonTimeBook.setChecked(timeBookDomain.isSelected());
        } else if (holder.getAdapterPosition() == 0) {
            holder.radioButtonTimeBook.setChecked(true);
            lastCheckedPosition = 0;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textTimeBook;
        private ConstraintLayout rowTimeBook;
        private RadioButton radioButtonTimeBook;
        private ImageView imageView19;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTimeBook = itemView.findViewById(R.id.textTimeBook);
            imageView19 = itemView.findViewById(R.id.imageView19);
            radioButtonTimeBook = itemView.findViewById(R.id.radioButtonTimeBook);
            rowTimeBook = itemView.findViewById(R.id.rowTimeBook);

            radioButtonTimeBook.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    handleRadiobuttonChecks(adapterPosition);
                }
            });
        }
    }

    private void handleRadiobuttonChecks(int adapterPosition) {
        isNewRadioButtonChecked = true;
        arrayList.get(lastCheckedPosition).setSelected(false);
        arrayList.get(adapterPosition).setSelected(true);
        lastCheckedPosition = adapterPosition;
        notifyDataSetChanged();
    }

    public String getSelectedTime() {
        if (lastCheckedPosition >= 0 && lastCheckedPosition < arrayList.size()) {
            return arrayList.get(lastCheckedPosition).getTime();
        }
        return "";
    }
}

