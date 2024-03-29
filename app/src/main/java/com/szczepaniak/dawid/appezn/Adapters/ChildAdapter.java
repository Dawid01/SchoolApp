package com.szczepaniak.dawid.appezn.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;
import com.szczepaniak.dawid.appezn.Activities.MyApplication;
import com.szczepaniak.dawid.appezn.Activities.PhotosViewerActivity;
import com.szczepaniak.dawid.appezn.Assymetric.AGVRecyclerViewAdapter;
import com.szczepaniak.dawid.appezn.Assymetric.AsymmetricItem;
import com.szczepaniak.dawid.appezn.ItemImage;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.Singleton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChildAdapter extends AGVRecyclerViewAdapter<ViewHolder> {
    private final List<ItemImage> items;
    private final String[] photosList;
    private int mDisplay = 0;
    private int mTotal = 0;

    public ChildAdapter(List<ItemImage> items,int mDisplay, int mTotal, String[] photosList) {
        this.items = items;
        this.mDisplay = mDisplay;
        this.mTotal = mTotal;
        this.photosList = photosList;

    }


    @NotNull
    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("RecyclerViewActivity", "onCreateView");
        ViewHolder viewHolder = new ViewHolder(parent, viewType,items, photosList);
        viewHolder.setIsRecyclable(true);
        return viewHolder;
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("RecyclerViewActivity", "onBindView position=" + position);
        holder.bind(items,position,mDisplay,mTotal);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    @Override public AsymmetricItem getItem(int position) {
        return (AsymmetricItem) items.get(position);
    }

    @Override public int getItemViewType(int position) {
        return position % 2 == 0 ? 1 : 0;
    }
}


class ViewHolder extends RecyclerView.ViewHolder {
    private final ImageView mImageView;
    private final TextView mTextCount;
    private final String[] photosList;

    public ViewHolder(ViewGroup parent, int viewType, List<ItemImage> items, String[] photosList) {
        super(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.post_photo, parent, false));
        this.photosList = photosList;

        mImageView = itemView.findViewById(R.id.image);
        mTextCount = itemView.findViewById(R.id.countText);



    }


    public void bind(final List<ItemImage> item, int position, int mDisplay, int mTotal) {
        final Singleton singleton = Singleton.getInstance();
        if(mImageView != null) {

            RequestOptions reqOptions = new RequestOptions()
                    .fitCenter()
                    .override(mImageView.getMeasuredWidth(), mImageView.getMeasuredHeight());
            Glide.with(MyApplication.getAppContext()).asBitmap().load(item.get(position).getImagePath()).apply(reqOptions).centerCrop().into(mImageView);

            if (mTotal > mDisplay) {
                if (position == mDisplay - 1) {
                    mImageView.setAlpha(120);
                    String text = "+" + (mTotal - mDisplay);
                    mTextCount.setText(text);
                } else {
                    mImageView.setAlpha(255);
                }
            } else {
                mImageView.setAlpha(255);
            }
        }

        assert mImageView != null;
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] photos = new String[item.size()];

                for(int i = 0; i < item.size(); i++){

                    photos[i] = item.get(i).getImagePath();
                }

                singleton.setPhotos(photosList);
                singleton.getMainActivity().startActivity(new Intent(singleton.getMainActivity(), PhotosViewerActivity.class));


            }
        });

    }
}
