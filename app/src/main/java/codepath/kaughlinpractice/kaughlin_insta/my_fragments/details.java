package codepath.kaughlinpractice.kaughlin_insta.my_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.GetCallback;
import com.parse.ParseException;

import codepath.kaughlinpractice.kaughlin_insta.model.Post;


public class details extends Fragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String id  = args.getString("PostId");

        Post.Query query = new Post.Query();
        query.getQuery(Post.class).getInBackground(id, new GetCallback<Post>() {
            @Override
            public void done(Post object, ParseException e) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
        //initate views in post details frag


    }
}
