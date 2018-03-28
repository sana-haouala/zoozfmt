package tn.rnu.fmt.zoozfmt.Client;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tn.rnu.fmt.zoozfmt.R;

public class ClientPeriodActivity extends AppCompatActivity {

    private Intent intent;
    private int lengthPeriod = 1;
    private int lengthCycle = 28;
    private String key;
    private Event ev1;
    private Date dateEv1;
    private Date dateEv2;
    private Date dateEv3;

    //UI references
    CompactCalendarView compactCalendar;
    private TextView mTextPeriod;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(ClientPeriodActivity.this,ClientProfileActivity.class));
                    return true;
                case R.id.navigation_periode:
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
        setContentView(R.layout.activity_client_period);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(2).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mTextPeriod =(TextView) findViewById(R.id.periode_details);
/*
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);
*/

        /*lengthCycle = intent.getIntExtra("cycleLength",1);
        //lengthPeriod = intent.getIntExtra("periodLength",28);
        key = intent.getStringExtra("key");*/

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        //reading and writing calendar already granted
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                if ((ContextCompat.checkSelfPermission(ClientPeriodActivity.this,
                        Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED)
                        &&
                        (ContextCompat.checkSelfPermission(ClientPeriodActivity.this, Manifest.permission.WRITE_CALENDAR)
                                == PackageManager.PERMISSION_GRANTED)) {
                    final CharSequence[] items = {
                            getString(R.string.start_period_event), getString(R.string.end_period_event)
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(ClientPeriodActivity.this);
                    builder.setTitle(getString(R.string.create_event));
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            // Do something with the selection
                            mTextPeriod.setText( items[item]);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    dateEv1 = dateClicked;
                    ev1 = new Event(Color.WHITE, dateEv1.getTime() ,mTextPeriod.getText().toString());
                    //Toast.makeText(ClientPeriodActivity.this,ev1.getData().toString(),Toast.LENGTH_LONG).show();
                    compactCalendar.addEvent(ev1,true);

                } else {
                    //Request Calendar Permission
                    checkCalendarPermission();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
            }
        });
        //create events automatically
        if(!mTextPeriod.getText().toString().equals("")){
                if(mTextPeriod.getText().toString().equals(R.string.start_period_event)){
                    int noOfDays = lengthPeriod + 7; //i.e one week
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateEv1);
                    calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
                    Date dateEv2 = calendar.getTime();
                }
                else if (mTextPeriod.getText().toString().equals(R.string.end_period_event)){
                    int noOfDays = 7; //i.e one week
                    int noOfDays2 = lengthCycle - lengthPeriod;
                    Calendar calendar = Calendar.getInstance();
                    Calendar calendar2 = Calendar.getInstance();
                    calendar.setTime(dateEv1);
                    calendar2.setTime(dateEv2);
                    calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
                    calendar2.add(Calendar.DAY_OF_YEAR,noOfDays2);

                    dateEv2 = calendar.getTime();
                    dateEv3 = calendar2.getTime();
                }
                Event ev2 = new Event(Color.WHITE, dateEv2.getTime() ,getString(R.string.control_date));
                Event ev3 = new Event(Color.WHITE, dateEv3.getTime() ,getString(R.string.control_date));
                compactCalendar.addEvent(ev2,true);
                compactCalendar.addEvent(ev3,true);
        }

    }
    public static final int MY_PERMISSIONS_REQUEST_CALENDAR = 99;
    private void checkCalendarPermission() {
        if ((ContextCompat.checkSelfPermission(ClientPeriodActivity.this,
                Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
                ||
                (ContextCompat.checkSelfPermission(ClientPeriodActivity.this, Manifest.permission.WRITE_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED))  {
            // Should we show an explanation?
            if ((ActivityCompat.shouldShowRequestPermissionRationale(ClientPeriodActivity.this, Manifest.permission.WRITE_CALENDAR))
                    ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(ClientPeriodActivity.this, Manifest.permission.WRITE_CALENDAR)))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(ClientPeriodActivity.this)
                        .setTitle(getString(R.string.calendar_permission_title))
                        .setMessage(getString(R.string.calendar_permission_body))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(ClientPeriodActivity.this,
                                        new String[]{Manifest.permission.WRITE_CALENDAR,Manifest.permission.READ_CALENDAR},
                                        MY_PERMISSIONS_REQUEST_CALENDAR );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ClientPeriodActivity.this,
                        new String[]{Manifest.permission.WRITE_CALENDAR,Manifest.permission.READ_CALENDAR},
                        MY_PERMISSIONS_REQUEST_CALENDAR );
            }
        }
    }

    }

