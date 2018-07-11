package codepath.kaughlinpractice.kaughlin_insta.my_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import codepath.kaughlinpractice.kaughlin_insta.InstaAdapter;
import codepath.kaughlinpractice.kaughlin_insta.R;
import codepath.kaughlinpractice.kaughlin_insta.model.Post;


public class home extends Fragment {

    private SwipeRefreshLayout swipeContainer;


    InstaAdapter instaAdapter;
    ArrayList<Post> mPosts;
    RecyclerView rvPosts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = (RecyclerView) view.findViewById(R.id.rvIGpost);
        //init the arraylist (data source)
        mPosts = new ArrayList<>();
        // construct the adapter from this datasource
        instaAdapter = new InstaAdapter(mPosts);

        //recyclerView setup (layout manager, user adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        // set the adapter
        rvPosts.setAdapter(instaAdapter);
        loadTopPosts();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    private void loadTopPosts() {

        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();


        postsQuery.findInBackground(new FindCallback<Post>()

        {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("HomeActivity", "post[" + i + "]= " + objects.get(i).getDescription()
                                + "\nusername =" + objects.get(i).getUser().getUsername());
                        mPosts.add(0 , objects.get(i));
                        instaAdapter.notifyItemInserted(mPosts.size()-1);
                    }

                } else {
                    e.printStackTrace();
                }
            }
        });
    }
    public void fetchTimelineAsync(int page) {
        instaAdapter.clear();
        loadTopPosts();
        swipeContainer.setRefreshing(false);

    }


}
