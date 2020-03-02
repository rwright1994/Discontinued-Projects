package com.example.payroll.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payroll.Main.DataObjects.Employee;
import com.example.payroll.R;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditEmployeeActivity extends AppCompatActivity {

    private EditText mFirstName, mMiddleName, mLastName, mDateOfBirth;
    private EditText mUnitNum, mStreetNum, mStreetName, mCity, mProvince, mPostalCode, mCountry;
    private TextInputLayout mTIL_FirstName, mTIL_MiddleName, mTIL_LastName;
    private TextInputLayout mTIL_UnitNum, mTIL_StreetNum, mTIL_StreetName, mTIL_City;
    private TextInputLayout mTIL_Province, mTIL_PostalCode, mTIL_Country;
    private CircleImageView mProfilePic;
    private Button btnSubmit, btnCancel;

    private Employee mEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        mEmployee = (Employee)getIntent().getSerializableExtra("Employee");

        mProfilePic = findViewById(R.id.ProfilePicture);
        mProfilePic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Pushed", Toast.LENGTH_SHORT).show();
                Intent imgIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(imgIntent,0);
            }
        });

        mFirstName = findViewById(R.id.FirstName);
        mMiddleName = findViewById(R.id.MiddleName);
        mLastName = findViewById(R.id.LastName);
        mDateOfBirth = findViewById(R.id.DateOfBirth);

        mTIL_FirstName = findViewById(R.id.til_FirstName);
        mTIL_MiddleName = findViewById(R.id.til_MiddleName);
        mTIL_LastName = findViewById(R.id.til_LastName);

        mUnitNum = findViewById(R.id.UnitNumber);
        mStreetNum = findViewById(R.id.StreetNumber);
        mStreetName = findViewById(R.id.StreetName);
        mCity = findViewById(R.id.City);
        mProvince = findViewById(R.id.Province);
        mPostalCode = findViewById(R.id.PostalCode);
        mCountry = findViewById(R.id.Country);

        mTIL_UnitNum = findViewById(R.id.til_UnitNumber);
        mTIL_StreetNum = findViewById(R.id.til_StreetNumber);
        mTIL_StreetName = findViewById(R.id.til_StreetName);
        mTIL_City = findViewById(R.id.til_City);
        mTIL_Province = findViewById(R.id.til_Province);
        mTIL_PostalCode = findViewById(R.id.til_PostalCode);
        mTIL_Country = findViewById(R.id.til_Country);

        btnSubmit = findViewById(R.id.SubmitBtn);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitChanges();
            }
        });

        btnCancel = findViewById(R.id.CancelBtn);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        populateFields();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode != Activity.RESULT_CANCELED) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mProfilePic.setImageBitmap(bitmap);
        }

    }

    private void populateFields(){
        mFirstName.setText(mEmployee.getFirstName(), TextView.BufferType.EDITABLE);
        mMiddleName.setText(mEmployee.getMiddleName(), TextView.BufferType.EDITABLE);
        mLastName.setText(mEmployee.getLastName(), TextView.BufferType.EDITABLE);

        mUnitNum.setText(mEmployee.getAddress().getUnitNumber(), TextView.BufferType.EDITABLE);
        mStreetNum.setText(mEmployee.getAddress().getStreetNumber(), TextView.BufferType.EDITABLE);
        mStreetName.setText(mEmployee.getAddress().getUnitNumber(), TextView.BufferType.EDITABLE);
        mCity.setText(mEmployee.getAddress().getCity(), TextView.BufferType.EDITABLE);
        mProvince.setText(mEmployee.getAddress().getProvince(), TextView.BufferType.EDITABLE);
        mPostalCode.setText(mEmployee.getAddress().getPostalCode(), TextView.BufferType.EDITABLE);
        mCountry.setText(mEmployee.getAddress().getCountry(), TextView.BufferType.EDITABLE);

    }

    private void submitChanges(){
        if(!validateFirstName() | !validateMiddleName() | !validateLastName() | !validateUnitNum()
                |!validateStreetNum() | !validateStreetName() |!validateCity() | !validateProvince() |
                !validatePostalCode() | !validateCountry()){
            return;
        }




    }

    public void requestFocus(View view){

        if(view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    private boolean validateFirstName(){
        if(mFirstName.getText().toString().trim().isEmpty()){
            mTIL_FirstName.setError("Enter Employee's first name.");
            requestFocus(mTIL_FirstName);
            return false;
        }else{
            if(mFirstName.getText().toString().trim().length() <3){
                mTIL_FirstName.setError("Minimum 3 characters");
                requestFocus(mTIL_FirstName);
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
                mTIL_MiddleName.setError("Minimum 3 characters");
                requestFocus(mTIL_MiddleName);
                return false;
            }
        }
        return true;
    }

    private boolean validateLastName(){
        if(mLastName.getText().toString().trim().isEmpty()) {
            mTIL_LastName.setError("Enter employee's last name");
            requestFocus(mTIL_LastName);
            return false;
        }
        return true;
    }

    private boolean validateUnitNum(){
        if(mUnitNum.getText().toString().trim().isEmpty()){
            mTIL_UnitNum.setError("Enter a Unit Number.");
            return false;
        }else{
            try {
                Integer.parseInt(mUnitNum.getText().toString().trim());
            }catch(NumberFormatException e){
                mTIL_UnitNum.setError("Enter only numbers.");
                requestFocus(mTIL_UnitNum);
                return false;
            }
        }

        return true;
    }

    private boolean validateStreetNum(){
        if(mStreetNum.getText().toString().trim().isEmpty()){
            mTIL_StreetNum.setError("Enter a Street Number.");
            return false;
        }else{
            try {
                Integer.parseInt(mStreetNum.getText().toString().trim());
            }catch(NumberFormatException e){
                mTIL_StreetNum.setError("Enter only numbers.");
                requestFocus(mTIL_StreetNum);
                return false;
            }
        }

        return true;
    }

    private boolean validateStreetName(){
        if(mStreetName.getText().toString().trim().isEmpty()){
            mTIL_StreetName.setError("Enter street name.");
            requestFocus(mTIL_StreetName);
            return false;
        }else{
            if(mStreetName.getText().toString().trim().length() <3){
                mTIL_StreetName.setError("Minimum 3 characters");
                requestFocus(mTIL_StreetName);
                return false;
            }
        }
        return true;
    }

    private boolean validateCity(){
        if(mCity.getText().toString().trim().isEmpty()){
            mTIL_City.setError("Enter the city name.");
            requestFocus(mTIL_City);
            return false;
        }else{
            if(mCity.getText().toString().trim().length() <3){
                mTIL_City.setError("Minimum 3 characters");
                requestFocus(mTIL_City);
                return false;
            }
        }
        return true;
    }

    private boolean validateProvince(){
        if(mProvince.getText().toString().trim().equals("Select Province...")){
            mTIL_Province.setError("Select a Province.");
            requestFocus(mTIL_Province);
            return false;
        }
        mTIL_Province.setError(null);
        return true;
    }

    private boolean validatePostalCode(){
        if(mPostalCode.getText().toString().trim().isEmpty()){
            mTIL_PostalCode.setError("Enter postal code.");
            requestFocus(mTIL_PostalCode);
            return false;
        }else{
            if(mPostalCode.getText().toString().trim().length() <3){
                mTIL_PostalCode.setError("Minimum 3 characters");
                requestFocus(mTIL_PostalCode);
                return false;
            }
        }
        mTIL_PostalCode.setError(null);
        return true;
    }

    private boolean validateCountry(){
        if(mCountry.getText().toString().trim().equals("Select Country...")){
            mTIL_Country.setError("Select a Country.");
            requestFocus(mTIL_Country);
            return false;
        }
        mTIL_Country.setError(null);
        return true;
    }
}
