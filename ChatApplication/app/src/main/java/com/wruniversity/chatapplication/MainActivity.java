package com.wruniversity.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.wruniversity.chatapplication.RegisterActivity.EXTRA_USER_REGISTER_SUCCESSFULLY;

public class MainActivity extends AppCompatActivity {
    public static String Usernamels;
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin;
    private CheckBox mCheckBoxsaveinfo;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mDatabaseReference;
    private String Pref_name="info_login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
/*        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, Chat.class));
            finish();
        }
*/
        // set the view now
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        mCheckBoxsaveinfo=(CheckBox)findViewById(R.id.savePassword) ;
        restoreSharePreference();
        //restoreSharePreference();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
        restoreSharePreference();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                if(mCheckBoxsaveinfo.isChecked())
                    savingPreferences();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Auth success    ", Toast.LENGTH_LONG).show();
                                        Usernamels =task.getResult().getUser().getEmail();
                                    Intent intent = new Intent(MainActivity.this, MainNavActivity.class);
                                    startActivityForResult(intent,1);
                                    finish();

                                } else {
                                    Toast.makeText(MainActivity.this, "Auth failed    " + task.getException(), Toast.LENGTH_LONG).show();


                                }
                            }
                        });
            }
        });
    }

    private  void restoreSharePreference(){
        SharedPreferences mSharedPreferences=getSharedPreferences(Pref_name, MODE_PRIVATE);
        boolean rememberPassword=mSharedPreferences.getBoolean("savePassword",false);
        if(rememberPassword)
        {
            String userLogin=mSharedPreferences.getString("userLogin","");
            String password=mSharedPreferences.getString("password","");
            inputEmail.setText(userLogin);
            inputPassword.setText(password);
        }
        mCheckBoxsaveinfo.setChecked(rememberPassword);



    }
    public void savingPreferences()
    {
        SharedPreferences mSharedPreferences=getSharedPreferences(Pref_name,MODE_PRIVATE);
        SharedPreferences.Editor mSharePreferenceEditor=mSharedPreferences.edit();
        mCheckBoxsaveinfo=(CheckBox)findViewById(R.id.savePassword) ;
        boolean rememberPassword=mCheckBoxsaveinfo.isChecked();
        if(!rememberPassword){
            mSharePreferenceEditor.clear();
        }
        else {

            mSharePreferenceEditor.putString("userLogin",inputEmail.getText().toString());
            mSharePreferenceEditor.putString("password",inputPassword.getText().toString());
            mSharePreferenceEditor.putBoolean("savePassword",rememberPassword);
            mSharePreferenceEditor.apply();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        savingPreferences();
    }
    @Override
    protected void onResume() {

        super.onResume();
        restoreSharePreference();
    }
}