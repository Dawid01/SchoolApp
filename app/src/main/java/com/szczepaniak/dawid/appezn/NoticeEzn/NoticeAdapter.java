package com.szczepaniak.dawid.appezn.NoticeEzn;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szczepaniak.dawid.appezn.Adapters.ChildAdapter;
import com.szczepaniak.dawid.appezn.Adapters.RecyclerViewAdapter;
import com.szczepaniak.dawid.appezn.Assymetric.AsymmetricRecyclerView;
import com.szczepaniak.dawid.appezn.Assymetric.AsymmetricRecyclerViewAdapter;
import com.szczepaniak.dawid.appezn.Assymetric.Utils;
import com.szczepaniak.dawid.appezn.ItemImage;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.SpacesItemDecoration;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.http.PUT;
import retrofit2.http.Url;

public class NoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<NoticePost> posts;
    private Context context;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;


    public NoticeAdapter(List<NoticePost> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_post, parent, false);
            return new NoticeAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new NoticeAdapter.LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof NoticeAdapter.ViewHolder) {
            loadNoticePost((NoticeAdapter.ViewHolder)holder, position);
        }else if (holder instanceof NoticeAdapter.LoadingViewHolder) {
            showLoadingView((NoticeAdapter.LoadingViewHolder) holder, position);
        }

    }


    void loadNoticePost(NoticeAdapter.ViewHolder holder, int position){

        NoticePost post = posts.get(position);
        holder.name.setText(post.getTitle().getRendered());
        DateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.GERMAN);
        try {
            Date date = inputFormat.parse(post.getDate());
            String out = outputFormat.format(date);
            holder.time.setText(out);
        }catch (ParseException e){
            holder.time.setText(post.getDate());
        }

        AsymmetricRecyclerView album = holder.album;
        album.setNestedScrollingEnabled(true);
        album.setRequestedColumnCount(3);
        album.setRequestedHorizontalSpacing(Utils.dpToPx(context, 1));
        album.addItemDecoration(new SpacesItemDecoration(context.getResources().getDimensionPixelSize(R.dimen.asymetric_grid_offset)));
        ImageGetter imageGetter = new ImageGetter(holder.content, context);

        ArrayList<String> images = pullLinks(post.getContent().getRendered());

        holder.content.setText(Html.fromHtml(post.getContent().getRendered(), imageGetter, null));
        holder.content.setMovementMethod(LinkMovementMethod.getInstance());

        if(images.size() != 0){

            if(images.size() == 1){

                Glide.with(context).load(images.get(0)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC)).into(holder.photo);

            }else {

                List<ItemImage> postImages = new ArrayList<>();
                boolean isCol2Avail = false;

                for (int i = 0; i < images.size(); i++) {
                    String url = images.get(i);
                    ItemImage postImage = new ItemImage(i, url, url);
                    int colSpan = 1;
                    if (i == 0) {
                        colSpan = 2;
                    }

                    int rowSpan = colSpan;
                    if (colSpan == 2 && !isCol2Avail)
                        isCol2Avail = true;
                    else if (colSpan == 2 && isCol2Avail)
                        colSpan = 1;


                    if (images.size() == 2) {
                        colSpan = 3;
                        rowSpan = 2;

                    } else if (images.size() == 4) {

                        if (i == 0) {
                            colSpan = 3;
                            rowSpan = 2;
                        }
                    } else if (images.size() == 5) {

                        if (i == 3) {
                            colSpan = 2;
                            rowSpan = 1;
                        }
                    }

                    postImage.setColumnSpan(colSpan);
                    postImage.setRowSpan(rowSpan);
                    postImage.setPosition(i);
                    postImages.add(postImage);

                }

                List<ItemImage> photos = new ArrayList<>();
                String[] photoTable = new String[postImages.size()];

                for (int i = 0; i < postImages.size(); i++) {

                    if (i <= 5) {
                        photos.add(postImages.get(i));
                    }
                    photoTable[i] = postImages.get(i).getImagePath();

                }

                ChildAdapter adapter = new ChildAdapter(photos, 6, postImages.size(), photoTable);
                album.setHasFixedSize(true);
                album.setNestedScrollingEnabled(false);
                album.setItemViewCacheSize(20);
                album.setDrawingCacheEnabled(true);
                album.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                album.setAdapter(new AsymmetricRecyclerViewAdapter(context, album, adapter));
            }

        }


    }

    private void showLoadingView(NoticeAdapter.LoadingViewHolder holder, int position) {

    }

    @Override public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return posts.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
         public TextView time;
         public TextView name;
         public TextView content;
         public LinearLayout layout;
         public AsymmetricRecyclerView album;
         public ImageView photo;


        public ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.content);
            layout = itemView.findViewById(R.id.layout);
            album = itemView.findViewById(R.id.photo_album);
            photo = itemView.findViewById(R.id.photo);

        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public static ArrayList<String> extractLinks(String text) {
        ArrayList<String> links = new ArrayList<String>();
        Matcher m = Patterns.WEB_URL.matcher(text);
        while (m.find()) {
            String url = m.group();
            String s = url.substring(url.length() - 3);
//            if(s.equals("jpg") || s.equals("png")) {
//                links.add(url);
//            }
            links.add(url);

        }

        return links;
    }


    private ArrayList pullLinks(String html) {
        ArrayList links = new ArrayList();
        Elements srcs = Jsoup.parse(html).select("[src]");
        for (int i = 0; i < srcs.size(); i++) {
            links.add(srcs.get(i).attr("src"));
        }
        return links;
    }

}