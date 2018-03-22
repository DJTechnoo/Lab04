package com.example.askel.lab04;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class A3 extends AppCompatActivity {
    private String userName;
    private ListView listView;
    private ArrayList<String> msgList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a3);

        DatabaseReference mDb;

        mDb = FirebaseDatabase.getInstance().getReference();

        //  Get "userName" from users-tab to see all msg from
        Intent intent = getIntent();
        userName = intent.getStringExtra("THATUSER");

        //  Fire up the listview and adapter
        listView = (ListView) findViewById(R.id.lv_msg_user);
        msgList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                msgList
        );
        listView.setAdapter(adapter);

        mDb.child("msg").addChildEventListener(new UserMessagesListener());
    }

    //  List all messages from that "userName"
    private class UserMessagesListener implements ChildEventListener{
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if(userName.matches(dataSnapshot.child("u").getValue(String.class))) {
                String retrievedMsg = dataSnapshot.child("m").getValue(String.class);
                msgList.add(retrievedMsg);
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount() - 1);
            }
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
}
