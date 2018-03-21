package com.example.askel.lab04;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by askel on 17/03/2018.
 */

public class UserFragment extends Fragment {
    private static final String TAG = "Tab2Frag";

    private DatabaseReference mDb;


    private ListView listView;
    private ArrayList<String> userList;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, container, false);

        mDb = FirebaseDatabase.getInstance().getReference();
        listView = (ListView) view.findViewById(R.id.userlist);

        userList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                userList
        );

        listView.setAdapter(adapter);

        mDb.child("users").addChildEventListener(new OnUserListener());

        listView.setOnItemClickListener(new ItemListener());

        return view;
    }


    //  When we click on an item in the listview
    private class ItemListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {

            Intent intent = new Intent (getActivity(), A3.class);
            intent.putExtra("THATUSER", userList.get(position));
            startActivity (intent);
        }
    }

    //     Update listview of users from db
    private class OnUserListener implements ChildEventListener{
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String retrievedUser = dataSnapshot.getValue(String.class);
            userList.add(retrievedUser);
            Collections.sort(userList, String.CASE_INSENSITIVE_ORDER);
            adapter.notifyDataSetChanged();
            listView.setSelection(adapter.getCount() - 1);
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
