package com.szczepaniak.dawid.appezn;

import android.support.constraint.ConstraintLayout;
import android.support.design.internal.BottomNavigationMenu;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.szczepaniak.dawid.appezn.Activities.CommentsActivity;
import com.szczepaniak.dawid.appezn.Activities.MainActivity;
import com.szczepaniak.dawid.appezn.Adapters.CommentsAdapter;
import com.szczepaniak.dawid.appezn.Models.Comment;
import com.szczepaniak.dawid.appezn.Models.Post;
import com.vanniktech.emoji.EmojiPopup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentUpSlider {

    private MainActivity mainActivity;
    private SlidingUpPanelLayout sliding;
    private ConstraintLayout slidingLayout;
    private View menu;


    private RecyclerView commentsView;
    private List<Comment> comments;
    private ImageView send;
    private EditText commentText;
    private ApiService api;
    private CommentsAdapter commentsAdapter;
    private ImageView emojiBtm;
    private TextViewEmojiAndMore postContent;
    private ImageView avatar;
    private TextView userName;
    private TextView data;

    public CommentUpSlider(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        api = RetroClient.getApiService();
        commentsView = mainActivity.findViewById(R.id.comments_view);
        commentsView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mainActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentsView.setLayoutManager(layoutManager);

        send = mainActivity.findViewById(R.id.send);
        commentText = mainActivity.findViewById(R.id.post_edit_text);
        emojiBtm = mainActivity.findViewById(R.id.emoji);
        loadEmoji();
        sliding = mainActivity.findViewById(R.id.sliding_layout);
        sliding.setScrollableView(commentsView);
        slidingLayout = mainActivity.findViewById(R.id.sliding);
        postContent = mainActivity.findViewById(R.id.contentPost);
        avatar = mainActivity.findViewById(R.id.avatarPost);
        userName = mainActivity.findViewById(R.id.name);
        data = mainActivity.findViewById(R.id.time);
        menu = mainActivity.findViewById(R.id.bottomMenu);
        sliding.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

    }

    public void openPanel(){

        sliding.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        menu.setVisibility(View.GONE);
        commentsView.removeAllViews();

        comments = Singleton.getInstance().getComments();
        commentsAdapter = new CommentsAdapter(comments, mainActivity);
        commentsView.setAdapter(commentsAdapter);

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

                                Toast.makeText(mainActivity, "Comment Send", Toast.LENGTH_SHORT).show();
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
