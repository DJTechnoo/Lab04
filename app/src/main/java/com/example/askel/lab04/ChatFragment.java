package com.example.askel.lab04;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by askel on 17/03/2018.
 */

public class ChatFragment extends Fragment {
    private static final String TAG = "Tab1Frag";

    private Handler handler;
    private Runnable runnable;

    private DatabaseReference mDb;

    // Views
    private Button btn1;
    private TextView txt1;
    private EditText et_msg;
    private ListView messages;
    private final String userPrefFile = "userpref";

    // Lists and Strings
    private String userName;
    private ArrayList<String> messageList;
    private ArrayAdapter<String> adapter;

    private boolean notify = false;

    final static int A2_INTENT = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                                @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);

        handler = new Handler();
        mDb = FirebaseDatabase.getInstance().getReference();

        // get views
        btn1     = (Button) view.findViewById(R.id.btn1);
        txt1     = (TextView) view.findViewById(R.id.txt1);
        et_msg   = (EditText) view.findViewById(R.id.et_msg);
        messages = (ListView) view.findViewById(R.id.chatlist);

        //  Fire up the listview and adapter
        messageList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                messageList
        );
        messages.setAdapter(adapter);

        //  Get the local username from device if used before.
        //  otherwise start new activity for username
        userInit();

        mDb.child("msg").addChildEventListener(new OnChatListener());

        // post message
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postMessage();
            }
        });

        handler = new Handler();

        // run feed update every frequency-time
        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                new notifyInBackGround().execute((Void) null);
                handler.postDelayed(runnable, 5000);
            }
        };
        handler.post(runnable);

        return view;
    }



    private class OnChatListener implements ChildEventListener{

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String retrievedMsg = dataSnapshot.child("m").getValue(String.class);
            messageList.add(retrievedMsg);
            adapter.notifyDataSetChanged();
            messages.setSelection(adapter.getCount() - 1);
            notify = true;
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == A2_INTENT) && (resultCode == getActivity().RESULT_OK)){

            userName = data.getStringExtra("USR");
            txt1.setText(userName);

            SharedPreferences userPref = getActivity().getSharedPreferences(
                    userPrefFile,
                    getActivity().MODE_PRIVATE
            );
            SharedPreferences.Editor userEdit = userPref.edit();
            userEdit.putString("USERNAME", userName);
            userEdit.apply();
            mDb.child("users").push().setValue(userName);
        }
    }

    private void postMessage(){
        //  username + date + message add to db *_*
        String msg;
        msg = et_msg.getText().toString();
        if( (msg != null) && (msg.length() <= 255) && !(msg.matches("")) ) {
          msg = userName + " says: " + msg;
            Map<String, String> map = new HashMap<>();
            map.put("u", userName);
            map.put("m", msg);
            // put date ("d");

            mDb.child("msg").push().setValue(map);
            hideKeyboard(getActivity());
            adapter.notifyDataSetChanged();
        }
        msg = "";
        et_msg.setText("");

    }

    public void userInit(){
        userPref();
        if( (userName == null) || (userName.isEmpty()) ) {
            Intent intent = new Intent(getActivity(), A2.class);
            startActivityForResult(intent, A2_INTENT);
        }
        txt1.setText(userName);
    }

    public void userPref(){
        SharedPreferences userPref = getActivity().getSharedPreferences(
                userPrefFile,
                getActivity().MODE_PRIVATE
        );
        String name = userPref.getString("USERNAME", null);
        userName = name;
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void notificationCall(){
        NotificationCompat.Builder notificationBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(getContext())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("You've Got Mail! XD");

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getContext());
        notificationManager.notify(0, notificationBuilder.build());
        notify = false;
    }

    private class notifyInBackGround extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(notify){
                notificationCall();

            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success){
                super.onPostExecute(success);


            }
        }
    }
}
