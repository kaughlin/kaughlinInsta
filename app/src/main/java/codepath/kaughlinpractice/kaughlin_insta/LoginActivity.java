package codepath.kaughlinpractice.kaughlin_insta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button signupBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.etUsername);
        passwordInput = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.btnLoginButton);
        signupBtn = findViewById(R.id.btnSignup);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gets refrence to what user types in
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                login(username, password);
            }
        });


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gets refrence to what user types in
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                
                signUp(username, password);
            }
        });


        ParseUser currentUser = ParseUser.getCurrentUser();

        // automatic log in
        if(currentUser != null){
            Intent i = new Intent(LoginActivity.this, bottomNavActivity.class);
            startActivity(i);
            Toast.makeText(LoginActivity.this, "Automatically logged in", Toast.LENGTH_SHORT).show();
        }
    }
    private void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){
                    Toast.makeText(LoginActivity.this,"Login successful", Toast.LENGTH_LONG ).show();
                    Log.d("LoginActivity", "login successful!");
                    final Intent intent = new Intent(LoginActivity.this, bottomNavActivity.class);
                    startActivity(intent);
                    finish();

                }
                else{
                    Toast.makeText(LoginActivity.this,"Login Failure", Toast.LENGTH_LONG ).show();
                    Log.e("LoginActivity", "log in failure");
                    e.printStackTrace();
                }
            }
        });

    }

    public void signUp(String username, String password){
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        //user.setEmail("email@example.com");
        // Set custom properties
        //user.put("phone", "650-253-0000");
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Toast.makeText(LoginActivity.this,"Sign up successful", Toast.LENGTH_LONG ).show();
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.e("LoginActivity", "Sign up failure");
                    e.printStackTrace();
                }
            }
        });
    }

}
