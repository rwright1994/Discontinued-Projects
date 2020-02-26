package com.example.payroll.Main.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payroll.Main.DataObjects.EmployeeCard;
import com.example.payroll.R;

import java.util.ArrayList;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.RecyclerListViewHolder> {

    private ArrayList<EmployeeCard> mEmployeeList;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int pos);
    }


    public static class RecyclerListViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;

        public RecyclerListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView1);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mTextView3 = itemView.findViewById(R.id.textView3);
            mTextView4 = itemView.findViewById(R.id.textView4);
            mTextView5 = itemView.findViewById(R.id.payAmount);

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

    public RecyclerListAdapter(ArrayList<EmployeeCard> employeeList){
        mEmployeeList = employeeList;
    }

    @NonNull
    @Override
    public RecyclerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_card, parent,false);
        RecyclerListViewHolder rlvh = new RecyclerListViewHolder(v, mListener);
        return rlvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerListViewHolder holder, int position) {
        EmployeeCard currentItem = mEmployeeList.get(position);

        holder.mImageView.setImageResource(currentItem.getmImageResource());
        holder.mTextView1.setText(currentItem.getmText1());
        holder.mTextView2.setText("Salary: " + currentItem.getmText2());
        holder.mTextView3.setText("Hourly: " + currentItem.getmText3());

        if(currentItem.getmText4() != null) {
            holder.mTextView4.setText("Last Payroll: " + currentItem.getmText4());
        }else{
            holder.mTextView4.setText("Last Payroll: " + "N/A");
        }

        if(currentItem.getmText5() != null){
            holder.mTextView5.setText(currentItem.getmText5());
        }else{
            holder.mTextView5.setText("0");
        }


    }

    @Override
    public int getItemCount() {
        return mEmployeeList.size();
    }


}
