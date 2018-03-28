package tn.rnu.fmt.zoozfmt.Client;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import tn.rnu.fmt.zoozfmt.LoginActivity;
import tn.rnu.fmt.zoozfmt.R;

public class ClientProfileActivity extends AppCompatActivity {

    private Intent intent = new Intent();
    private String key;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_how:
                    intent = new Intent(ClientProfileActivity.this,LearnHowActivity.class);
                    intent.putExtra("key",key);
                    startActivity(intent);
                    return true;
                case R.id.navigation_periode:
                    intent = new Intent(ClientProfileActivity.this,ClientPeriodDetailsActivity.class);
                    intent.putExtra("key",key);
                    startActivity(intent);
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
        setContentView(R.layout.activity_client_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        TextView eName = (TextView) findViewById(R.id.account_name);
        TextView eEmail = (TextView) findViewById(R.id.account_email);
        TextView eBirthday = (TextView) findViewById(R.id.account_birthday);
        TextView ePhone = (TextView) findViewById(R.id.account_phone) ;
        TextView eNbChildren = (TextView) findViewById(R.id.account_nbchildren);

        mAuth = FirebaseAuth.getInstance();

        key = intent.getStringExtra("key");

        /*eName.setText(getString(R.string.description_title));
        eEmail.setText(getString(R.string.first_tip));
        eBirthday.setText(getString(R.string.second_tip));
        ePhone.setText(getString(R.string.third_tip));
        eNbChildren.setText(getString(R.string.conclusion));*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_learn_how, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mAuth.signOut();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(ClientProfileActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
