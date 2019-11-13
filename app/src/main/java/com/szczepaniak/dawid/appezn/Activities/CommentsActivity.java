package com.szczepaniak.dawid.appezn.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.szczepaniak.dawid.appezn.Adapters.CommentsAdapter;
import com.szczepaniak.dawid.appezn.ApiService;
import com.szczepaniak.dawid.appezn.Models.Comment;
import com.szczepaniak.dawid.appezn.Models.CommentList;
import com.szczepaniak.dawid.appezn.R;
import com.szczepaniak.dawid.appezn.RetroClient;
import com.szczepaniak.dawid.appezn.Singleton;
import com.vanniktech.emoji.EmojiPopup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView commentsView;
    private List<Comment> comments;
    private ImageView send;
    private EditText commentText;
    private ApiService api;
    private CommentsAdapter commentsAdapter;
    private ImageView emojiBtm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        api = RetroClient.getApiService();
        commentsView = findViewById(R.id.comments_view);
        comments = Singleton.getInstance().getComments();
        commentsView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentsView.setLayoutManager(layoutManager);
        commentsAdapter = new CommentsAdapter(comments, this);
        commentsView.setAdapter(commentsAdapter);
        send = findViewById(R.id.send);
        commentText = findViewById(R.id.post_edit_text);
        emojiBtm = findViewById(R.id.emoji);
        loadEmoji();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!commentText.getText().equals("")){

                    final Comment comment = new Comment();
                    comment.setContent(commentText.getText().toString());
                    comment.setPost(Singleton.getInstance().getPost());
                    comment.setId(Singleton.getInstance().getCurrentUserID());
                    retrofit2.Call<Comment> commentCall = api.newComment(comment);

                    commentCall.enqueue(new Callback<Comment>() {
                        @Override
                        public void onResponse(Call<Comment> call, Response<Comment> response) {

                            if(response.isSuccessful()){

                                Toast.makeText(CommentsActivity.this, "Comment Send", Toast.LENGTH_SHORT).show();
                                comments.add(response.body());
                                commentsAdapter.setComments(comments);
                                commentsAdapter.notifyItemInserted(comments.size() - 1);
                                commentsAdapter.notifyDataSetChanged();
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
