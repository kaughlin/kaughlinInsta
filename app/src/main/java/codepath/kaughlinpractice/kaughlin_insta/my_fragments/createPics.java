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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import codepath.kaughlinpractice.kaughlin_insta.R;
import codepath.kaughlinpractice.kaughlin_insta.model.Post;

import static android.app.Activity.RESULT_OK;


public class createPics extends Fragment {


    static final int REQUEST_IMAGE_CAPTURE = 1;

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;


    Button btnCamera;
    ImageView userPic;
    EditText description;
    Button post;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_pics, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCamera = view.findViewById(R.id.btnCamButton);


        userPic = view.findViewById(R.id.ivUserPic);
        userPic.setVisibility(View.INVISIBLE);
        description = view.findViewById(R.id.etDescription);
        description.setVisibility(View.INVISIBLE);
        post = view.findViewById(R.id.btnPost);
        post.setVisibility(View.INVISIBLE);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(); // Intent
                //final ParseFile image = (ParseFile) userPic.getParseFile("");
            }
        });





    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (BitmapFactory.decodeFile(photoFile.getAbsolutePath()));
            userPic.setImageBitmap(imageBitmap); // image that what just took post in on imageview i created on fragment

            userPic.setVisibility(View.VISIBLE);
            btnCamera.setVisibility(View.INVISIBLE);
            description.setVisibility(View.VISIBLE);
            post.setVisibility(View.VISIBLE);



            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String descriptionInput = description.getText().toString();
                    final ParseUser user = ParseUser.getCurrentUser();
                    final File file = getPhotoFileUri(photoFileName);
                    final ParseFile parseFile = new ParseFile(file);

                    //TODO -- at intent to go back to home screen
                    createPost(descriptionInput, parseFile, user);
                    userPic.setVisibility(View.INVISIBLE);
                    btnCamera.setVisibility(View.VISIBLE);
                    description.setVisibility(View.INVISIBLE);
                    post.setVisibility(View.INVISIBLE);
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

        if (takePictureIntent.resolveActivity(createPics.this.getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void createPost(String description, ParseFile imageFile, ParseUser user){
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(getActivity(),"Post successful", Toast.LENGTH_LONG ).show();

                    Log.d("home", "create post success!");
                }
                else{
                    Toast.makeText(getActivity(),"Post unsuccessful", Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                }
            }
        });
    }

}
