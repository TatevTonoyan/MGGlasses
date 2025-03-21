package com.example.firstactivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.view.View;
import androidx.camera.view.PreviewView;

public class CameraActivity extends AppCompatActivity {

    private PreviewView previewView;
    private ExecutorService cameraExecutor;
    private int eyePrescription = 0;
    private boolean isNearVision = true; // Default vision preference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.camera_preview);

        // Get eye prescription from intent
        if (getIntent().hasExtra("eye_prescription")) {
            eyePrescription = getIntent().getIntExtra("eye_prescription", 0);
        }

        // Initialize CameraX
        startCamera();

        // Initialize background thread for camera operations
        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                Camera camera = cameraProvider.bindToLifecycle(
                        (LifecycleOwner) this,
                        cameraSelector,
                        preview
                );

                adjustCameraZoom(camera); // Adjust zoom based on prescription

            } catch (Exception e) {
                Log.e("CameraX", "Failed to start camera: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this)); // ✅ FIX: Use ContextCompat for API < 28
    }

    private void adjustCameraZoom(Camera camera) {
        if (camera == null) return;

        int maxZoom = (int) camera.getCameraInfo().getZoomState().getValue().getMaxZoomRatio();
        int zoomLevel = calculateZoomLevel(maxZoom);

        camera.getCameraControl().setZoomRatio(zoomLevel);
        Log.i("CameraZoom", "Zoom adjusted to level: " + zoomLevel);
    }

    private int calculateZoomLevel(int maxZoom) {
        int baseZoom = eyePrescription * 2; // Adjust factor for eye correction
        int visionAdjustment = isNearVision ? 5 : -3; // Near vision requires more zoom
        int zoomLevel = baseZoom + visionAdjustment;
        return Math.max(1, Math.min(zoomLevel, maxZoom)); // Ensure zoom stays within bounds
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}
