package com.szczepaniak.dawid.appezn.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.szczepaniak.dawid.appezn.Adapters.CommentsAdapter;
import com.szczepaniak.dawid.appezn.ApiService;
import com.szczepaniak.dawid.appezn.Models.Comment;
import com.szczepaniak.dawid.appezn.Models.CommentList;
import com.szczepaniak.dawid.appezn.Models.Post;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;
import com.szczepaniak.dawid.appezn.Singleton;
import com.szczepaniak.dawid.appezn.TextViewEmojiAndMore;
import com.vanniktech.emoji.EmojiPopup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView commentsView;
    private List<Comment> comments;
    private ImageView send;
    private EditText commentText;
    private ApiService api;
    private CommentsAdapter commentsAdapter;
    private ImageView emojiBtm;
    private SlidingUpPanelLayout sliding;
    private TextViewEmojiAndMore postContent;
    private ImageView avatar;
    private TextView userName;
    private TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.LightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        api = RetroClient.getApiService();
        commentsView = findViewById(R.id.comments_view);
        comments = Singleton.getInstance().getComments();
        //commentsView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentsView.setLayoutManager(layoutManager);
        commentsAdapter = new CommentsAdapter(comments, this);
        commentsView.setAdapter(commentsAdapter);
        send = findViewById(R.id.send);
        commentText = findViewById(R.id.post_edit_text);
        emojiBtm = findViewById(R.id.emoji);
        loadEmoji();
        sliding = findViewById(R.id.sliding_layout);
        sliding.setScrollableView(commentsView);
        postContent = findViewById(R.id.content);
        avatar = findViewById(R.id.avatar);
        userName = findViewById(R.id.name);
        data = findViewById(R.id.time);

        Post post = Singleton.getInstance().getPost();
        postContent.setText(post.getContent());
        userName.setText(post.getUser().getName() + post.getUser().getSurname());
        data.setText(post.getDateTime());
        Picasso.get().load(post.getUser().getPhoto()).into(avatar);


        commentsView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int i, int i1) {
                return false;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!commentText.getText().toString().trim().equals("")) {

                    final Comment comment = new Comment();
                    comment.setContent(commentText.getText().toString());
                    comment.setPost(Singleton.getInstance().getPost());
                    comment.setId(Singleton.getInstance().getCurrentUserID());
                    retrofit2.Call<Comment> commentCall = api.newComment(comment);
                     commentCall.enqueue(new Callback<Comment>() {
                            @Override
                            public void onResponse(Call<Comment> call, Response<Comment> response) {

                                if (response.isSuccessful()) {

                                    Toast.makeText(CommentsActivity.this, "Comment Send", Toast.LENGTH_SHORT).show();
                                    comments.add(response.body());
                                    commentsAdapter.setComments(comments);
                                    commentsAdapter.notifyItemInserted(comments.size() - 1);
                                    //commentsAdapter.notifyDataSetChanged();
                                    commentText.setText("");
                                    commentText.setActivated(false);
                                }
                            }

                            @Override
                            public void onFailure(Call<Comment> call, Throwable t) {

                            }
                        });

                }
            }
        });
    }

    private void loadEmoji(){

        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(commentsView).build(commentText);

        emojiBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(emojiPopup.isShowing()){
                    emojiPopup.dismiss();
                }else {
                    emojiPopup.toggle();
                }
            }
        });


    }
}
