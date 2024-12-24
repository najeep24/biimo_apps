package com.example.finpro.Adaptor;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.List;

public class SpinnerAdaptor {
    public static void setupSpinner(Context context, Spinner spinner, List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}