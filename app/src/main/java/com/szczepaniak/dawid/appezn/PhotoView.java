package com.szczepaniak.dawid.appezn;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.security.Policy;
import java.util.List;

public class PhotoView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Context context;

    public PhotoView(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);

    }

    public PhotoView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        Camera mCamera = Camera.open();
        this.mCamera = mCamera;
        mCamera.setDisplayOrientation(90);
        mHolder = getHolder();
        mHolder.addCallback(this);
       // mHolder.setFixedSize(this.getWidth(), this.getHeight());
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);



    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPictureSize (this.getWidth(), this.getHeight());

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                parameters.set("orientation", "portrait");
                parameters.setRotation(90);
                mCamera.setDisplayOrientation(90);

            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                parameters.set("orientation", "landscape");
                parameters.setRotation(0);
                mCamera.setDisplayOrientation(0);

            }

            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {

        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.stopPreview();
        mCamera.release();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
                               int width, int height) {
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPictureSize (this.getWidth(), this.getHeight());

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                parameters.set("orientation", "portrait");
                parameters.setRotation(90);
                mCamera.setDisplayOrientation(90);

            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                parameters.set("orientation", "landscape");
                parameters.setRotation(0);
                mCamera.setDisplayOrientation(0);

            }

            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

    }
}



