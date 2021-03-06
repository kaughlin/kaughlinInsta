package codepath.kaughlinpractice.kaughlin_insta;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import codepath.kaughlinpractice.kaughlin_insta.model.Post;
import codepath.kaughlinpractice.kaughlin_insta.my_fragments.createPics;
import codepath.kaughlinpractice.kaughlin_insta.my_fragments.details;
import codepath.kaughlinpractice.kaughlin_insta.my_fragments.home;
import codepath.kaughlinpractice.kaughlin_insta.my_fragments.profile;

public class bottomNavActivity extends AppCompatActivity {

    private TextView mTextMessage;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    // define your fragments here
    final Fragment fragment1 = new home();
    final Fragment fragment2 = new createPics();
    final Fragment fragment3 = new profile();
    final Fragment fragment4 = new details();



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flContainer, fragment1).commit();

                    return true;

                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);
                    FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                    fragmentTransaction2.replace(R.id.flContainer, fragment2).commit();
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                    fragmentTransaction3.replace(R.id.flContainer, fragment3).commit();
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        //mTextMessage.setText(R.string.title_home);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, fragment1).commit();


        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void openDetails(Post post){
        // bundle communication between activity and fragment
        Bundle args = new Bundle();
        args.putString("PostId", post.getObjectId());// unique to every post

        fragment4.setArguments(args);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, fragment4).commit();
    }

}
