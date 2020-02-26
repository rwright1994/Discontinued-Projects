package com.example.payroll.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.payroll.Main.DataObjects.Address;
import com.example.payroll.Main.DataObjects.Employee;
import com.example.payroll.R;
import com.google.android.material.textfield.TextInputLayout;

public class CreateEmployeeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText mFirstName, mMiddleName,mLastName;
    private TextInputLayout til_firstName, til_middleName, til_lastName;
    private EditText mUnitNum, mStreetNum, mStreetName, mCity,mProvince,mPostalCode, mCountry;
    private TextInputLayout til_unitNum, til_streetNum, til_streetName, til_city, til_province, til_postalCode, til_country;
    private EditText mSIN, mSalary, mHourlyWage;
    private TextInputLayout til_SIN, til_salary, til_hourlyWage;


    private Button doneBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee);


        toolbar = findViewById(R.id.toolbar2);

        mFirstName = findViewById(R.id.FirstName);
        mMiddleName = findViewById(R.id.MiddleName);
        mLastName = findViewById(R.id.LastNameTxt);

        til_firstName = findViewById(R.id.til_firstName);
        til_middleName = findViewById(R.id.til_middleName);
        til_lastName = findViewById(R.id.til_lastName);

        mUnitNum = findViewById(R.id.unit_Number);
        mStreetNum = findViewById(R.id.street_Number);
        mStreetName = findViewById(R.id.street_Name);
        mCity = findViewById(R.id.city);
        mProvince = findViewById(R.id.province);
        mPostalCode = findViewById(R.id.postalCode);
        mCountry = findViewById(R.id.country);

        til_unitNum = findViewById(R.id.til_unitNumber);
        til_streetNum = findViewById(R.id.til_streetNumber);
        til_streetName = findViewById(R.id.til_streetName);
        til_city = findViewById(R.id.til_city);
        til_province = findViewById(R.id.til_province);
        til_postalCode = findViewById(R.id.til_postalCode);
        til_country = findViewById(R.id.til_country);

        mSIN = findViewById(R.id.Social_Insurance_Number);
        mSalary = findViewById(R.id.Salary);
        mHourlyWage = findViewById(R.id.Hourly_Wage);

        til_SIN = findViewById(R.id.til_socialInsuranceNumber);
        til_salary = findViewById(R.id.til_salary);
        til_hourlyWage = findViewById(R.id.til_hourlyWage);

        doneBtn = findViewById(R.id.DoneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                submitForm();

            }
        });
     }



    public void submitForm(){
            if(!validateFirstName() | !validateMiddleName() | !validateLastName() | !validateUnitNum()
            |!validateStreetNum() | !validateStreetName() |!validateCity() | !validateProvince() |
            !validatePostalCode() | !validateCountry()){
                return;
            }
            Intent returnIntent = new Intent();
            Employee obj = new Employee(mFirstName.getText().toString(),mLastName.getText().toString(),
                    new Address(mUnitNum.getText().toString(), mStreetNum.getText().toString(),
                            mStreetName.getText().toString(), mCity.getText().toString(),
                            mProvince.getText().toString(), mPostalCode.getText().toString()
                            ,mCountry.getText().toString()), Double.parseDouble(mSalary.getText().toString()),
                    Double.parseDouble(mHourlyWage.getText().toString()),Integer.parseInt(mSIN.getText().toString()));
            returnIntent.putExtra("Employee",obj);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();

        }


    public void requestFocus(View view){

        if(view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    private boolean validateFirstName(){
        if(mFirstName.getText().toString().trim().isEmpty()){
            til_firstName.setError("Enter Employee's first name.");
            requestFocus(til_firstName);
            return false;
        }else{
            if(mFirstName.getText().toString().trim().length() <3){
                til_firstName.setError("Minimum 3 characters");
                requestFocus(til_firstName);
                return false;
            }
        }
        return true;
    }

    private boolean validateMiddleName(){
            if(mMiddleName.getText().toString().trim().isEmpty()){
                return true;
            }else{
                if(mMiddleName.getText().toString().trim().length() <3){
                    til_middleName.setError("Minimum 3 characters");
                    requestFocus(til_middleName);
                    return false;
                }
            }
            return true;
    }

    private boolean validateLastName(){
        if(mLastName.getText().toString().trim().isEmpty()) {
            til_lastName.setError("Enter employee's last name");
            requestFocus(til_lastName);
            return false;
        }
        return true;
    }

    private boolean validateUnitNum(){
        if(mUnitNum.getText().toString().trim().isEmpty()){
            til_unitNum.setError("Enter a Unit Number.");
            return false;
        }else{
            try {
                Integer.parseInt(mUnitNum.getText().toString().trim());
            }catch(NumberFormatException e){
                til_unitNum.setError("Enter only numbers.");
                requestFocus(til_unitNum);
                return false;
            }
        }

        return true;
    }

    private boolean validateStreetNum(){
        if(mStreetNum.getText().toString().trim().isEmpty()){
            til_streetNum.setError("Enter a Street Number.");
            return false;
        }else{
            try {
                Integer.parseInt(mStreetNum.getText().toString().trim());
            }catch(NumberFormatException e){
                til_streetNum.setError("Enter only numbers.");
                requestFocus(til_streetNum);
                return false;
            }
        }

        return true;
    }

    private boolean validateStreetName(){
        if(mStreetName.getText().toString().trim().isEmpty()){
            til_streetName.setError("Enter street name.");
            requestFocus(til_streetName);
            return false;
        }else{
            if(mStreetName.getText().toString().trim().length() <3){
                til_streetName.setError("Minimum 3 characters");
                requestFocus(til_streetName);
                return false;
            }
        }
        return true;
    }

    private boolean validateCity(){
        if(mCity.getText().toString().trim().isEmpty()){
            til_city.setError("Enter the city name.");
            requestFocus(til_city);
            return false;
        }else{
            if(mCity.getText().toString().trim().length() <3){
                til_city.setError("Minimum 3 characters");
                requestFocus(til_city);
                return false;
            }
        }
        return true;
    }

    private boolean validateProvince(){
        if(mProvince.getText().toString().trim().equals("Select Province...")){
            til_province.setError("Select a Province.");
            requestFocus(til_province);
            return false;
        }
        til_province.setError(null);
        return true;
    }

    private boolean validatePostalCode(){
        if(mPostalCode.getText().toString().trim().isEmpty()){
            til_postalCode.setError("Enter postal code.");
            requestFocus(til_postalCode);
            return false;
        }else{
            if(mPostalCode.getText().toString().trim().length() <3){
                til_postalCode.setError("Minimum 3 characters");
                requestFocus(til_postalCode);
                return false;
            }
        }
        til_postalCode.setError(null);
        return true;
    }

    private boolean validateCountry(){
        if(mCountry.getText().toString().trim().equals("Select Country...")){
            til_country.setError("Select a Country.");
            requestFocus(til_country);
            return false;
        }
        til_country.setError(null);
        return true;
    }
}
