package com.wruniversity.chatapplication;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightonke.boommenu.BoomMenuButton;
import com.wruniversity.chatapplication.Model.UserChat;

import java.util.ArrayList;

/**
 * The Class UserList is the Activity class. It shows a list of all users of
 * this app. It also shows the Offline/Online status of users.
 */

public class UserActivity extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener,SearchView.OnQueryTextListener{


        public UserAdapter ad;
        /**
         * The Chat list.
         */
        public static String userClick;
        public ArrayList<UserChat> uList;
        /**
         * Users database reference
         */
        private DatabaseReference database;
        private FirebaseAuth mFirebaseAuth;
        private BoomMenuButton boomMenuButton;
        private ListView list;
        private ChildEventListener mChildEventListener = null;
        private FirebaseAuth mfirebaseAuth;

        public UserActivity() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View v=inflater.inflate(R.layout.activity_user, container, false);
            uList = new ArrayList<UserChat>();
            ActionBar mActionBar = getActivity().getActionBar();
            if (mActionBar != null)
                mActionBar.setDisplayHomeAsUpEnabled(true);
            //View v=getView();
            list = (ListView) v.findViewById(R.id.list);
            ad = (new UserAdapter(getActivity(), R.layout.user_list, uList));
            list.setAdapter(ad);

            // Get reference to the Firebase database
            database = FirebaseDatabase.getInstance().getReference().child("Users");
            attachDatabaseReadListener();
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    UserChat item = (UserChat) parent.getItemAtPosition(position);
                    userClick=item.getEmail();
                    startActivity(new Intent(getActivity(),
                            Chat.class).putExtra(UserChat.EXTRA_DATA, item.getEmail())
                            .putExtra(UserChat.EXTRA_DATA_USER, item.getUsername()).putExtra(UserChat.EXTRA_GENDER, item.getGender()));
                    ;
                }
            });
            return v;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            //updateUserStatus(false);
        }

        /* (non-Javadoc)
         * @see android.support.v4.app.FragmentActivity#onResume()
         */
        @Override
        public void onResume() {
            super.onResume();
            attachDatabaseReadListener();
/*
        loadUserList();
*/

        }



        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(getString(R.string.pref_online_key))) {

            }
        }
        /*
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000ff")));*/


        /**
         * The Class UserAdapter is the adapter class for User ListView. This
         * adapter shows the user name and it's only online status for each item.
         */

        private void attachDatabaseReadListener() {
            if (mChildEventListener == null) {
                mChildEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        UserChat friendlyMessage = dataSnapshot.getValue(UserChat.class);
                        if(!friendlyMessage.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                            uList.add(friendlyMessage);
                        ad.notifyDataSetChanged();

                    }

                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    public void onCancelled(DatabaseError databaseError) {}
                };
                database.addChildEventListener(mChildEventListener);
            }

        }

        @Override
        public boolean onQueryTextSubmit(String query) {

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }

        public class UserAdapter extends ArrayAdapter<UserChat> {
            private Context context; //context
            private ArrayList<UserChat> items; //data source of the list adapter


            public UserAdapter(Context context,int resource, ArrayList<UserChat> items) {
                super(context, resource, items);
            }
            @Override
            public int getCount() {
                return uList.size();
            }
            @Override
            public UserChat getItem(int arg0) {
                return uList.get(arg0);
            }
            @Override
            public long getItemId(int arg0) {
                return arg0;
            }
            @Override
            public View getView(int pos, View v, ViewGroup arg2) {
                if (v == null)
                    v = LayoutInflater.from(getContext()).inflate(R.layout.user_list, null);

                UserChat c = getItem(pos);
                if(!c.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                    TextView lbl = (TextView) v.findViewById(R.id.Userchat);
                    lbl.setText(c.getEmail());
                    lbl.setCompoundDrawablesWithIntrinsicBounds(c.isFemale()? R.drawable.user_chat1 : R.drawable.user_chat2
                            , 0, R.drawable.ic_online, 0);
                }
                return v;
            }


        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_user, menu);
            MenuItem item =menu.findItem(R.id.search_view);
            SearchView mSearchView= (SearchView) item.getActionView();

        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.search_view:
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }
