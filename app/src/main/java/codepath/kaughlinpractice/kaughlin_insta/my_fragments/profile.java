package codepath.kaughlinpractice.kaughlin_insta.my_fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import codepath.kaughlinpractice.kaughlin_insta.GlideApp;
import codepath.kaughlinpractice.kaughlin_insta.InstaAdapter;
import codepath.kaughlinpractice.kaughlin_insta.LoginActivity;
import codepath.kaughlinpractice.kaughlin_insta.R;
import codepath.kaughlinpractice.kaughlin_insta.model.Post;

import static android.app.Activity.RESULT_OK;


public class profile extends Fragment {
    Button btnLogout;
    Button btnEditBio;
    Button btnSendBio;
    EditText etBio;
    TextView tvBio;
    Button btnTakeProfilePic;
    ImageView ProfilePic;
    ImageView actualProfilePic;
    TextView tvNumOfPosts;
    TextView tvuserName;
    Button uploadProfilePic;
    Button btnEditProfile;

    InstaAdapter instaAdapter;
    ArrayList<Post> mPosts;
    RecyclerView rvPosts;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    final ParseUser user = ParseUser.getCurrentUser();

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout = view.findViewById(R.id.btnLogout);
        actualProfilePic = view.findViewById(R.id.ivActualProfilePic);
        tvNumOfPosts = view.findViewById(R.id.tvNumOfPosts);
        tvuserName = view.findViewById(R.id.tvUserName);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnTakeProfilePic = view.findViewById(R.id.btnTakeProfilePic);
        ProfilePic = view.findViewById(R.id.ivProfilePic);
        uploadProfilePic = view.findViewById(R.id.btnUploadProfilePic);
        btnTakeProfilePic.setVisibility(View.INVISIBLE);
        tvBio = view.findViewById(R.id.tvBio);

        btnSendBio = view.findViewById(R.id.btnSendBio);
        btnEditBio = view.findViewById(R.id.btnBio);
        etBio = view.findViewById(R.id.etBio);
        etBio.setVisibility(View.INVISIBLE);
        btnSendBio.setVisibility(View.INVISIBLE);
        btnEditBio.setVisibility(View.INVISIBLE);
        if(user.getString("bio") != null) {
            tvBio.setText(user.getString("bio"));

        }

        btnEditBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEditBio.setVisibility(View.INVISIBLE);

                etBio.setVisibility(View.VISIBLE);
                btnSendBio.setVisibility(View.VISIBLE);
                if(user.getString("bio") != null) {
                    //tvBio.setText(user.getString("bio"));
                    tvBio.setVisibility(View.INVISIBLE);
                }

            }
        });

        btnSendBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etBio.setVisibility(View.INVISIBLE);
                btnTakeProfilePic.setVisibility(View.INVISIBLE);
                btnEditBio.setVisibility(View.INVISIBLE);
                btnSendBio.setVisibility(View.INVISIBLE);
                user.put("bio", etBio.getText().toString());//put it in the data base
                tvBio.setText(etBio.getText().toString());
                user.saveInBackground();
                tvBio.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Bio Updated", Toast.LENGTH_LONG).show();
            }
        });


        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnTakeProfilePic.setVisibility(View.VISIBLE);
                btnEditBio.setVisibility(View.VISIBLE);
            }
        });

        if(user.getParseFile("profile") != null) {
            GlideApp.with(getActivity())
                    .load(user.getParseFile("profile").getUrl())
                    .circleCrop()
                    .into(actualProfilePic);
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });





        ProfilePic.setVisibility(View.INVISIBLE);

        uploadProfilePic.setVisibility(View.INVISIBLE);

        btnTakeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(); // Intent
                //final ParseFile image = (ParseFile) userPic.getParseFile("");
            }
        });


        rvPosts = (RecyclerView) view.findViewById(R.id.rvProfilePosts);
        //init the arraylist (data source)
        mPosts = new ArrayList<>();
        // construct the adapter from this datasource
        instaAdapter = new InstaAdapter(mPosts);

        //recyclerView setup (layout manager, user adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        // set the adapter
        rvPosts.setAdapter(instaAdapter);
        loadTopPosts();



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (BitmapFactory.decodeFile(photoFile.getAbsolutePath()));
            ProfilePic.setImageBitmap(imageBitmap); // image that what just took post in on imageview i created on fragment

            ProfilePic.setVisibility(View.INVISIBLE);
            btnTakeProfilePic.setVisibility(View.INVISIBLE);
            uploadProfilePic.setVisibility(View.VISIBLE);



            uploadProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ParseUser user = ParseUser.getCurrentUser();
                    final File file = getPhotoFileUri(photoFileName);
                    final ParseFile parseFile = new ParseFile(file);


                    // TODO -- figure out how this works. the magic of getting the image
                    parseFile.saveInBackground();
                    user.put("profile", parseFile);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Toast.makeText(getActivity(),"Profile pic upload successful", Toast.LENGTH_LONG ).show();

                                Log.d("profile", "profile pic upload successful success!");
                            }
                            else{
                                Toast.makeText(getActivity(),"Profile pic upload unsuccessful", Toast.LENGTH_LONG ).show();
                                e.printStackTrace();
                            }
                        }
                    });


                    //TODO -- at intent to go back to home screen

                    btnTakeProfilePic.setVisibility(View.VISIBLE);
                    uploadProfilePic.setVisibility(View.INVISIBLE);
                }
            });

        }
    }


    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getActivity(), "codepath.kaughlinpractice.kaughlin_insta", photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (takePictureIntent.resolveActivity(profile.this.getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



    private void loadTopPosts() {

        final Post.Query postsQuery = new Post.Query();
        //final ParseUser user = ParseUser.getCurrentUser();
        postsQuery.getTop().withUser();


        postsQuery.findInBackground(new FindCallback<Post>()

        {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        if(objects.get(i).getUser().getUsername().equals(user.getUsername()) ) {
                            Log.d("HomeActivity", "post[" + i + "]= " + objects.get(i).getDescription()
                                    + "\nusername =" + objects.get(i).getUser().getUsername());
                            mPosts.add(0, objects.get(i));
                            instaAdapter.notifyItemInserted(mPosts.size() - 1);
                        }
                    }

                    tvuserName.setText(user.getUsername());
                    tvNumOfPosts.setText("Posts " + mPosts.size());

                } else {
                    e.printStackTrace();
                }
            }
        });
    }
    public void fetchTimelineAsync(int page) {
        instaAdapter.clear();
        loadTopPosts();
        //swipeContainer.setRefreshing(false);
    }



}