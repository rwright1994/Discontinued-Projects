package com.example.payroll.Main.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payroll.Main.DataObjects.Payroll;
import com.example.payroll.Main.DataObjects.PayrollCard;
import com.example.payroll.R;

import java.util.ArrayList;

public class PayrollListAdapter extends RecyclerView.Adapter<PayrollListAdapter.PayrollListViewHolder> {


    private ArrayList<PayrollCard> mPayrollList;
    private OnItemClickListener mListener;

    //Define setOnItemClickListener
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    //Define interface and methods
    public interface OnItemClickListener{
        void onItemClick(int pos);
    }

    //Create Payroll List View Holder.
    public static class PayrollListViewHolder extends RecyclerView.ViewHolder {

        //Define required fields to render on the Recycler View's list Items.
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public TextView mTextView6;
        public TextView mTextView7;

        //Set text fields to corresponding text fields that are to be rendered.
        public PayrollListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.GrossTextView);
            mTextView2 = itemView.findViewById(R.id.CPPTextView);
            mTextView3 = itemView.findViewById(R.id.EITextView);
            mTextView4 = itemView.findViewById(R.id.FederalTextView);
            mTextView5 = itemView.findViewById(R.id.ProvincialTextView);
            mTextView6 = itemView.findViewById(R.id.NetTextView);
            mTextView7 = itemView.findViewById(R.id.payrollDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            listener.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }

    //Initialize Payroll Array List.
    public PayrollListAdapter(ArrayList<PayrollCard> payrollList){
        mPayrollList = payrollList;
    }

    @NonNull
    @Override
    public PayrollListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payroll_card, parent,false);
        PayrollListViewHolder rlvh = new PayrollListViewHolder(v, mListener);
        return rlvh;
    }

    //set text fields to each item's respective field.
    @Override
    public void onBindViewHolder(@NonNull PayrollListViewHolder holder, int position) {
        PayrollCard currentItem = mPayrollList.get(position);

        holder.mTextView1.setText("Gross: " + currentItem.getmGrossText());
        holder.mTextView2.setText("CPP:" + currentItem.getmCPPText());
        holder.mTextView3.setText("EI: " + currentItem.getmEIText());
        holder.mTextView4.setText("Federal: " + currentItem.getmFederal());
        holder.mTextView5.setText("Provincial: " + currentItem.getmProvincial());
        holder.mTextView6.setText("Net: " + currentItem.getmNetText());
        holder.mTextView7.setText("Date: " + currentItem.getmPayrollDateText());
    }

    //returns the amount of items in the list.
    @Override
    public int getItemCount() {
        return mPayrollList.size();
    }
}
