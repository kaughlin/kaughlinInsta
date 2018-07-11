package codepath.kaughlinpractice.kaughlin_insta.my_fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import codepath.kaughlinpractice.kaughlin_insta.R;
import codepath.kaughlinpractice.kaughlin_insta.model.Post;

import static android.app.Activity.RESULT_OK;


public class createPics extends Fragment {
    //private static final String imagePath = "/storage/emulated/0/DCIM/Camera/IMG_20180709_175930.jpg";

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
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            userPic.setImageBitmap(imageBitmap); // image that what just took post in on imageview i created on fragment
            userPic.setVisibility(View.VISIBLE);
            btnCamera.setVisibility(View.INVISIBLE);
            description.setVisibility(View.VISIBLE);
            post.setVisibility(View.VISIBLE);

/*

            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String descriptionInput = description.getText().toString();
                    final ParseUser user = ParseUser.getCurrentUser();
                    final File file = new File(imagePath);
                    final ParseFile parseFile = new ParseFile(file);

                    createPost(descriptionInput, parseFile, user);
                }
            });
*/
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                    //Toast.makeText(bottomNavActivity.this,"Post successful", Toast.LENGTH_LONG ).show();

                    Log.d("home", "create post success!");
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }

}
