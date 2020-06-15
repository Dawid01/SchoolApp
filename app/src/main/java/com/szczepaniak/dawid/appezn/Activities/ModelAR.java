package com.szczepaniak.dawid.appezn.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.ar.core.Anchor;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.szczepaniak.dawid.appezn.ApiService;
import com.szczepaniak.dawid.appezn.Models.ModelARList;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModelAR extends AppCompatActivity {

    private ArFragment arFragment;
    private String ASSET_3D = null;
    private int anchorCount = 0;
    public AnchorNode anchorNode;
    public TransformableNode transformableNode;
    private ImageView clear;
    private ProgressBar progressBar;
    private BottomSheetBehavior bottomSheetBehavior;
    private List<com.szczepaniak.dawid.appezn.Models.ModelAR> models = new ArrayList<>();
    private ApiService api;
    private LinearLayout modelsLayout;
    private View modelsSheet;
    private ConstraintLayout llBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = this.getSharedPreferences(
                "Settings", Context.MODE_PRIVATE);
        if(prefs.getBoolean("DarkTheme", false)){

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.LightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_ar);


        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        clear = findViewById(R.id.clear_button);
        api = RetroClient.getApiService();
        modelsLayout = findViewById(R.id.models);

        llBottomSheet = (ConstraintLayout) findViewById(R.id.models_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setPeekHeight(250);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

                if(progressBar.getVisibility() == View.VISIBLE){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeAnchorNode(anchorNode);
                anchorCount = 0;
            }
        });

        loadModelsList();

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);



        assert arFragment != null;
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            if(anchorCount == 0) {
                placeModel(hitResult.createAnchor());
                anchorCount = 1;
            }

        });

    }


    private void loadModelsList(){

        retrofit2.Call<ModelARList> modelARListCall = api.getModelsAR(20);

        modelARListCall.enqueue(new Callback<ModelARList>() {
            @SuppressLint("CutPasteId")
            @Override
            public void onResponse(Call<ModelARList> call, Response<ModelARList> response) {

                if(response.isSuccessful()){

                    assert response.body() != null;
                    models = response.body().getModelsAR();
                    com.szczepaniak.dawid.appezn.Models.ModelAR modelAR = models.get(0);
                    ASSET_3D = modelAR.getModelURL();
                    final TextView name = findViewById(R.id.name);
                    name.setText(modelAR.getName());

                    modelsSheet = findViewById(R.id.models_sheet);


                    for(com.szczepaniak.dawid.appezn.Models.ModelAR model : models){

                        LayoutInflater layoutInflater = LayoutInflater.from(ModelAR.this);
                        View view = layoutInflater.inflate(R.layout.ar_model_image, modelsLayout, false);
                        ImageView img = view.findViewById(R.id.image);
                        ImageView select = view.findViewById(R.id.select);
                        Glide.with(ModelAR.this).load(model.getImageURL()).centerCrop().into(img);
                        modelsLayout.addView(view);

                        if(models.indexOf(model) == 0){
                            select.setVisibility(View.VISIBLE);
                        }

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ASSET_3D = model.getModelURL();
                                name.setText(model.getName());

                                for(int i = 1; i < modelsLayout.getChildCount(); i++){

                                    modelsLayout.getChildAt(i).findViewById(R.id.select).setVisibility(View.GONE);
                                }
                                select.setVisibility(View.VISIBLE);
                            }
                        });

                    }


                }
            }

            @Override
            public void onFailure(Call<ModelARList> call, Throwable t) {

            }
        });

    }

    private void placeModel(Anchor anchor) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        if(ASSET_3D != null) {
            progressBar.setVisibility(View.VISIBLE);
            ModelRenderable
                    .builder()
                    .setSource(this,
                            RenderableSource
                                    .builder().setSource(this, Uri.parse(ASSET_3D), RenderableSource.SourceType.GLB)
                                    .setScale(0.1f)
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
    }

    private void addNodeToScene(ModelRenderable modelRenderable, Anchor anchor) {

        AnchorNode anchorNode = new AnchorNode(anchor);
        arFragment.getArSceneView().getScene().onAddChild(anchorNode);
       // arFragment.getArSceneView().getPlaneRenderer().getMaterial().thenAccept(material -> material.setFloat3(PlaneRenderer.MATERIAL_COLOR, new Color(20, 177, 174)) );
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.getScaleController().setMaxScale(7);
        node.getScaleController().setMinScale(0.5f);

        node.setParent(anchorNode);
        node.setRenderable(modelRenderable);
        node.select();

        this.anchorNode = anchorNode;
        this.transformableNode = node;
        progressBar.setVisibility(View.GONE);
        llBottomSheet.invalidate();


    }

    private void removeAnchorNode(AnchorNode nodeToremove) {
        if (nodeToremove != null) {
            arFragment.getArSceneView().getScene().removeChild(nodeToremove);
            nodeToremove.getAnchor().detach();
            nodeToremove.setParent(null);
        } else {
        }
    }

}
