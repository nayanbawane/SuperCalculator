package com.ammco.SuperCalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AgeCalculationActivity extends AppCompatActivity {

    // initializing variables
    private AppCompatButton btn_birth, btn_today, btn_calculate;
    private TextView tvResult;
    private DatePickerDialog.OnDateSetListener dateSetListener1, dateSetListener2;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_calculation);


        loadAdMobBanner();

        // assign variables
        initViews();

        // calendar format is imported to pick date
        Calendar calendar = Calendar.getInstance();

        // for year
        int year = calendar.get(Calendar.YEAR);

        // for month
        int month = calendar.get(Calendar.MONTH);

        // for date
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // to set the current date as by default
        String date = simpleDateFormat.format(Calendar.getInstance().getTime());
        btn_today.setText(date);

        // action to be performed when button 1 is clicked
        btn_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // date picker dialog is used
                // and its style and color are also passed
                DatePickerDialog datePickerDialog = new DatePickerDialog(AgeCalculationActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener1, year, month, day
                );
                // to set background for date-picker
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        // it is used to set the date which user selects
        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // here month+1 is used so that
                // actual month number can be displayed
                // otherwise it starts from 0 and it shows
                // 1 number less for every month
                // example- for january month=0
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                btn_birth.setText(date);
            }
        };

        // action to be performed when button 2 is clicked
        btn_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // date picker dialog is used
                // and its style and color are also passed
                DatePickerDialog datePickerDialog = new DatePickerDialog(AgeCalculationActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener2, year, month, day
                );
                // to set background for datepicker
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        // it is used to set the date which user selects
        dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // here month+1 is used so that
                // actual month number can be displayed
                // otherwise it starts from 0 and it shows
                // 1 number less for every month
                // example- for january month=0
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                btn_today.setText(date);
            }
        };

        // action to be performed when calculate button is clicked
        btn_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // converting the inputted date to string
                String sDate = btn_birth.getText().toString();
                String eDate = btn_today.getText().toString();
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    // converting it to date format
                    Date date1 = simpleDateFormat1.parse(sDate);
                    Date date2 = simpleDateFormat1.parse(eDate);

                    long startdate = date1.getTime();
                    long endDate = date2.getTime();

                    // condition
                    if (startdate <= endDate) {
                        org.joda.time.Period period = new Period(startdate, endDate, PeriodType.yearMonthDay());
                        int years = period.getYears();
                        int months = period.getMonths();
                        int days = period.getDays();

                        // show the final output
                        tvResult.setText(years + " Years || " + months + "Months || " + days + "Days");

                    } else {
                        // show message
                        Toast.makeText(AgeCalculationActivity.this, "BirthDate should not be larger than today's date!", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // banner ad
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void loadAdMobBanner() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }


    @SuppressLint("WrongViewCast")
    private void initViews() {
        btn_birth = findViewById(R.id.bt_birth);
        btn_today = findViewById(R.id.bt_today);
        btn_calculate = findViewById(R.id.btn_calculate);
        tvResult = findViewById(R.id.tv_result);
        mAdView = findViewById(R.id.adViewAgeCalculation);
    }


}