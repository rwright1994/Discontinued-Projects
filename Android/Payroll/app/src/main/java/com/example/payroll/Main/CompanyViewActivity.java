package com.example.payroll.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payroll.Main.Adapters.RecyclerListAdapter;
import com.example.payroll.Main.DataObjects.Company;
import com.example.payroll.Main.DataObjects.Employee;
import com.example.payroll.Main.DataObjects.EmployeeCard;
import com.example.payroll.Main.DataObjects.Payroll;
import com.example.payroll.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CompanyViewActivity extends AppCompatActivity {

    static final int ADD_EMPLOYEE_REQUEST = 1;
    static final int EDIT_EMPLOYEE_REQUEST= 2;
    static final int ADD_PAYROLL_REQUEST = 3;
    static final int EDIT_PAYROLL_REQUEST = 4;

    private ArrayList<EmployeeCard> exampleList;
    private Company company;

    private FloatingActionButton addButton;
    private PopupWindow popUp;

    private RecyclerView mRecyclerView;
    private RecyclerListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        company = (Company) getIntent().getSerializableExtra("Company");

        setContentView(R.layout.activity_company_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Payroll - " + company.getName());
        setSupportActionBar(toolbar);

        addButton = findViewById(R.id.addEmployeeFAB);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmployee();
            }
        });

         createEmployeeList();
         buildRecyclerView();

    }

    public void createEmployeeList(){
        exampleList = new ArrayList<>();


    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerListAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyclerListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                showPopupWindow(pos);
                mAdapter.notifyItemChanged(pos);
            }
        });
    }

    public void showPopupWindow(final int pos){

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popUpView = inflater.inflate(R.layout.employee_popup_window, null,false);
        popUp = new PopupWindow(popUpView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popUp.setContentView(popUpView);
        popUp.showAtLocation(findViewById(R.id.layoutTemp), Gravity.CENTER, 0,0);

        Button genPayrollBtn = popUpView.findViewById(R.id.generatePayrollBtn);
        genPayrollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPayrollIntent = new Intent(v.getContext(), GeneratePayrollActivity.class);
                addPayrollIntent.putExtra("Company",company);
                addPayrollIntent.putExtra("Employee", exampleList.get(pos).getEmployee());
                addPayrollIntent.putExtra("Position",pos);
                startActivityForResult(addPayrollIntent, ADD_PAYROLL_REQUEST);
                mAdapter.notifyItemChanged(pos);

            }
        });

        Button popUpEditPayrollBtn = popUpView.findViewById(R.id.editPayrollBtn);
        popUpEditPayrollBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(exampleList.get(pos).getEmployee().getPayroll().size() !=0) {
                    Intent editPayrollIntent = new Intent(v.getContext(), EditPayrollActivity.class);
                    editPayrollIntent.putExtra("Employee", exampleList.get(pos).getEmployee());
                    startActivityForResult(editPayrollIntent, EDIT_PAYROLL_REQUEST);
                    mAdapter.notifyItemChanged(pos);
                }else{
                    Toast.makeText(CompanyViewActivity.this, "Not payrolls entered for "+ exampleList.get(pos).getEmployee().getFirstName(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button popUpEditEmpBtn = popUpView.findViewById(R.id.popUpEditBtn);
        popUpEditEmpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent editEmployeeIntent = new Intent(v.getContext(),EditEmployeeActivity.class);
                editEmployeeIntent.putExtra("Employee", exampleList.get(pos).getEmployee());
                startActivityForResult(editEmployeeIntent, EDIT_EMPLOYEE_REQUEST);
                mAdapter.notifyItemChanged(pos);
            }
        });

        Button cancelBtn = popUpView.findViewById(R.id.popUpCancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });


    }

    public void addEmployee(){
        Intent addEmpIntent = new Intent(this, CreateEmployeeActivity.class);
        startActivityForResult(addEmpIntent, ADD_EMPLOYEE_REQUEST);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.company_view_overflow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_details:
                //transaction to company details.
                return true;

            case R.id.action_settings:
                //Open settings.
                return true;

            case R.id.action_exit:
                //Close application
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_EMPLOYEE_REQUEST){
            if(resultCode == Activity.RESULT_OK) {
                Employee employee = (Employee) data.getSerializableExtra("Employee");
                EmployeeCard employeeCard = new EmployeeCard(R.drawable.ic_person_black_24dp, employee.getFirstName() + " " + employee.getLastName(), Double.toString(employee.getSalary()), Double.toString(employee.getHourlyWage()), null, null);
                employeeCard.setEmployee(employee);
                company.addEmployee(employee);
                exampleList.add(employeeCard);
                mAdapter.notifyDataSetChanged();
            }
        }
        if(requestCode == ADD_PAYROLL_REQUEST){
                Payroll payroll = (Payroll) data.getSerializableExtra("Payroll");
                int pos = data.getIntExtra("Position",0);
                exampleList.get(pos).getEmployee().addPayroll(payroll);
                exampleList.get(pos).setmText4(exampleList.get(pos).getEmployee().getLastPayrollDate());
                exampleList.get(pos).setmText5(Double.toString(exampleList.get(pos).getEmployee().getLastPayroll().getGross()));
                System.out.println("OUTPUT: Payroll start date = " + exampleList.get(pos).getEmployee().getPayroll().get(0).getStartDate());
                mAdapter.notifyDataSetChanged();
        }
        if(requestCode == EDIT_PAYROLL_REQUEST){
            if(resultCode == RESULT_OK){

            }
        }
        if(requestCode == EDIT_EMPLOYEE_REQUEST){
            if(resultCode == RESULT_OK) {
                Employee rtnEmployee = (Employee) data.getSerializableExtra("Employee");
            }
        }

            if(resultCode == Activity.RESULT_CANCELED){
                // do something
            }
        }
    }


