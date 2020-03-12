package com.szczepaniak.dawid.appezn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class PhotoView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera camera;
    private Context context;
    private List<Camera.Size> mSupportedPreviewSizes;
    Camera.Size mPreviewSize;
    int currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private byte[] byteArray;


    public PhotoView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;

    }

    public void startCamera(){
        Camera camera = Camera.open();
        this.camera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSupportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();
    }

    public void refreshCamera() throws IOException {
        camera.reconnect();
    }

    public void stopCamera(){
        camera.stopPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        if(camera != null){
            camera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] bytes, Camera camera) {
                    if (camera.getParameters() == null)
                    {
                        return;
                    }
                    byteArray = bytes;
                }
            });
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
                               int width, int height) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            camera.setParameters(parameters);
            camera.setParameters(parameters);
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(mHolder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
        }

        if (mPreviewSize!=null) {
            float ratio;
            if(mPreviewSize.height >= mPreviewSize.width)
                ratio = (float) mPreviewSize.height / (float) mPreviewSize.width;
            else
                ratio = (float) mPreviewSize.width / (float) mPreviewSize.height;

            setMeasuredDimension(width, (int) (width * ratio));

        }else {
            setMeasuredDimension(width, height);
        }
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


    public void switchCamera(){

        camera.stopPreview();
        camera.release();

        if(currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        else {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        try {
            try {
                camera = Camera.open(currentCameraId);
                Camera.Parameters parameters = camera.getParameters();
                parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }
                camera.setParameters(parameters);
            }catch (RuntimeException E){}
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(mHolder);
            camera.startPreview();
        }catch (IOException e){

        }
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = null;

        try {
            if (camera.getParameters()== null)
                return null;

            if (mPreviewSize == null)
                return null;

            int format = camera.getParameters().getPreviewFormat();
            YuvImage yuvImage = new YuvImage(byteArray, format, mPreviewSize.width, mPreviewSize.height, null);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            Rect rect = new Rect(0, 0, mPreviewSize.width, mPreviewSize.height);

            yuvImage.compressToJpeg(rect, 75, byteArrayOutputStream);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            options.inInputShareable = true;
            bitmap = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size(), options);

            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return bitmap;
    }
}



