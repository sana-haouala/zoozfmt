package tn.rnu.fmt.zoozfmt.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

import tn.rnu.fmt.zoozfmt.R;
import tn.rnu.fmt.zoozfmt.SuperAdminProfileActivity;

public class ListCheckedClientActivity extends AppCompatActivity {

    //firebase database ref
    private DatabaseReference mRoot = FirebaseDatabase.getInstance().getReference();
    //listView
    private ListView mListView;
    private ArrayList<String> mClientnames = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(ListCheckedClientActivity.this,AdminProfileActivity.class));
                    return true;
                case R.id.navigation_check:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_checked_client);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(1).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mListView = (ListView) findViewById(R.id.clientList);
        arrayAdapter = new ArrayAdapter<String>(ListCheckedClientActivity.this, android.R.layout.simple_list_item_1, mClientnames);
        mListView.setAdapter(arrayAdapter);
        DatabaseReference myRefChild = mRoot.child("Clients");

        myRefChild.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map<String, String> map =  (Map<String, String>) dataSnapshot.getValue();
                String firstname = map.get("eFirstname");
                String lastname = map.get("eLastname");

                mClientnames.add(firstname+" "+lastname);

                arrayAdapter.notifyDataSetChanged();

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
        });

    }
}
