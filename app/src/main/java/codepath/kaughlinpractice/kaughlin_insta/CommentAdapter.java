package codepath.kaughlinpractice.kaughlin_insta;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Object> mComments;
    private TextView tvUserName;
    Context context;

    public CommentAdapter(List<Object> objects){ mComments = objects;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_comment, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String comment = (String) mComments.get(position);
        final String [] commentArray = comment.split(" ", 2);
        ParseQuery<ParseUser> commentParse = ParseUser.getQuery().whereEqualTo("objectId", commentArray[0]);
        commentParse.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                for(int i = 0; i < objects.size(); i++){
                    ParseUser user = objects.get(i);


                    if(user.getParseFile("profile") != null) {
                        GlideApp.with(context).load(user.getParseFile("profile").getUrl()).circleCrop().into(holder.ivProfilePic);
                    }



                    holder.tvUserName.setText(user.getUsername());

                    holder.tvComment.setText(commentArray[1]);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //public ImageView ivProfileImage;
        public ImageView ivProfilePic;
        public TextView tvComment;
        public TextView tvUserName;

        public ViewHolder(View itemView) {
            super(itemView);
            //ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            ivProfilePic = (ImageView) itemView.findViewById(R.id.ivProfilePic);
            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            tvUserName= (TextView) itemView.findViewById(R.id.tvUserName);


        }


        //TODO on click listner stuff go to other user profile
        /*
        @Override
        public void onClick(View view) {


            // gets item position
            int position = getAdapterPosition();
            Log.d("InstaAdapter", "A post was clicked");
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Post post = mPosts.get(position);
                // create bundle
                Log.d("InstaAdapter", "Here's the ");
                ((bottomNavActivity) context).openDetails(post);


            }
        }
        */
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        mComments.clear();
        notifyDataSetChanged();
    }


}

