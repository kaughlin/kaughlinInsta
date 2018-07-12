package codepath.kaughlinpractice.kaughlin_insta;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import codepath.kaughlinpractice.kaughlin_insta.model.Post;

public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.ViewHolder> {

    private List<Post> mPosts;
    Context context;

    public InstaAdapter(List<Post> posts){ mPosts = posts;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        // get the data according to position
        final Post post = mPosts.get(position);
        holder.tvUsername.setText(post.getUser().getUsername());
        holder.tvDescription.setText(post.getDescription());
        //holder.ivPostImage.setText(post.getDescription());
        holder.tvTweetTime.setText(post.getRelativeTimeAgo());
        //holder.tvHandle.setText("@" +tweet.handle); // display tweet handle

        GlideApp.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPostImage);
                //.transform(new RoundedCornersTransformation(75,0 ))


    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //public ImageView ivProfileImage;
        public ImageView ivPostImage;
        public TextView tvUsername;
        public TextView tvDescription;
        public TextView tvTweetTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            ivPostImage = (ImageView) itemView.findViewById(R.id.ivPostImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvTweetTime = (TextView) itemView.findViewById(R.id.tvTime);

            itemView.setOnClickListener(this); //idk what this does


        }


        //TODO on click listner stuff
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
    }

        /* Within the RecyclerView.Adapter class */

        // Clean all elements of the recycler
        public void clear() {
            mPosts.clear();
            notifyDataSetChanged();
        }

        // Add a list of items -- change to type used
        public void addAll(List<Post> list) {
            mPosts.addAll(list);
            notifyDataSetChanged();
        }




}
