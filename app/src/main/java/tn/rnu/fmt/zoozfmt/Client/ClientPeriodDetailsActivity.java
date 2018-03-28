package tn.rnu.fmt.zoozfmt.Client;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tn.rnu.fmt.zoozfmt.R;

public class ClientPeriodDetailsActivity extends AppCompatActivity {

    //UI References
    private ElegantNumberButton periodeLength;
    private ElegantNumberButton cycleLength;
    private Button valideBtn;

    private DatabaseReference mRootRef ;
    private DatabaseReference mChildRef;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_period_details);

        periodeLength = (ElegantNumberButton) findViewById(R.id.periode_length);
        cycleLength = (ElegantNumberButton) findViewById(R.id.cycle_length);

        valideBtn = (Button) findViewById(R.id.validate_periode_btn);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setIndeterminate(true);

        valideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         validate();
            }
        });

    }
    public void validate(){
        mProgressDialog.show();
        Intent intent1 = new Intent();
        String key = intent1.getStringExtra("key");
        mChildRef= mRootRef.child("Period");
        //DatabaseReference mChild = mChildRef.child(key);
        DatabaseReference mLengthPer = mChildRef.child("Length periode");
        String num1 = periodeLength.getNumber();
        mLengthPer.setValue(num1);
        DatabaseReference mLengthCy = mChildRef.child("Length Cycle");
        String num2 = cycleLength.getNumber();
        mLengthCy.setValue(num2);
        mProgressDialog.dismiss();
        Intent intent = new Intent(this,ClientPeriodActivity.class);
        intent.putExtra("key",key);
        intent.putExtra("periodLength",Integer.parseInt(num1));
        intent.putExtra("cycleLength",Integer.parseInt(num2));
        startActivity(intent);
    }
}
