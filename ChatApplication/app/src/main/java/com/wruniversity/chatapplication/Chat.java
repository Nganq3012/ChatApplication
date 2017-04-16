package com.wruniversity.chatapplication;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable; import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater; import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button; import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.adobe.creativesdk.aviary.internal.headless.utils.MegaPixels;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hitomi.cmlibrary.CircleMenu;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.wruniversity.chatapplication.Model.MessageInfo;
import com.wruniversity.chatapplication.Model.UserChat;
import com.wruniversity.chatapplication.ui.widget.CustomLightboxActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class Chat extends AppCompatActivity implements EmojiconGridFragment.OnEmojiconClickedListener,EmojiconsFragment
        .OnEmojiconBackspaceClickedListener, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(18.679585, 105.681335), new LatLng(20.351387, 105.221214));
    private ChildEventListener mChildEventListener = null;
    public  final int PICK_PHOTO_FROM_GALLERY = 2000;
    public  MessageInfo friendlyMessage;
    public  MessageAdapter mMessageAdapter;
    public  List<MessageInfo> friendlyMessages = new ArrayList<MessageInfo>();
    public final int TAKE_PHOTO = 3000;
    public final int PLACE_PICKER_REQUEST= 100;
    public final String ANONYMOUS = "anonymous";
    public final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static String messsages = "";
    private ImageView mEmojiIcon;
    private CoordinatorLayout containerLayout;
    private ListView mMessageListView;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    private Button mSendButton;
    private ImageView mImageViewsend;
    public static String mUsername;
    public static String receive = "";
    // Firebase instance variables
    private FirebaseAuth mfirebaseAuth;
    private FirebaseAuth.AuthStateListener  mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference mDatabaseReference;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;
    public static String pathNameReceive = "";
    public static String pathNameSend = "";
    String mGender;
    private ListView mListView;
    Uri uriFile;Runnable mRunnable;
    private String displayname;
    private EmojiconEditText mEmojiconEditText;
    private boolean isInit = false;
    public static Uri selectedImageUri;
    StorageReference photoRef;
    private int[] SharePlaceTypes = new int[]{1, 2, 4, 2, 4, 6, 4, 3, 2};
    private boolean init = false;
    private BoomMenuButton bmb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ActionBar mActionBar = this.getSupportActionBar();
        if (mActionBar != null)
            mActionBar.setDisplayHomeAsUpEnabled(true);
       RelativeLayout frameLayout = (RelativeLayout) findViewById(R.id.emojicons);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
       mEmojiconEditText = (EmojiconEditText) findViewById(R.id.emojicon_edit_text);
       mMessageListView = (ListView) findViewById(R.id.messageListView);
       mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessageAdapter = new MessageAdapter(Chat.this, R.layout.item_message_send, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);
        mfirebaseAuth = FirebaseAuth.getInstance();
        attachDatabaseReadListener();

        mFirebaseStorage = FirebaseStorage.getInstance();
       mEmojiIcon = (ImageView) findViewById(R.id.img_emoji_btn);
       Intent myIntent = getIntent();
       if (myIntent.hasExtra(UserChat.EXTRA_DATA)) {
           receive = myIntent.getStringExtra(UserChat.EXTRA_DATA);
           mGender=myIntent.getStringExtra(UserChat.EXTRA_GENDER);
       }
       displayname = mfirebaseAuth.getCurrentUser().getDisplayName(); // loadConversationList();
       mEmojiconEditText.setVisibility(View.GONE);
       containerLayout = (CoordinatorLayout) findViewById(R.id.container);
       mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
       // Get the ActionBar
       ActionBar ab = getSupportActionBar();
       // Create a gradient drawable programmatically
       GradientDrawable gradient = new GradientDrawable();
       gradient.setColors(new int[]{
               Color.parseColor("#feffe9"),
               Color.parseColor("#fff600"),
               Color.parseColor("#feffe9"),
       });
        if(mGender =="Female")
            ab.setIcon(R.drawable.user_chat2);
        else
            ab.setIcon(R.drawable.user_chat1);
       gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
       gradient.setShape(GradientDrawable.RECTANGLE);
       gradient.setStroke(2, Color.parseColor("#cbb700"));
       ab.setBackgroundDrawable(gradient);

       // Create a TextView programmatically.
       TextView tv = new TextView(getApplicationContext());

       // Create a LayoutParams for TextView
       LayoutParams lp = new RelativeLayout.LayoutParams(
               LayoutParams.MATCH_PARENT, // Width of TextView
               LayoutParams.WRAP_CONTENT); // Height of TextView

       // Apply the layout parameters to TextView widget
       tv.setLayoutParams(lp);

       // Set text to display in TextView
       tv.setText(receive);

       // Set the text color of TextView
       tv.setTextColor(Color.GREEN);
       tv.setTypeface(Typeface.MONOSPACE);
       // Set TextView text alignment to center
       tv.setGravity(Gravity.CENTER);
       tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
       // Set the ActionBar display option
       ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

       // Finally, set the newly created TextView as ActionBar custom view
       ab.setCustomView(tv);

        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
        mImageViewsend = (ImageView) findViewById(R.id.txtImage);
        mUsername = mfirebaseAuth.getCurrentUser().getEmail();
        String[] parts = mUsername.split("@");
        pathNameSend = parts[0];
        String[] part2 = receive.split("@");
        pathNameReceive = part2[0];

        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("photo/" + pathNameSend + "_" + pathNameReceive);
        mDatabaseReference = mFirebaseDatabase.getReference().child("messages/" + pathNameSend + "_" + pathNameReceive);
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");

        bmb = (BoomMenuButton) findViewById(R.id.photoPickerButton);
       initViews();
        mRunnable=new Runnable() {
            @Override
            public void run() {
                mMessageAdapter.notifyDataSetChanged();
                mMessageListView.invalidate();
                mMessageListView.refreshDrawableState();
            }
        };
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
       mEmojiconEditText.setOnKeyListener(new View.OnKeyListener()
       {
           public boolean onKey(View v, int keyCode, KeyEvent event)
           {
               if (event.getAction() == KeyEvent.ACTION_DOWN)
               {
                   switch (keyCode)
                   {
                       case KeyEvent.KEYCODE_DPAD_CENTER:
                       case KeyEvent.KEYCODE_ENTER:

                           // Initialize Firebase components
                           SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault());
                           Date date = new Date();
                           String str = simpleDateFormat.format(date.getTime());
                           messsages = mEmojiconEditText.getText().toString();
                           friendlyMessage = new MessageInfo(null,null,mGender,null, messsages, null, mUsername, receive, true, true, str, mfirebaseAuth.getCurrentUser().getDisplayName(), mUsername + "_" + receive);
                           FirebaseDatabase.getInstance().getReference().child("messages/" + pathNameSend + "_" + pathNameReceive).push().setValue(friendlyMessage);
                           FirebaseDatabase.getInstance().getReference().child("messages/" + pathNameReceive + "_" + pathNameSend).push().setValue(friendlyMessage);
                            mEmojiconEditText.setText("");
                           return true;
                       default:
                           break;
                   }
               }
               return false;
           }
       });
        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize Firebase components
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault());
                Date date = new Date();
                String str = simpleDateFormat.format(date.getTime());

                if (mMessageEditText.getVisibility() == View.VISIBLE) {
                    messsages = mMessageEditText.getText().toString();
                    friendlyMessage = new MessageInfo(null,null,mGender,messsages, null, null, mUsername, receive, true, true, str, mfirebaseAuth.getCurrentUser().getDisplayName(), mUsername + "_" + receive);
                }

                FirebaseDatabase.getInstance().getReference().child("messages/" + pathNameSend + "_" + pathNameReceive).push().setValue(friendlyMessage);
                FirebaseDatabase.getInstance().getReference().child("messages/" + pathNameReceive + "_" + pathNameSend).push().setValue(friendlyMessage);
                // Clear input box
                mMessageEditText.setText("");
                mEmojiconEditText.setText("");

            }
        });

        mEmojiIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmojiPopUp(true);
                mEmojiconEditText.setVisibility(View.VISIBLE);
                mMessageEditText.setVisibility(View.GONE);
                mEmojiconEditText.setUseSystemDefault(false);
                setEmojiconFragment(false);

            }


        });
       mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                             @Override
                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                 MessageInfo item = (MessageInfo) parent.getItemAtPosition(position);
                                 String videoID=null;
                                 if (item.getURLYYB()!=null) {
                                     videoID=extractYTId(item.getURLYYB());
                                     if (YouTubeIntents.canResolvePlayVideoIntent(getApplicationContext())) {
                                         final Intent lightboxIntent = new Intent(Chat.this, CustomLightboxActivity.class);
                                         lightboxIntent.putExtra(CustomLightboxActivity.KEY_VIDEO_ID,videoID);
                                         startActivity(lightboxIntent);
                                     }
                                 }
                             }
                         });
        mMessageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            MessageInfo item = (MessageInfo) parent.getItemAtPosition(position);
            if(item.getPhotoUrl()!=null){

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(item.getPhotoUrl());
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.i("Main", "File uri: " + uri.toString());
                }
            });

            //download the file
            try {
                final File localFile = File.createTempFile("images" + String.valueOf(new SimpleDateFormat("yyyy-MM-dd")), "jpg");
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        Toast.makeText(getApplicationContext(), "Download successful!", Toast.LENGTH_LONG);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Download failed!", Toast.LENGTH_LONG);

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Main", "IOE Exception");
            }
        }
        return false;
    }
});
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                AuthUI.getInstance().signOut(this);
                Intent newIntent= new Intent(Chat.this,MainActivity.class);
                startActivity(newIntent);
                return true;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }








    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void attachDatabaseReadListener() {
         mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mChildEventListener == null) {
                    //mMessageAdapter.clear();
                    mChildEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            MessageInfo friendlyMessage = dataSnapshot.getValue(MessageInfo.class);
                            mMessageAdapter.add(friendlyMessage);
                        }

                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                        }

                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        }

                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    runOnUiThread(mRunnable);
                    mDatabaseReference.addChildEventListener(mChildEventListener);

                }

            };
        };
    }
            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                EmojiconsFragment.input(mEmojiconEditText, emojicon);
                hideEmojiPopUp(true);
            }

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                EmojiconsFragment.backspace(mEmojiconEditText);
                setEmojiconFragment(false);

            }
            private void setEmojiconFragment(boolean useSystemDefault) {
                getSupportFragmentManager().beginTransaction().replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault)).commit();
            }
            public void hideEmojiPopUp(boolean hideEmoji) {
                RelativeLayout frameLayout = (RelativeLayout) findViewById(R.id.emojicons);
                frameLayout.setVisibility(View.GONE);
            }
            public void showEmojiPopUp(boolean hideEmoji) {
                RelativeLayout frameLayout = (RelativeLayout) findViewById(R.id.emojicons);
            frameLayout.setVisibility(View.VISIBLE);
            }




    private void initViews() {
        initBoom();
    }


    private PlaceType getPlaceType() {
        return PlaceType.SHARE_4_2;
    }

    private String[] Colors = {
            "#F44336",
            "#E91E63",
            "#9C27B0",
            "#2196F3",
            "#03A9F4",
            "#00BCD4",
            "#009688",
            "#4CAF50",
            "#8BC34A",
            "#CDDC39",
            "#FFEB3B",
            "#FFC107",
            "#FF9800",
            "#FF5722",
            "#795548",
            "#9E9E9E",
            "#607D8B"};

    public int getRandomColor() {
        Random random = new Random();
        int p = random.nextInt(Colors.length);
        return Color.parseColor(Colors[p]);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Use a param to record whether the boom button has been initialized
        // Because we don't need to init it again when onResume()
        if (init) return;
        init = true;

        initBoom();
    }

    public void updateUserlist(){

            Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("status").startAt(mfirebaseAuth.getCurrentUser().getEmail()+"_"+true);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                        userSnapshot.getRef().child("status").setValue(mfirebaseAuth.getCurrentUser().getEmail()+"_"+"false");

                        HashMap<String, Object> result = new HashMap<>();
                        result.put("status", mfirebaseAuth.getCurrentUser().getEmail()+"_"+"false");

                        FirebaseDatabase.getInstance().getReference().child("Users").updateChildren(result); }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
    }

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
        iconToBeChanged.setImageResource(drawableResourceId);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_OUTSIDE:
                hideEmojiPopUp(true);
                return true;
        }
        return false;
    }

    private void initBoom() {

        Drawable[] drawables = new Drawable[4];
        int[] drawablesResource = new int[]{
                R.drawable.mark,
                R.drawable.refresh,
                R.drawable.copy,
                R.drawable.heart
        };
        for (int i = 0; i < 4; i++)
            drawables[i] = ContextCompat.getDrawable(this, drawablesResource[i]);

        String[] STRINGS = new String[]{
                "Image gallery",
                "Info",
                "Youtube video",
                "Place"
        };
        String[] strings = new String[4];
        for (int i = 0; i < 4; i++)
            strings[i] = STRINGS[i];

        int[][] colors = new int[4][2];
        for (int i = 0; i < 4; i++) {
            colors[i][1] = getRandomColor();
            colors[i][0] = Util.getInstance().getPressedColor(colors[i][1]);
        }

        // Now with Builder, you can init BMB more convenient
        new BoomMenuButton.Builder()
                .subButtons(drawables, colors, strings)
                .button(ButtonType.CIRCLE)
                .boom(BoomType.HORIZONTAL_THROW_2)
                .place(getPlaceType())
                .boomButtonShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .shareStyle(3f, getRandomColor(), getRandomColor())
                .init(bmb);
        bmb.setOnSubButtonClickListener(new BoomMenuButton.OnSubButtonClickListener() {
            @Override
            public void onClick(int buttonIndex) {
                if (buttonIndex == 0)
                {       Intent newIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    newIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(newIntent, "action pick"), PICK_PHOTO_FROM_GALLERY);
                }
                else if(buttonIndex ==1) {
                    AlertDialog.Builder mBuilderAlertDialog= new AlertDialog.Builder(getApplicationContext());
                    mBuilderAlertDialog.setMessage("Chat appplication with send messages,image with editor image, emoji ," +
                            "Entertain with the game and share location of user").setIcon(R.drawable.info).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                             dialog.cancel();
                        }
                    });
                }
                else if(buttonIndex==2) {

                    Intent mIntent=new Intent(Chat.this,SearchActivity.class);
                    startActivity(mIntent);
                }
                else if(buttonIndex==3){
                    displayPlacePicker();
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final String path = "";
        selectedImageUri=null;

        if (requestCode == PLACE_PICKER_REQUEST){
            Place place = PlacePicker.getPlace(Chat.this, data);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault());
            Date date = new Date();
            String str = simpleDateFormat.format(date.getTime());
            String address =  place.getAddress().toString();
            friendlyMessage = new MessageInfo(address, null, mGender, null, null, null, mUsername, receive, true, true, str, mfirebaseAuth.getCurrentUser().getDisplayName(), mUsername + "_" + receive);
            FirebaseDatabase.getInstance().getReference().child("messages/" + pathNameSend + "_" + pathNameReceive).push().setValue(friendlyMessage);
            FirebaseDatabase.getInstance().getReference().child("messages/" + pathNameReceive + "_" + pathNameSend).push().setValue(friendlyMessage);
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO ) {
           /*   if (uriFile != null) {

                  selectedImageUri = uriFile;
                                getContentResolver().notifyChange(selectedImageUri, null);
             } else selectedImageUri = data.getData();
                            Intent intent = new AdobeImageIntent.Builder(getApplicationContext()).setData(selectedImageUri).withOutputSize(MegaPixels.Mp0).withOutputQuality(10).build();
                finish();
                            startActivityForResult(intent, 1);
*/
            }
            else if (requestCode == PICK_PHOTO_FROM_GALLERY) {
                selectedImageUri = data.getData();
                Intent intent = new AdobeImageIntent.Builder(getApplicationContext()).setData(selectedImageUri)
                        .withOutputSize(MegaPixels.Mp0).withOutputQuality(10).build();
                startActivityForResult(intent, 1);

            }


            Uri editedImageUri = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
            Uri mUri=null;

            if (editedImageUri != null) {
            mUri = editedImageUri;
              }
                 else {
              mUri = selectedImageUri;
            }
            photoRef = mChatPhotosStorageReference.child(mUri.getLastPathSegment());
                // Upload file to Firebase Storage
                photoRef.putFile(mUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // When the image has successfully uploaded, we get its download URL
                        attachDatabaseReadListener();
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        String UrlString = "";
                        if (downloadUrl != null) {
                            UrlString = downloadUrl.toString();
                        }
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault());
                        Date date = new Date();
                        String str = simpleDateFormat.format(date.getTime());
                        friendlyMessage = new MessageInfo(null,null,mGender, null, null, UrlString, mUsername, receive, true, true, str, displayname, mUsername + "_" + receive);
                        FirebaseDatabase.getInstance().getReference().child("messages/"+pathNameSend+"_"+pathNameReceive).push().setValue(friendlyMessage);
                        FirebaseDatabase.getInstance().getReference().child("messages/"+pathNameReceive+"_"+pathNameSend).push().setValue(friendlyMessage);
                        finish();
                    }

                    ;
                });
            }

        }



    public static String extractYTId(String ytUrl) {
        String vId = null;
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(ytUrl);

        if(matcher.find()){
            vId =matcher.group();
        }
        return vId;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mfirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        mMessageAdapter.clear();
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mMessageAdapter.clear();
        Intent myIntent = getIntent();
        if (myIntent.hasExtra(UserChat.EXTRA_DATA)) {
            receive = myIntent.getStringExtra(UserChat.EXTRA_DATA);
        }
        mUsername = mfirebaseAuth.getCurrentUser().getEmail();
        String[] parts = mUsername.split("@");
        pathNameSend = parts[0];
        String[] part2 = receive.split("@");
        pathNameReceive = part2[0];
        mfirebaseAuth.addAuthStateListener(mAuthStateListener);
   }
    void downloadFile(String path)
    {
        //View btnDownloadAsFile = findViewById(R.id.btn_download_file);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://filestorage-d5afb.appspot.com").child("photo");

        //get download file url
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("Main", "File uri: " + uri.toString());
            }
        });

        //download the file
        try {
            final File localFile = File.createTempFile("images"+String.valueOf(new SimpleDateFormat("yyyy-MM-dd")), "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                    Toast.makeText(getApplicationContext(),"Download successful!",Toast.LENGTH_LONG);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(),"Download failed!",Toast.LENGTH_LONG);

                }
            });
        } catch (IOException e ) {
            e.printStackTrace();
            Log.e("Main", "IOE Exception");
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if( mGoogleApiClient != null )
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
    private void displayPlacePicker() {
        if( mGoogleApiClient == null || !mGoogleApiClient.isConnected() )
            return;

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult( builder.build(Chat.this), PLACE_PICKER_REQUEST );
        } catch ( GooglePlayServicesRepairableException e ) {
            Log.d( "PlacesAPI Demo", "GooglePlayServicesRepairableException thrown" );
        } catch ( GooglePlayServicesNotAvailableException e ) {
            Log.d( "PlacesAPI Demo", "GooglePlayServicesNotAvailableException thrown" );
        }
    }
}



