package com.olfu.virtualhomeinteriordesigning.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.olfu.virtualhomeinteriordesigning.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CameraActivity extends Activity implements View.OnClickListener {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    Camera camera;
    CameraPreview cameraPreview;

    FrameLayout cameraFrame;
    Button btnCapture, btnRetake, btnSave;

    File picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initViews();
        initCamera();
    }

    private void initViews() {

        btnCapture = (Button) findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(this);

        btnRetake = (Button) findViewById(R.id.btnRetake);
        btnRetake.setOnClickListener(this);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);


    }

    private void initCamera() {

        camera = getCameraInstance();
        cameraPreview = new CameraPreview(this, camera);

        cameraFrame = (FrameLayout) findViewById(R.id.cameraFrame);
        cameraFrame.addView(cameraPreview);
    }

    private void showDialog() {

        View view = getLayoutInflater().inflate(R.layout.dialog_camera_preview, null);
        ImageView img = (ImageView) view.findViewById(R.id.preview);
        img.setImageURI(Uri.parse(image_path));

        AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
        builder.setView(view);

        builder.show();


    }


    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance

            Camera.Parameters params = c.getParameters();
            List sizes = params.getSupportedPictureSizes();
            Camera.Size result = null;
            for (int i = 0; i < sizes.size(); i++) {
                result = (Camera.Size) sizes.get(i);
//                Log.i("PictureSize", "Supported Size. Width: " + result.width + "height : " + result.height);
            }
            result = (Camera.Size) sizes.get(2);
            params.setPictureSize(result.width, result.height);
            c.setParameters(params);


        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
//                Log.d("TAG", "Error creating media file, check storage permissions: " +
                //                        e.getMessage());
                return;
            }

            try {

                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                fromCamera();
            } catch (FileNotFoundException e) {
                Log.d("TAG", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("TAG", "Error accessing file: " + e.getMessage());
            }
        }
    };


    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static final int REQUEST_CODE_FROM_CAMERA = 112;
    private Uri fileUri;
    String image_path = "";

    private void fromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        image_path = fileUri.getPath();

        Log.d("CLICKED file uri", image_path);
//        showDialog();
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//
//        // start the image capture Intent
//        startActivityForResult(intent, REQUEST_CODE_FROM_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FROM_CAMERA
                && resultCode == Activity.RESULT_OK) {
            try {
                image_path = fileUri.getPath();
                Log.d("camera", image_path);
                showDialog();

            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCamera();
    }

    private void stopCamera() {
        camera.stopPreview();
        camera.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        camera = getCameraInstance();
    }

    @Override
    public void onClick(View v) {
        if (v == btnCapture) {
            camera.takePicture(null, null, mPicture);
            btnCapture.setVisibility(View.GONE);
            btnRetake.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
        }
        if (v == btnRetake) {
            camera.startPreview();
            btnCapture.setVisibility(View.VISIBLE);
            btnRetake.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
        }
        if (v == btnSave) {
            Log.d("IMAGE", image_path);

            Bundle extras = new Bundle();
            extras.putString("root", "camera");
            extras.putString("background", image_path);
            startActivity(new Intent(this, Designer2.class).putExtras(extras));
            finish();
        }
    }
}
