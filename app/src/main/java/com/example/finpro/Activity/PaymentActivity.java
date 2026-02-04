package com.example.finpro.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.finpro.R;
import com.example.finpro.Activity.MainActivity; // Added import
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.Manifest;
import android.content.pm.PackageManager;



public class PaymentActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 2;

    private ImageView back;
    private TextView rekeningNumber;
    private Button uploadButton;
    private ImageView proofImageView;
    private Button confirmPaymentButton;

    private Uri imageUri;
    private String orderId;

    private DatabaseReference ordersReference;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        orderId = getIntent().getStringExtra("orderId");

        if (orderId == null || orderId.isEmpty()) {
            Toast.makeText(this, "Order ID is missing.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ordersReference = FirebaseDatabase.getInstance().getReference("bookings").child(orderId);
        storageReference = FirebaseStorage.getInstance().getReference("payment_proofs").child(orderId);

        back = findViewById(R.id.back);
        rekeningNumber = findViewById(R.id.rekeningNumber);
        uploadButton = findViewById(R.id.uploadButton);
        proofImageView = findViewById(R.id.proofImageView);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);

        back.setOnClickListener(v -> onBackPressed());

        uploadButton.setOnClickListener(v -> openFileChooser());

        confirmPaymentButton.setOnClickListener(v -> uploadFile());
    }

    private void openFileChooser() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST_CODE);
        } else {
            startImagePicker();
        }
    }

    private void startImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startImagePicker();
            } else {
                Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            proofImageView.setImageURI(imageUri);
        }
    }

    private void uploadFile() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    ordersReference.child("paymentProofUrl").setValue(downloadUrl);
                    ordersReference.child("status").setValue("waiting_confirmation");
                    Toast.makeText(PaymentActivity.this, "Payment proof uploaded. Please wait for confirmation.", Toast.LENGTH_LONG).show();
                    // Navigate to a "waiting for confirmation" screen or back to the main activity
                    Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }))
                .addOnFailureListener(e -> Toast.makeText(PaymentActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnProgressListener(snapshot -> {
                    // You can add a progress bar here if you want
                });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}
