package com.example.firstactivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private Camera mCamera;
    private SurfaceView mSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera); // Ensure this is the correct layout

        // Check for Camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            initializeCamera();
        }
    }

    private void initializeCamera() {
        mSurfaceView = findViewById(R.id.surfaceView); // Ensure this is the correct reference
        mCamera = getCameraInstance();

        if (mCamera == null) {
            Toast.makeText(this, "Failed to get camera instance", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set up SurfaceView for preview
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    // Set the camera preview display and start the preview
                    mCamera.setPreviewDisplay(holder);
                    setCameraResolution(); // Set camera resolution without zooming in
                    mCamera.startPreview();
                } catch (Exception e) {
                    Log.e("CameraError", "Error setting up camera preview: " + e.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                try {
                    mCamera.stopPreview();
                    mCamera.setPreviewDisplay(holder);
                    setCameraResolution(); // Set camera resolution again when surface changes
                    mCamera.startPreview();
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

    private static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open(); // Try to open the default camera
        } catch (Exception e) {
            Log.e("CameraError", "Camera is not available: " + e.getMessage());
        }
        return camera;
    }

    private void setCameraResolution() {
        Camera.Parameters parameters = mCamera.getParameters();

        // Get supported preview sizes and find the highest resolution
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size bestPreviewSize = supportedPreviewSizes.get(0);

        for (Camera.Size size : supportedPreviewSizes) {
            if (size.width * size.height > bestPreviewSize.width * bestPreviewSize.height) {
                bestPreviewSize = size;
            }
        }

        // Set the best preview size
        parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);

        // Get supported picture sizes and set the highest resolution for photos
        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
        Camera.Size bestPictureSize = supportedPictureSizes.get(0);

        for (Camera.Size size : supportedPictureSizes) {
            if (size.width * size.height > bestPictureSize.width * bestPictureSize.height) {
                bestPictureSize = size;
            }
        }

        // Set the picture size (for capturing photos)
        parameters.setPictureSize(bestPictureSize.width, bestPictureSize.height);

        // We are NOT setting zoom here to keep the normal/far view
        // Just use default settings for normal distance

        mCamera.setParameters(parameters);

        // Set the camera display orientation to 90 degrees (portrait mode)
        setCameraDisplayOrientation(90);

        Log.i("CameraInfo", "Preview size set to: " + bestPreviewSize.width + "x" + bestPreviewSize.height);
        Log.i("CameraInfo", "Picture size set to: " + bestPictureSize.width + "x" + bestPictureSize.height);
    }

    private void setCameraDisplayOrientation(int rotation) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);

        // Get the current display rotation
        int degrees = rotation; // We want to set 90 degrees for portrait mode

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
