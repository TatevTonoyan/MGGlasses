package com.example.firstactivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;

import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private int eyePrescription = 0;
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Retrieve the eye prescription data passed from QuizActivity
        if (getIntent().hasExtra("eye_prescription")) {
            eyePrescription = getIntent().getIntExtra("eye_prescription", 0); // Default to 0 if no data
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            initializeCamera();
        }

        // After initializing the camera, ask for zoom preference
        askForZoomPreference();
    }

    // Method to ask the user for zoom preference (near or far)
    public void askForZoomPreference() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Zoom Preference")
                .setMessage("Do you want to see near or far?")
                .setPositiveButton("Near", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Adjust zoom for near
                        setEyePrescription(10);  // Higher zoom level for near view
                    }
                })
                .setNegativeButton("Far", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Adjust zoom for far
                        setEyePrescription(-5);  // Lower zoom level for far view
                    }
                })
                .create()
                .show();
    }

    public void setEyePrescription(int prescription) {
        this.eyePrescription = prescription;
        adjustCameraZoom();
    }

    // Adjust the camera zoom based on the eye prescription
    private void adjustCameraZoom() {
        if (mCamera == null) return;
        Camera.Parameters parameters = mCamera.getParameters();

        if (parameters.isZoomSupported()) {
            int maxZoom = parameters.getMaxZoom();
            int zoomLevel = (int) ((eyePrescription + 5) * (maxZoom / 10.0));
            zoomLevel = Math.max(0, Math.min(zoomLevel, maxZoom));
            parameters.setZoom(zoomLevel);
            mCamera.setParameters(parameters);
            Log.i("CameraZoom", "Zoom adjusted to level: " + zoomLevel);
        } else {
            Log.i("CameraZoom", "Zoom not supported on this device");
        }
    }

    private void initializeCamera() {
        mSurfaceView = findViewById(R.id.surfaceView);
        mCamera = getCameraInstance();

        if (mCamera == null) {
            Toast.makeText(this, "Failed to get camera instance", Toast.LENGTH_SHORT).show();
            return;
        }

        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mCamera.setPreviewDisplay(holder);
                    setCameraResolution();
                    mCamera.startPreview();
                    adjustCameraZoom();
                } catch (Exception e) {
                    Log.e("CameraError", "Error setting up camera preview: " + e.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                try {
                    mCamera.stopPreview();
                    mCamera.setPreviewDisplay(holder);
                    setCameraResolution();
                    mCamera.startPreview();
                    adjustCameraZoom();
                } catch (Exception e) {
                    Log.e("CameraError", "Error restarting camera preview: " + e.getMessage());
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                releaseCamera();
            }
        });
    }

    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open(cameraId);
        } catch (Exception e) {
            Log.e("CameraError", "Camera is not available: " + e.getMessage());
        }
        return camera;
    }

    private void setCameraResolution() {
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> supportedSizes = parameters.getSupportedPictureSizes();
        Camera.Size bestSize = supportedSizes.get(0);

        for (Camera.Size size : supportedSizes) {
            if (size.width * size.height > bestSize.width * bestSize.height) {
                bestSize = size;
            }
        }
        parameters.setPictureSize(bestSize.width, bestSize.height);
        parameters.setJpegQuality(100);

        if (parameters.isVideoStabilizationSupported()) {
            parameters.setVideoStabilization(true);
        }
        if (parameters.isAutoExposureLockSupported()) {
            parameters.setAutoExposureLock(false);
        }

        mCamera.setParameters(parameters);
        setCameraDisplayOrientation(90);
    }

    private void setCameraDisplayOrientation(int rotation) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int degrees = rotation;
        int result = (info.orientation - degrees + 360) % 360;
        mCamera.setDisplayOrientation(result);
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
