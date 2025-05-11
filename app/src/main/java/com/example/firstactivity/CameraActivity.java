package com.example.firstactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

public class CameraActivity extends ComponentActivity {

    private PreviewView previewView;
    private Button adjustZoomButton;
    private Button changeInfoButton; // Button to change information
    private float zoomLevel = 1.0f;
    private float prescription;
    private boolean isFarsighted;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.camera_preview);
        adjustZoomButton = findViewById(R.id.adjustZoomButton);
        changeInfoButton = findViewById(R.id.changeInfoButton);  // Initialize the button

        SharedPreferences sharedPreferences = getSharedPreferences("VisionPreferences", Context.MODE_PRIVATE);
        isFarsighted = sharedPreferences.getBoolean("isFarsighted", false);  // Default to false (if not set)
        prescription = sharedPreferences.getFloat("prescriptionValue", 0.0f);

        // If the information is not set, show the dialog to ask the user
        if (prescription == 0.0f) {
            showInitialSetupDialog();
        } else {
            startCamera();
        }

        adjustZoomButton.setOnClickListener(v -> showZoomOptions());

        // Show dialog for changing information
        changeInfoButton.setOnClickListener(v -> showInitialSetupDialog());
    }

    private void showInitialSetupDialog() {
        // Ask the user if they have farsightedness or shortsightedness
        new AlertDialog.Builder(this)
                .setTitle("Select Vision Type")
                .setMessage("Do you have farsightedness or shortsightedness?")
                .setPositiveButton("Farsighted", (dialog, which) -> {
                    isFarsighted = true;
                    askForPrescription();
                })
                .setNegativeButton("Shortsighted", (dialog, which) -> {
                    isFarsighted = false;
                    askForPrescription();
                })
                .show();
    }

    private void askForPrescription() {
        // Ask the user to input their prescription
        EditText prescriptionInput = new EditText(this);
        prescriptionInput.setHint("Enter your prescription value (e.g., 1.5)");

        new AlertDialog.Builder(this)
                .setTitle("Enter Prescription")
                .setMessage("Please enter your prescription value.")
                .setView(prescriptionInput)
                .setPositiveButton("OK", (dialog, which) -> {
                    try {
                        prescription = Float.parseFloat(prescriptionInput.getText().toString());
                        saveVisionSettings();  // Save the settings to SharedPreferences
                        startCamera();  // Start the camera with the new settings
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid prescription value", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> finish())
                .show();
    }

    private void saveVisionSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("VisionPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFarsighted", isFarsighted);
        editor.putFloat("prescriptionValue", prescription);
        editor.apply();  // Save the data to SharedPreferences
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                cameraProvider.unbindAll();
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);

                // Apply zoom after short delay to ensure camera is ready
                previewView.postDelayed(this::applyInitialZoom, 200);

            } catch (Exception e) {
                Toast.makeText(this, "Error starting camera", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void applyInitialZoom() {
        if (camera == null) return;

        // Calculate zoom level based on prescription
        float zoomChange = Math.abs(prescription) / 10f;

        if (isFarsighted) {
            // Farsighted → zoom out
            zoomLevel = Math.max(0.1f, 1.0f - zoomChange);
        } else {
            // Shortsighted → zoom in
            zoomLevel = Math.min(5.0f, 1.0f + zoomChange);
        }

        camera.getCameraControl().setZoomRatio(zoomLevel)
                .addListener(() -> {
                    Toast.makeText(this, "Zoom set to: " + zoomLevel, Toast.LENGTH_SHORT).show();
                }, ContextCompat.getMainExecutor(this));
    }

    private void showZoomOptions() {
        final String[] options = {"Near", "Far", "None"};

        new AlertDialog.Builder(this)
                .setTitle("Adjust Zoom")
                .setItems(options, (dialog, which) -> {
                    float zoomAdjustment = 0.2f; // The step by which the zoom level will change

                    switch (which) {
                        case 0: // Near
                            // Increase zoom (make things larger, zoom in)
                            zoomLevel = Math.min(5.0f, zoomLevel + zoomAdjustment);
                            break;
                        case 1: // Far
                            // Decrease zoom (make things smaller, zoom out)
                            zoomLevel = Math.max(0.1f, zoomLevel - zoomAdjustment);
                            break;
                        case 2: // None
                            // No change to the zoom
                            Toast.makeText(this, "No change applied", Toast.LENGTH_SHORT).show();
                            return;
                    }

                    // Apply the zoom adjustment
                    if (camera != null) {
                        camera.getCameraControl().setZoomRatio(zoomLevel)
                                .addListener(() -> Toast.makeText(this, "Zoom updated to: " + zoomLevel, Toast.LENGTH_SHORT).show(),
                                        ContextCompat.getMainExecutor(this));
                    }
                })
                .show();
    }}


