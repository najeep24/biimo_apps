package com.example.finpro.Viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finpro.Domain.OnSiteServiceDomain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.finpro.Domain.ServiceTypeDomain;
import java.util.ArrayList;

public class ServiceTypeViewModel extends ViewModel {
    private static final String TAG = "ServiceTypeViewModel";
    private MutableLiveData<ArrayList<ServiceTypeDomain>> serviceList;
    private MutableLiveData<OnSiteServiceDomain> onsiteService;
    private DatabaseReference dbRef;
    private ValueEventListener valueEventListener;
    private ValueEventListener onsiteListener;
    private String selectedVehicleType;

    public ServiceTypeViewModel() {
        serviceList = new MutableLiveData<>();
        onsiteService = new MutableLiveData<>();
        dbRef = FirebaseDatabase.getInstance().getReference().child("serviceTypes");
    }

    public void setVehicleType(String type) {
        Log.d(TAG, "Setting vehicle type: " + type);
        this.selectedVehicleType = type;
        loadServices();
        loadOnsiteService();
    }

    public LiveData<ArrayList<ServiceTypeDomain>> getServices() {
        return serviceList;
    }

    public LiveData<OnSiteServiceDomain> getOnsiteService() {
        return onsiteService;
    }

    private void loadOnsiteService() {
        if (selectedVehicleType == null) return;

        DatabaseReference onsiteRef = dbRef.child(selectedVehicleType).child("onsite");

        if (onsiteListener != null) {
            onsiteRef.removeEventListener(onsiteListener);
        }

        onsiteListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    Long priceValue = dataSnapshot.child("price").getValue(Long.class);
                    String price = "Rp " + (priceValue != null ? String.format("%,d", priceValue) : "0");

                    Log.d(TAG, "Onsite service loaded - Name: " + name + ", Price: " + price);

                    if (name != null) {
                        onsiteService.setValue(new OnSiteServiceDomain(name, price));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing onsite service: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Database error for onsite: " + error.getMessage());
            }
        };

        onsiteRef.addValueEventListener(onsiteListener);
    }

    private void loadServices() {
        if (selectedVehicleType == null) {
            Log.e(TAG, "Vehicle type is null");
            return;
        }

        DatabaseReference serviceRef = dbRef.child(selectedVehicleType).child("services");
        Log.d(TAG, "Loading services from path: " + serviceRef.toString());

        if (valueEventListener != null) {
            serviceRef.removeEventListener(valueEventListener);
        }

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ServiceTypeDomain> services = new ArrayList<>();
                Log.d(TAG, "Data snapshot received: " + dataSnapshot.getChildrenCount() + " services");

                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                    try {
                        String name = serviceSnapshot.child("name").getValue(String.class);
                        // Handle price as Long and convert to formatted string
                        Long priceValue = serviceSnapshot.child("price").getValue(Long.class);
                        String price = "Rp " + (priceValue != null ? String.format("%,d", priceValue) : "0");

                        Log.d(TAG, "Service found - Name: " + name + ", Price: " + price);

                        if (name != null) {
                            services.add(new ServiceTypeDomain(name, price));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing service: " + e.getMessage());
                    }
                }

                Log.d(TAG, "Total services loaded: " + services.size());
                serviceList.setValue(services);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        };

        serviceRef.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (valueEventListener != null) {
            dbRef.removeEventListener(valueEventListener);
        }
        if (onsiteListener != null) {
            dbRef.removeEventListener(onsiteListener);
        }
    }
}