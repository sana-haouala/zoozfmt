package tn.rnu.fmt.zoozfmt;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import tn.rnu.fmt.zoozfmt.Admin.AdminProfileActivity;
import tn.rnu.fmt.zoozfmt.Client.ClientProfileActivity;
import tn.rnu.fmt.zoozfmt.Client.SignUpClientActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailLogin";

    //user role
    ///private final String role = intent.getStringExtra("role");
    //UI references
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button registerBtn;
    private Button loginBtn;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    DatabaseReference mRoot = FirebaseDatabase.getInstance().getReference();

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        mEmail = (TextInputLayout) findViewById(R.id.emailInput);
        mPassword = (TextInputLayout) findViewById(R.id.password);
        registerBtn = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        loginBtn = (Button) findViewById(R.id.btnLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               signIn(mEmail.getEditText().getText().toString(),mPassword.getEditText().getText().toString());
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpClientActivity.class);
                startActivity(i);
            }
        });
        //Verify the granted permission
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                android.Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED){
            // Initialize Facebook Login button
            mCallbackManager = CallbackManager.Factory.create();
            LoginButton loginButton = findViewById(R.id.facebook_btn);
            loginButton.setReadPermissions("email", "public_profile");
            loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG, "facebook:onSuccess:" + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "facebook:onCancel");
                    // [START_EXCLUDE]
                    updateUI(null);
                    // [END_EXCLUDE]
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d(TAG, "facebook:onError", error);
                    // [START_EXCLUDE]
                    updateUI(null);
                    // [END_EXCLUDE]
                }
            });
            // [END initialize_fblogin]
        }
        else{
            //Request Token Permission
            checkTokenPermission();
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_CALENDAR = 99;
    private void checkTokenPermission() {
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                android.Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, android.Manifest.permission.WRITE_SETTINGS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle(getString(R.string.token_permission_title))
                        .setMessage(getString(R.string.token_permission_body))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LoginActivity.this,
                                        new String[]{android.Manifest.permission.WRITE_SETTINGS},
                                        MY_PERMISSIONS_REQUEST_CALENDAR);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{android.Manifest.permission.WRITE_SETTINGS},
                        MY_PERMISSIONS_REQUEST_CALENDAR);
            }
        }
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START on_activity_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    // [END on_activity_result]

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_facebook]

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        //baseActivity.showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        //baseActivity.hideProgressDialog();
                    }
                });
        // [END sign_in_with_email]
    }
    @SuppressLint("ResourceType")
    private boolean validateForm() {
        boolean valid = true;

        String email = mEmail.getEditText().getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.required_field));
            valid = false;
        } else {
            mEmail.setError(null);
        }

        String password = mPassword.getEditText().getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.required_field));
            valid = false;
        } else {
            mPassword.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        //baseActivity.hideProgressDialog();
        if (user != null) {
            /*Intent intent = new Intent(LoginActivity.this,ClientProfileActivity.class);
            intent.putExtra("uid",user.getUid());
            intent.putExtra("name",user.getDisplayName());
            startActivity(intent);*/

            DatabaseReference mClientRef = mRoot.child("Clients");
            Query queryRef = mClientRef.orderByChild("eEmail").equalTo(user.getEmail());
            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Toast.makeText(LoginActivity.this,"datasnapshat equals to"+dataSnapshot.getValue(),Toast.LENGTH_LONG);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            //TODO: create an other filter to make difference between users
                Intent intent;
            String email = user.getEmail().toString();
            if (email.contains("client")){
                intent = new Intent(LoginActivity.this,ClientProfileActivity.class);
            }
            else if(email.contains("superadmin")){
                intent = new Intent(LoginActivity.this,SuperAdminProfileActivity.class);
            }
            else{
                intent = new Intent(LoginActivity.this,AdminProfileActivity.class);
            }
            intent.putExtra("email",email);
            startActivity(intent);

        } else {

            Toast.makeText(LoginActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }



}

