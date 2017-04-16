package com.wruniversity.chatapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.wruniversity.chatapplication.Model.MessageInfo;
import com.wruniversity.chatapplication.Model.UserChat;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Register is the Activity class that shows user registration screen
 * that allows user to register itself on Parse server for this Chat app.
 */
public class RegisterActivity extends AppCompatActivity {
    public static final String EXTRA_USER_REGISTER_SUCCESSFULLY = "User register successfully";
    private Spinner inputGender;
    private ProgressBar progressBar;
    private ChildEventListener mChildEventListener;
    private String mGender;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseReference;
    private String mEmail;
    private EditText pwd1;
    private EditText email1;
    private EditText displayName1;

    private ProgressDialog registerProgressDlg;
    private Button btnSignUp;

    /* (non-Javadoc)
     * @see com.chatt.custom.CustomActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputGender = (Spinner) findViewById(R.id.gender);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_spinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        inputGender.setAdapter(adapter);

        pwd1 = (EditText) findViewById(R.id.password);
        email1 = (EditText) findViewById(R.id.email);
        displayName1 = (EditText) findViewById(R.id.username);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password = pwd1.getText().toString();
                final String email = email1.getText().toString();
                final String displayName = displayName1.getText().toString();
                inputGender.setOnItemSelectedListener(new SpinnerActivity());
                if (mGender == null)
                    mGender = "Female";

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(displayName)) {
                    Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);


                // Register the user
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                            Toast.makeText(RegisterActivity.this, " Authentication false." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(RegisterActivity.this, " Authentication true." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            UserChat friendlyUser = new UserChat(task.getResult().getUser().getUid(), displayName, task.getResult().getUser().getEmail(),inputGender.getSelectedItem().toString(),task.getResult().getUser().getEmail()+"_"+true);
                            /*String[] parts = friendlyUser.getEmail().split("@");
                            mEmail = parts[0];*/
                            final String key = FirebaseDatabase.getInstance()
                                    .getReference().child("Users").push().getKey();
                            FirebaseDatabase.getInstance().getReference().child("Users").push().setValue(friendlyUser);
                            final Intent intent = new Intent(RegisterActivity.this, MainActivity.class);


                            // Update the user profile information
                            final FirebaseUser user = task.getResult().getUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayName)
                                    .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                    .build();
                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, " Authentication update csuccess." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
      /*  mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                UserChat friendlyMessage = dataSnapshot.getValue(UserChat.class);
                uList.add(friendlyMessage);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        };*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            mGender = inputGender.getSelectedItem().toString();
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback

        }
    }

}
