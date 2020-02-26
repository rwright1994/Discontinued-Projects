package com.example.payroll.Main.DataObjects;

public class PayrollCard {

    private String mEIText, mCPPText, mGrossText, mNetText, mPayrollDateText;
    private String mFederalText, mProvincialText;
    private Payroll mPayroll;

    public PayrollCard(String grossText, String cppText, String eiText, String federalText,
                       String provincialText, String netText, String PayrollDateText){
        mGrossText = grossText;
        mCPPText = cppText;
        mEIText = eiText;
        mFederalText = federalText;
        mProvincialText = provincialText;
        mNetText = netText;
        mPayrollDateText = PayrollDateText;

    }

    public String getmCPPText() {
        return mCPPText;
    }

    public void setmCPPText(String mCPPText) {
        this.mCPPText = mCPPText;
    }

    public String getmGrossText() {
        return mGrossText;
    }

    public void setmGrossText(String mGrossText) {
        this.mGrossText = mGrossText;
    }

    public String getmNetText() {
        return mNetText;
    }

    public void setmNetText(String mNetText) {
        this.mNetText = mNetText;
    }

    public String getmEIText() {
        return mEIText;
    }

    public void setmEIText(String mEIText) {
        this.mEIText = mEIText;
    }

    public String getmFederal() {
        return mFederalText;
    }

    public void setmFederal(String mFederal) {
        this.mFederalText = mFederal;
    }

    public String getmProvincial() {
        return mProvincialText;
    }

    public void setmProvincial(String mProvincial) {
        this.mProvincialText = mProvincial;
    }

    public Payroll getmPayroll() {
        return mPayroll;
    }

    public void setmPayroll(Payroll mPayroll) {
        this.mPayroll = mPayroll;
    }


    public String getmPayrollDateText(){
        return mPayrollDateText;
    }

    public void setmPayrollDateText(){
        this.mPayrollDateText = mPayrollDateText;
    }







}
