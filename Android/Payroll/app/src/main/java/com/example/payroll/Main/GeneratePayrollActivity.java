package com.example.payroll.Main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.payroll.Main.Adapters.TaxDeductorAdapter;
import com.example.payroll.Main.DataObjects.Employee;
import com.example.payroll.Main.DataObjects.Payroll;
import com.example.payroll.Main.Fragments.DatePickerFragment;
import com.example.payroll.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.payroll.Main.Utils.MathUtil.round;

public class GeneratePayrollActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button doneBtn, cancelBtn;
    private EditText fromDate, toDate, hoursWorked;
    private TaxDeductorAdapter mTaxDeductorAdapter;
    private Spinner payPeriodSpinner, claimCodeSpinner;

    private Date mDate1, mDate2;
    private Employee employee;
    private Payroll payrollObj;

    private int payPeriods, claimCode;
    private double hours;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_payroll);

        final int position = getIntent().getIntExtra("Position",0);

        //Get employee Object from previous activity.
        employee = (Employee)getIntent().getSerializableExtra("Employee");

        //Initialize Tax deducter.
        try {
            mTaxDeductorAdapter = new TaxDeductorAdapter(this,2019, "New Brunswick");
        } catch (IOException e) {
            e.printStackTrace();
        }

        payPeriodSpinner = findViewById(R.id.PayPeriods);
        ArrayAdapter<CharSequence> payPeriodsSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.payPeriods, android.R.layout.simple_spinner_item);
        payPeriodsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payPeriodSpinner.setAdapter(payPeriodsSpinnerAdapter);
        payPeriodSpinner.setOnItemSelectedListener(this);

        claimCodeSpinner = findViewById(R.id.ClaimCodes);
        ArrayAdapter<CharSequence>  claimCodeSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.claimCodes, android.R.layout.simple_spinner_item);
        claimCodeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        claimCodeSpinner.setAdapter(claimCodeSpinnerAdapter);
        claimCodeSpinner.setOnItemSelectedListener(this);

        hoursWorked = findViewById(R.id.HoursWorked);
        fromDate = findViewById(R.id.FromDate);
        fromDate.setShowSoftInputOnFocus(false);
        fromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View v, boolean hasFocus) {
               if(hasFocus){
                   DialogFragment newFragment = new DatePickerFragment(fromDate);
                   newFragment.show(getSupportFragmentManager(),"datePicker");
               }
           }
       });

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment(fromDate);
                newFragment.show(getSupportFragmentManager(),"datePicker");
            }
        });


        toDate = findViewById(R.id.ToDate);
        toDate.setShowSoftInputOnFocus(false);
        toDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DialogFragment newFragment = new DatePickerFragment(toDate);
                    newFragment.show(getSupportFragmentManager(),"datePicker");
                }
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment(toDate);
                newFragment.show(getSupportFragmentManager(),"datePicker");
            }
        });

        hoursWorked = findViewById(R.id.HoursWorked);

        //Submit form data to run Payroll
        doneBtn = findViewById(R.id.Done_Btn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();

                DateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
                try {
                    mDate1 = formatter.parse(fromDate.getText().toString());
                    mDate2 = formatter.parse(toDate.getText().toString());
                    hours =  Double.parseDouble(hoursWorked.getText().toString());
                    claimCode = Integer.parseInt(claimCodeSpinner.getSelectedItem().toString());
                }catch (ParseException e){
                    e.getStackTrace();
                }
                try {
                   double gross = round((employee.getHourlyWage() * hours),2);
                   double cpp = mTaxDeductorAdapter.deductCPP(payPeriods, gross);
                   double ei = mTaxDeductorAdapter.calcEI(gross);
                   double federal = mTaxDeductorAdapter.deductFedTax(gross, payPeriods,claimCode);
                   double provincial = mTaxDeductorAdapter.calcT4(payPeriods,gross,claimCode);
                   double net = round(gross - cpp - ei,2);
                    payrollObj = new Payroll(gross,net,ei,cpp,federal, provincial, mDate1,mDate2);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                returnIntent.putExtra("Payroll", payrollObj);
                returnIntent.putExtra("Position", position);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        cancelBtn = findViewById(R.id.Cancel_Btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneratePayrollActivity.this.finish();
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        switch (parent.getId()){
            case R.id.PayPeriods:
                switch(text){
                    case "Daily":
                        payPeriods = 365;
                        break;
                    case "Weekly":
                        payPeriods = 52;
                        break;
                    case "Bi-Weekly":
                        payPeriods = 26;
                        break;
                    case "Semi-Monthly":
                        payPeriods = 24;
                        break;
                    case "Monthly":
                        payPeriods = 12;
                        break;
                }
                break;
            case R.id.ClaimCodes:
                switch(text){
                    case "0":
                        claimCode = 0;
                        break;
                    case "1":
                        claimCode = 1;
                        break;
                    case "2":
                        claimCode = 2;
                        break;
                    case "3":
                        claimCode = 3;
                        break;
                    case "4":
                        claimCode = 4;
                        break;
                    case "5":
                        claimCode = 5;
                        break;
                    case "6":
                        claimCode = 6;
                        break;
                    case "7":
                        claimCode = 7;
                        break;
                    case "8":
                        claimCode = 8;
                        break;
                    case "9":
                        claimCode = 9;
                        break;
                    case "10":
                        claimCode = 10;
                        break;
                }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
