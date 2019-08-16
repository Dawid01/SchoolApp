package com.szczepaniak.dawid.appezn;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.szczepaniak.dawid.appezn.Assymetric.AGVRecyclerViewAdapter;
import com.szczepaniak.dawid.appezn.Assymetric.AsymmetricItem;

import java.util.List;

class ChildAdapter extends AGVRecyclerViewAdapter<ViewHolder> {
    private final List<ItemImage> items;
    private int mDisplay = 0;
    private int mTotal = 0;

    public ChildAdapter(List<ItemImage> items,int mDisplay, int mTotal) {
        this.items = items;
        this.mDisplay = mDisplay;
        this.mTotal = mTotal;

    }


    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("RecyclerViewActivity", "onCreateView");
        return new ViewHolder(parent, viewType,items);
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

    public ViewHolder(ViewGroup parent, int viewType, List<ItemImage> items) {
        super(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.post_photo, parent, false));

        mImageView = itemView.findViewById(R.id.image);
        mTextCount = itemView.findViewById(R.id.countText);



    }


    public void bind(List<ItemImage> item, int position, int mDisplay, int mTotal) {
        Singleton singleton = Singleton.getInstance();
        ImageLoader imageLoader = singleton.getImageLoader();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MyApplication.getAppContext()));
        if(mImageView != null) {
            ImageLoader.getInstance().displayImage(String.valueOf(item.get(position).getImagePath()), mImageView);

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

    }
}
