package com.example.finpro.Viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailInformationHomeViewModel extends ViewModel {
    private final DatabaseReference databaseReference;
    private final MutableLiveData<List<String>> brands = new MutableLiveData<>();
    private final MutableLiveData<List<String>> models = new MutableLiveData<>();
    private final MutableLiveData<List<String>> variants = new MutableLiveData<>();
    private final MutableLiveData<List<String>> years = new MutableLiveData<>();
    private String selectedVehicleType = "motorcycles";

    public DetailInformationHomeViewModel() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("vehicles");
    }

    public void setVehicleType(String type) {
        selectedVehicleType = type;
        loadBrands();
    }

    public void loadBrands() {
        databaseReference.child(selectedVehicleType).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> brandList = new ArrayList<>();
                brandList.add("Pilih Merk/Brand");
                for (DataSnapshot brandSnapshot : dataSnapshot.getChildren()) {
                    brandList.add(brandSnapshot.getKey());
                }
                brands.setValue(brandList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    public void loadModels(String brand) {
        if (brand.equals("Pilih Merk/Brand")) {
            List<String> modelList = new ArrayList<>();
            modelList.add("Pilih Model");
            models.setValue(modelList);
            return;
        }

        databaseReference.child(selectedVehicleType).child(brand).child("models")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> modelList = new ArrayList<>();
                        modelList.add("Pilih Model");
                        for (DataSnapshot modelSnapshot : dataSnapshot.getChildren()) {
                            modelList.add(modelSnapshot.getKey());
                        }
                        models.setValue(modelList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }

    public void loadVariantsAndYears(String brand, String model) {
        if (model.equals("Pilih Model")) {
            resetVariantsAndYears();
            return;
        }

        loadVariants(brand, model);
        loadYears(brand, model);
    }

    private void loadVariants(String brand, String model) {
        databaseReference.child(selectedVehicleType).child(brand).child("models").child(model)
                .child("variants").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> variantList = new ArrayList<>();
                        variantList.add("Pilih Variant");
                        for (DataSnapshot variantSnapshot : dataSnapshot.getChildren()) {
                            variantList.add(variantSnapshot.getValue(String.class));
                        }
                        variants.setValue(variantList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }

    private void loadYears(String brand, String model) {
        databaseReference.child(selectedVehicleType).child(brand).child("models").child(model)
                .child("productionYears").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> yearList = new ArrayList<>();
                        yearList.add("Pilih Tahun Produksi");
                        for (DataSnapshot yearSnapshot : dataSnapshot.getChildren()) {
                            yearList.add(yearSnapshot.getValue(String.class));
                        }
                        years.setValue(yearList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }

    private void resetVariantsAndYears() {
        List<String> defaultList = new ArrayList<>();
        defaultList.add("Pilih Variant");
        variants.setValue(defaultList);

        defaultList = new ArrayList<>();
        defaultList.add("Pilih Tahun Produksi");
        years.setValue(defaultList);
    }

    // Getters for LiveData
    public LiveData<List<String>> getBrands() { return brands; }
    public LiveData<List<String>> getModels() { return models; }
    public LiveData<List<String>> getVariants() { return variants; }
    public LiveData<List<String>> getYears() { return years; }
}
