package codepath.kaughlinpractice.kaughlin_insta.my_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;

import codepath.kaughlinpractice.kaughlin_insta.CommentAdapter;
import codepath.kaughlinpractice.kaughlin_insta.GlideApp;
import codepath.kaughlinpractice.kaughlin_insta.R;
import codepath.kaughlinpractice.kaughlin_insta.model.Post;


public class details extends Fragment {

    public ImageView ivPostImage;
    public ImageView ivProfilePic;
    public TextView tvUsername;
    public TextView tvUsername2;
    public TextView tvDescription;
    public TextView tvPostTime;
    public TextView etComment;
    public Button btnSendComment;
    public ImageView ivClickToComment;
    public RecyclerView comments;
    public CommentAdapter commentAdapter;
    public ArrayList<Object> commentList;
    //public RecyclerView rv

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_details, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivPostImage = (ImageView) view.findViewById(R.id.ivPostImage);
        ivProfilePic = (ImageView) view.findViewById(R.id.ivProfilePic);
        tvUsername = (TextView) view.findViewById(R.id.tvUserName);
        tvUsername2 = (TextView) view.findViewById(R.id.tvUserName2);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvPostTime = (TextView) view.findViewById(R.id.tvTime);
        btnSendComment = (Button) view.findViewById(R.id.btnSendComment);
        etComment = (EditText) view.findViewById(R.id.etComment);
        ivClickToComment = (ImageView) view.findViewById(R.id.ivComment);

        comments = (RecyclerView)  view.findViewById(R.id.rvComments);

        commentList = new ArrayList();
        commentAdapter = new CommentAdapter(commentList);

        comments.setLayoutManager(new LinearLayoutManager(getContext()));
        comments.setAdapter(commentAdapter);




        Bundle args = getArguments();
        String id  = args.getString("PostId");

        Post.Query query = new Post.Query();
        query.getQuery(Post.class).getInBackground(id, new GetCallback<Post>() {
            @Override
            public void done(final Post post, ParseException e) {

                try {
                    tvUsername.setText(post.getUser().fetchIfNeeded().getUsername());
                    tvUsername2.setText(post.getUser().fetchIfNeeded().getUsername());
                    tvDescription.setText(post.getDescription());
                    tvPostTime.setText(post.getRelativeTimeAgo());

                    etComment.setVisibility(View.GONE);
                    btnSendComment.setVisibility(View.GONE);


                    GlideApp.with(getActivity())
                            .load(post.getImage().getUrl())
                            .into(ivPostImage);

                    if(post.getUser().getParseFile("profile") != null) {
                        GlideApp.with(getActivity())
                                .load(post.getUser().getParseFile("profile").getUrl())
                                .circleCrop()
                                .into(ivProfilePic);
                    }



                    btnSendComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String commentInfo = etComment.getText().toString();
                            commentInfo = ParseUser.getCurrentUser().getObjectId() + " " + commentInfo;
                            post.add("comments", commentInfo);
                            post.saveInBackground();
                            //etComment.getText().clear();
                            etComment.setText("");
                            etComment.setVisibility(View.GONE);
                            btnSendComment.setVisibility(View.GONE);
                            comments.setVisibility(View.VISIBLE);
                        }
                    });

                    ivClickToComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //rv.setVisibility(View.GONE);
                            //btnSendComment.setVisibility(View.GONE);

                            comments.setVisibility(View.INVISIBLE);
                            etComment.setVisibility(View.VISIBLE);
                            btnSendComment.setVisibility(View.VISIBLE);
                        }
                    });

                    commentList.clear();
                    commentList.addAll(post.getList("comments"));
                    commentAdapter.notifyDataSetChanged();

                }catch (ParseException e1){
                    e1.printStackTrace();

                }

            }
        });
    }


}
