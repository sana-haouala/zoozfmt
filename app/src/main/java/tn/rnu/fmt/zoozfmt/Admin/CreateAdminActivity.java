package tn.rnu.fmt.zoozfmt.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import tn.rnu.fmt.zoozfmt.Client.ClientProfileActivity;
import tn.rnu.fmt.zoozfmt.Client.SignUpClientActivity;
import tn.rnu.fmt.zoozfmt.R;
import tn.rnu.fmt.zoozfmt.SuperAdminProfileActivity;

public class CreateAdminActivity extends AppCompatActivity {

    private static final String TAG = "CreateAdminActivity";

    //UI references
    private TextInputLayout mFirstname;
    private TextInputLayout mLastname;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button adminreg;

    // Progress Dialog Object
    private ProgressDialog prgDialog;
    //firebase ref
    private DatabaseReference mRootDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(CreateAdminActivity.this, SuperAdminProfileActivity.class));
                    return true;
                case R.id.navigation_add:
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
        setContentView(R.layout.activity_create_admin);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(1).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //Referencing EditText widgets and Button placed inside in xml layout file
        mFirstname = (TextInputLayout) findViewById(R.id.firstname_reg);
        mLastname = (TextInputLayout) findViewById(R.id.lastname_reg);
        mEmail = (TextInputLayout) findViewById(R.id.email_reg);
        mPassword = (TextInputLayout) findViewById(R.id.pass_reg);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage(getString(R.string.wait_msg));
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        adminreg = (Button) findViewById(R.id.btn_reg);

    }
    public boolean validateForm(){
        //Validation for Blank Field
        if((!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail.getEditText().getText().toString()).matches())||(mEmail.getEditText().getText().toString().trim().equals("")))
        {
         //Validation for Invalid Email Address
            //Toast.makeText(getApplicationContext(), "Please enter a valid Email to receive notification", Toast.LENGTH_LONG).show();
            mEmail.setError(getString(R.string.invalid_field));
            return false;
        }
        else
        {
            Toast.makeText(getApplicationContext(), getString(R.string.signup_valid), Toast.LENGTH_LONG).show();
            return true;
        }
    }
    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        adminreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    createAdmin(mEmail.getEditText().getText().toString(), mPassword.getEditText().getText().toString());
                    //values
                    String firstName = mFirstname.getEditText().getText().toString();
                    String lastName = mLastname.getEditText().getText().toString();
                    String password = mPassword.getEditText().getText().toString();
                    String email = mEmail.getEditText().getText().toString();

                    DatabaseReference myRefAdmin = mRootDatabase.child("Admins");
                    DatabaseReference mNewAdmin = myRefAdmin.push();
                    mNewAdmin.setValue(new Admin(firstName,lastName,password,email));


                }
                else{
                    Toast.makeText(getApplicationContext(), getString(R.string.verify_field), Toast.LENGTH_LONG).show();
            }
            }
        });
    }
    private void createAdmin(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        prgDialog.show();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAdminActivity.this, getString(R.string.failed_auth),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        prgDialog.dismiss();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void updateUI(FirebaseUser user) {
        prgDialog.dismiss();
        if (user != null) {

            String email = user.getEmail().toString();

        } else {

            Toast.makeText(CreateAdminActivity.this, R.string.sign_up_error,
                    Toast.LENGTH_SHORT).show();
        }
    }


}
