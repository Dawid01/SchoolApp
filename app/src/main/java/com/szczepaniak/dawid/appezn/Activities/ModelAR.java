package com.szczepaniak.dawid.appezn.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.RenderableDefinition;
import com.google.ar.sceneform.rendering.ResourceManager;
import com.google.ar.sceneform.ux.ArFragment;
import com.szczepaniak.dawid.appezn.R;

import java.io.File;
import java.io.InputStream;

public class ModelAR extends AppCompatActivity {

    private ArFragment arFragment;
    private String ASSET_3D = "http://192.168.0.110:8080/downloadFile/computerv2.glb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_ar);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            placeModel(hitResult.createAnchor());
        });
    }

    private void placeModel(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this,
                        RenderableSource
                        .builder().setSource(this, Uri.parse(ASSET_3D), RenderableSource.SourceType.GLB)
                        .setScale(0.001f)
                        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                        .build()
                )
                .setRegistryId(ASSET_3D)
                .build()
                .thenAccept(modelRenderable -> addNodeToScene(modelRenderable, anchor))
                .exceptionally(throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(throwable.getMessage()).show();
                    return null;
                });
    }

    private void addNodeToScene(ModelRenderable modelRenderable, Anchor anchor) {

        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().onAddChild(anchorNode);
    }

}
