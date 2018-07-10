package codepath.kaughlinpractice.kaughlin_insta;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import codepath.kaughlinpractice.kaughlin_insta.model.Post;

public class ParseApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("kaughlin-ig")
                .clientKey("Aa3ecdaf")
                .server("http://kaughlin-insta.herokuapp.com/parse/")
                .build();
        Parse.initialize(configuration);
    }


}
