package tn.rnu.fmt.zoozfmt.Client;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import tn.rnu.fmt.zoozfmt.LoginActivity;
import tn.rnu.fmt.zoozfmt.R;

public class SignUpClientActivity extends AppCompatActivity {

    private static final String TAG = "SignUpClientActivity";

    private String key = "";

    //UI references
    private TextInputLayout mFirstname;
    private TextInputLayout mLastname;
    private TextInputLayout mPassword;
    private TextInputLayout mEmail;
    private TextInputLayout mNbChildren;
    private EditText mBirthday;
    private CheckBox mMenopause;
    private Button signupBtn;
    private Button loginBtn;
    //values
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private int nbChildren;
    private String birth;
    private Boolean menopause;

    private Calendar myCalendar = Calendar.getInstance();

    // Progress Dialog Object
    private ProgressDialog prgDialog;
    //firebase ref
    private DatabaseReference mRootDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_client);

        mFirstname = (TextInputLayout) findViewById(R.id.firstname_reg);
        mLastname = (TextInputLayout) findViewById(R.id.lastname_reg);
        mPassword = (TextInputLayout) findViewById(R.id.pass_reg);
        mEmail = (TextInputLayout) findViewById(R.id.email_reg);
        mNbChildren = (TextInputLayout) findViewById(R.id.nbChildren_reg);
        mMenopause = (CheckBox) findViewById(R.id.checkMeno);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        //Button
        signupBtn =(Button) findViewById(R.id.btn_reg);
        loginBtn = (Button) findViewById(R.id.btnLogin);
        // Instantiate Progress Dialog object
       prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage(getString(R.string.wait_msg));
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        //show datePicker to set the birthday

        mBirthday = (EditText) findViewById(R.id.age_reg);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        mBirthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignUpClientActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    //datepicker update
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mBirthday.setText(sdf.format(myCalendar.getTime()));
    }

    public boolean validateForm(){
        //Validation for Blank Field
       if((!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail.getEditText().getText().toString()).matches())||(mEmail.getEditText().getText().toString().trim().equals("")))
        {
//Validation for Invalid Email Address
            Toast.makeText(getApplicationContext(), "Please enter a valid Email to receive notification", Toast.LENGTH_LONG).show();
            mEmail.setError(getString(R.string.invalid_field));
            return false;
        }

        else if(mBirthday.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "Birthday cannot be Blank", Toast.LENGTH_LONG).show();
            mBirthday.setError(getString(R.string.required_field));
            return false;
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Validated Succesfully", Toast.LENGTH_LONG).show();
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
        signupBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(validateForm()){

                    createAccount(mEmail.getEditText().getText().toString(), mPassword.getEditText().getText().toString());
                    //values
                    firstName = mFirstname.getEditText().getText().toString();
                    lastName= mLastname.getEditText().getText().toString();
                    password = mPassword.getEditText().getText().toString();
                    email = mEmail.getEditText().getText().toString();
                    nbChildren = Integer.parseInt(mNbChildren.getEditText().getText().toString());
                    birth = mBirthday.getText().toString();
                    menopause = mMenopause.isChecked();

                    DatabaseReference myRefClient = mRootDatabase.child("Clients");
                    key = myRefClient.push().getKey();
                    myRefClient.child(key).setValue(new Client(firstName,lastName,password,email,nbChildren,birth,menopause));

                }
                else
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_field), Toast.LENGTH_LONG).show();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SignUpClientActivity.this,LoginActivity.class));
            }
        });
}
    // [END on_start_check_user]


    private void createAccount(String email, String password) {
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
                            Toast.makeText(SignUpClientActivity.this, "Authentication failed.",
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
        //baseActivity.hideProgressDialog();
        if (user != null) {

            String email = user.getEmail().toString();
            Intent intent = new Intent(SignUpClientActivity.this,ClientProfileActivity.class);
            intent.putExtra("email",email);
            intent.putExtra("key",key);
            startActivity(intent);

        } else {

            Toast.makeText(SignUpClientActivity.this, R.string.sign_up_error,
                    Toast.LENGTH_SHORT).show();
        }
    }


}
