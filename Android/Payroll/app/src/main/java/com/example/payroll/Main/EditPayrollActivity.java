package com.example.payroll.Main;

import android.os.Bundle;

import com.example.payroll.Main.Adapters.PayrollListAdapter;
import com.example.payroll.Main.DataObjects.Employee;
import com.example.payroll.Main.DataObjects.EmployeeCard;
import com.example.payroll.Main.DataObjects.Payroll;
import com.example.payroll.Main.DataObjects.PayrollCard;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.example.payroll.R;

import java.util.ArrayList;

public class EditPayrollActivity extends AppCompatActivity {

    private Employee mEmployee;
    private ArrayList<PayrollCard> mPayrollList;

    private RecyclerView mRecyclerView;
    private PayrollListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payroll_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEmployee = (Employee) getIntent().getSerializableExtra("Employee");

        createPayrollList();
        buildRecyclerView();
        buildPayrollCards();

        Toast.makeText(this, mEmployee.getPayroll().size() + " " + mEmployee.getPayroll().get(0).getBaseWage(), Toast.LENGTH_SHORT).show();
    }

    public void createPayrollList(){
        mPayrollList = new ArrayList<>();
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new PayrollListAdapter(mPayrollList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PayrollListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                mAdapter.notifyItemChanged(pos);
            }
        });
    }

    private void buildPayrollCards(){
            for(int i = 0; i < mEmployee.getPayroll().size(); i++){
                Payroll payrollToAdd = mEmployee.getPayroll().get(i);
                mPayrollList.add(new PayrollCard(payrollToAdd.getGross()+"",payrollToAdd.getCpp()+"",
                           payrollToAdd.getEI()+"",payrollToAdd.getFederal()+"",
                        payrollToAdd.getProvincial()+"", payrollToAdd.getNet()+"",
                                    payrollToAdd.getEndDate().toString()));
                mAdapter.notifyDataSetChanged();
            }


    }

}
