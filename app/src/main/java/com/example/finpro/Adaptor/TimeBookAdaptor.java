package com.example.finpro.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finpro.Activity.BookingServices;
import com.example.finpro.Activity.HomeServices;
import com.example.finpro.Domain.TimeBookDomain;
import com.example.finpro.R;

import java.util.ArrayList;

public class TimeBookAdaptor extends RecyclerView.Adapter<TimeBookAdaptor.ViewHolder> {
    private Context context;
    private ArrayList<TimeBookDomain> arrayList;
    private boolean isNewRadioButtonChecked = false;
    private int lastCheckedPosition = -1;

    public TimeBookAdaptor(BookingServices context, ArrayList<TimeBookDomain> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    public TimeBookAdaptor(HomeServices context, ArrayList<TimeBookDomain> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public TimeBookAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.viewholder_time, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeBookAdaptor.ViewHolder holder, int position) {
        TimeBookDomain timeBookDomain = arrayList.get(position);
        holder.textTimeBook.setText(timeBookDomain.getTime());

        if (isNewRadioButtonChecked){
            holder.radioButtonTimeBook.setChecked(timeBookDomain.isSelected());
        } else {
            if(holder.getAdapterPosition()==0){
                holder.radioButtonTimeBook.setChecked(true);
                lastCheckedPosition = 0;
            }
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

            this.textTimeBook = itemView.findViewById(R.id.textTimeBook);
            this.imageView19 = itemView.findViewById(R.id.imageView19);
            this.radioButtonTimeBook = itemView.findViewById(R.id.radioButtonTimeBook);
            this.rowTimeBook = itemView.findViewById(R.id.rowTimeBook);

            radioButtonTimeBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();

                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        handleRadiobuttonChecks(adapterPosition);
                    }
                }
            });
        }
    }

    private void handleRadiobuttonChecks(int adapterPosition){
        isNewRadioButtonChecked= true;
        arrayList.get(lastCheckedPosition).setSelected(false);
        arrayList.get(adapterPosition).setSelected(true);
        lastCheckedPosition = adapterPosition;
        notifyDataSetChanged();
    }
}

