package com.example.finpro.Adaptor;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.List;
import com.example.finpro.R;

public class SpinnerAdaptor {
    public static void setupSpinner(Context context, Spinner spinner, List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                R.layout.custom_spinner_item,  // Using custom layout here
                items
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);  // Using custom dropdown layout
        spinner.setAdapter(adapter);
    }
}