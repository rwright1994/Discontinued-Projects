package com.example.payroll.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.payroll.Main.DataObjects.Company;
import com.example.payroll.Main.DataObjects.Address;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.payroll.R;
import com.google.android.material.textfield.TextInputLayout;

public class CreateCompanyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextInputLayout til_companyName, til_unitNum, til_streetNum, til_streetName, til_city;
    TextInputLayout til_province, til_postalCode, til_country;
    EditText companyName, unitNum, streetNum, streetName, city, postalCode;
    Spinner provinceSpinner, countrySpinner;

    private String strProvince, strCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_company);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        til_companyName = findViewById(R.id.til_companyName);
        til_unitNum = findViewById(R.id.til_unitNumber);
        til_streetNum = findViewById(R.id.til_streetNumber);
        til_streetName = findViewById(R.id.til_streetName);
        til_city = findViewById(R.id.til_city);
        til_province = findViewById(R.id.til_province);
        til_postalCode = findViewById(R.id.til_postalCode);
        til_country = findViewById(R.id.til_country);

        companyName = findViewById(R.id.Company_Name);
        unitNum = findViewById(R.id.Unit_Number);
        streetNum = findViewById(R.id.Street_Number);
        streetName = findViewById(R.id.Street_Name);
        city = findViewById(R.id.City);

        provinceSpinner = findViewById(R.id.Province);
        ArrayAdapter<CharSequence> provinceSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.provinces, android.R.layout.simple_spinner_item);
        provinceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(provinceSpinnerAdapter);
        provinceSpinner.setOnItemSelectedListener(this);

        postalCode = findViewById(R.id.PostalCode);

        countrySpinner = findViewById(R.id.Country);
        ArrayAdapter<CharSequence> countrySpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.countries, android.R.layout.simple_spinner_item);
        countrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countrySpinnerAdapter);
        countrySpinner.setOnItemSelectedListener(this);

        Button done = (Button) findViewById(R.id.DoneBtn);
        done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                submitForm();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    public void requestFocus(View view){

        if(view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }


    private boolean validateName(){
        if(companyName.getText().toString().trim().isEmpty()){
            til_companyName.setError("Enter Company Name.");
            requestFocus(til_companyName);
            return false;
        }else{
            if(companyName.getText().toString().trim().length() <3){
                til_companyName.setError("Minimum 3 characters");
                requestFocus(til_companyName);
                return false;
            }
        }
        return true;
    }

    private boolean validateUnitNum(){
        if(unitNum.getText().toString().trim().isEmpty()){
            til_unitNum.setError("Enter a Unit Number.");
            return false;
        }else{
            try {
                Integer.parseInt(unitNum.getText().toString().trim());
            }catch(NumberFormatException e){
                til_unitNum.setError("Enter only numbers.");
                requestFocus(til_unitNum);
                return false;
                }
        }

        return true;
    }

    private boolean validateStreetNum(){
        if(streetNum.getText().toString().trim().isEmpty()){
            til_streetNum.setError("Enter a Street Number.");
            return false;
        }else{
            try {
                Integer.parseInt(streetNum.getText().toString().trim());
            }catch(NumberFormatException e){
                til_streetNum.setError("Enter only numbers.");
                requestFocus(til_streetNum);
                return false;
            }
        }

        return true;
    }

    private boolean validateStreetName(){
        if(streetName.getText().toString().trim().isEmpty()){
            til_streetName.setError("Enter street name.");
            requestFocus(til_streetName);
            return false;
        }else{
            if(streetName.getText().toString().trim().length() <3){
                til_streetName.setError("Minimum 3 characters");
                requestFocus(til_streetName);
                return false;
            }
        }
        return true;
    }

    private boolean validateCity(){
        if(city.getText().toString().trim().isEmpty()){
            til_city.setError("Enter the city name.");
            requestFocus(til_city);
            return false;
        }else{
            if(city.getText().toString().trim().length() <3){
                til_city.setError("Minimum 3 characters");
                requestFocus(til_city);
                return false;
            }
        }
        return true;
    }

    private boolean validateProvince(){
        if(strProvince.trim().equals("Select Province...")){
            til_province.setError("Select a Province.");
            requestFocus(til_province);
            return false;
        }
        til_province.setError(null);
       return true;
    }

    private boolean validatePostalCode(){
        if(postalCode.getText().toString().trim().isEmpty()){
            til_postalCode.setError("Enter postal code.");
            requestFocus(til_postalCode);
            return false;
        }else{
            if(postalCode.getText().toString().trim().length() <3){
                til_postalCode.setError("Minimum 3 characters");
                requestFocus(til_postalCode);
                return false;
            }
        }
        til_postalCode.setError(null);
        return true;
    }

    private boolean validateCountry(){
        if(strCountry.trim().equals("Select Country...")){
            til_country.setError("Select a Country.");
            requestFocus(til_country);
            return false;
        }
        til_country.setError(null);
        return true;
    }




    private void submitForm(){
        if(!validateName() | !validateUnitNum() | !validateStreetNum()| !validateStreetName() |
                !validateCity() |!validateProvince() |!validatePostalCode() | !validateCountry()){
            return;
        }

        String compName = companyName.getText().toString();
        String unitNum = companyName.getText().toString();
        String streetNum = companyName.getText().toString();
        String streetName = companyName.getText().toString();
        String strCity = companyName.getText().toString();
        String strPostalCode = companyName.getText().toString();

        Company company = new Company(compName, new Address(unitNum,streetNum,streetName,strCity,strProvince,strPostalCode,strCountry));

        Intent intent = new Intent(CreateCompanyActivity.this, CompanyViewActivity.class);
        intent.putExtra("Company", company);
        startActivity(intent);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

       switch(parent.getId()){
           case R.id.Province :
               strProvince = parent.getItemAtPosition(position).toString();
           case R.id.Country :
               strCountry = parent.getItemAtPosition(position).toString();
       }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
