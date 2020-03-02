package com.example.payroll.Main.Adapters;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;

import static com.example.payroll.Main.Utils.MathUtil.round;

public class TaxDeductorAdapter {

    private String province;
    private int taxYear;
    private Sheet shFedTable, shCPPTable, shEITable, shClaimCodeTable, shProvTable;
    private AssetManager assetManager;

    public TaxDeductorAdapter(Context context,int taxYear, String province) throws IOException{
        assetManager = context.getAssets();
        this.taxYear = taxYear;
        this.province = province;
        initTables(province,taxYear);
    }

    private void initTables(String prov, int year) throws IOException{

        //initialize Tables that corresponds with the correct Province and year
        initClaimCodeTable(prov, year);
        initProvincialTaxTable(prov,year);

        initCPPTable(year);
        initEITable(year);
        initFederalTaxTable(year);

    }


    private void initClaimCodeTable(String prov, int year) throws IOException{
        try{
            InputStream mInputStream = assetManager.open("Tax Tables/"+year+"/Claim Codes - " + year + ".xls");
            POIFSFileSystem mFileSystem = new POIFSFileSystem(mInputStream);
            HSSFWorkbook mWorkBook = new HSSFWorkbook(mFileSystem);
            shClaimCodeTable = mWorkBook.getSheet(prov);
            mWorkBook.close();
            mFileSystem.close();
            mInputStream.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void initCPPTable(int year) throws IOException{
        try{
            InputStream mInputStream = assetManager.open("Tax Tables/"+ year +"/CPP Deduction Amounts - "+ year +".xls");
            POIFSFileSystem mFileSystem = new POIFSFileSystem(mInputStream);
            HSSFWorkbook mWorkBook = new HSSFWorkbook(mFileSystem);
            shCPPTable = mWorkBook.getSheetAt(0);
            mWorkBook.close();
            mFileSystem.close();
            mInputStream.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void initEITable(int year) throws IOException{
        try{
            InputStream mInputStream = assetManager.open("Tax Tables/" + year + "/EI Table - "+ year +".xls");
            POIFSFileSystem mFileSystem = new POIFSFileSystem(mInputStream);
            HSSFWorkbook mWorkBook = new HSSFWorkbook(mFileSystem);
            shEITable = mWorkBook.getSheetAt(0);
            mWorkBook.close();
            mFileSystem.close();
            mInputStream.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void initFederalTaxTable(int year) throws IOException{
        try{
            InputStream mInputStream = assetManager.open("Tax Tables/"+ year + "/Income Tax Brackets - "+year+".xls");
            POIFSFileSystem mFileSystem = new POIFSFileSystem(mInputStream);
            HSSFWorkbook mWorkBook = new HSSFWorkbook(mFileSystem);
            shFedTable = mWorkBook.getSheet("Federal");
            mWorkBook.close();
            mFileSystem.close();
            mInputStream.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void initProvincialTaxTable(String prov,int year) throws IOException{
        try{
            InputStream mInputStream = assetManager.open("Tax Tables/"+ year +"/Income Tax Brackets - " + year + ".xls");
            POIFSFileSystem mFileSystem = new POIFSFileSystem(mInputStream);
            HSSFWorkbook mWorkBook = new HSSFWorkbook(mFileSystem);
            shProvTable = mWorkBook.getSheet(prov);
            mWorkBook.close();
            mFileSystem.close();
            mInputStream.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public double deductCPP(int payPeriods, double pay) {

        double maxPenEarnings = shCPPTable.getRow(1).getCell(1).getNumericCellValue();
        double basExemptionAmt = round((shCPPTable.getRow(5).getCell(1).getNumericCellValue()),2);
        double contRate = round(shCPPTable.getRow(2).getCell(1).getNumericCellValue(),3);

        basExemptionAmt = basExemptionAmt / payPeriods;


        double res = (pay - basExemptionAmt) * contRate;


        if(res >= maxPenEarnings) {
            res = maxPenEarnings;
        }

        return round(res,2);
    }

    public double calcEI(double pay){

        double val = pay * 0.0162;

        return round(val,2);
    }

    //get federal claim code deduction constant.
    public double getFedClaimCodeAmount(int claimCode) throws IOException {
        return shClaimCodeTable.getRow(claimCode+1).getCell(4).getNumericCellValue();
    }



    public double calcK2(double rate, int payPeriods, double income) throws IOException {

        double res = 0;

        if(deductCPP(payPeriods, income) * payPeriods > getMaxCCPCont() && calcEI(income) * payPeriods > getMaxEICont() ) {
            res = ( (rate * getMaxCCPCont()) + (rate * getMaxEICont()));
        }else if(deductCPP(payPeriods, income) * payPeriods > getMaxCCPCont() && calcEI(income) * payPeriods < getMaxEICont() ) {
            res = ( (rate *  getMaxCCPCont()) + ( rate * (payPeriods* calcEI(income))));
        }else if(deductCPP(payPeriods, income) * payPeriods < getMaxCCPCont() && calcEI(income) * payPeriods > getMaxEICont()){
            res = ( (rate *  (deductCPP(payPeriods, income)*payPeriods)) + ( rate * getMaxEICont()));
        }else {
            res =  (round((rate *  (deductCPP(payPeriods, income)*payPeriods)), 3) + round(( rate * (payPeriods* calcEI(income))),3));
        }

        return round(res,3);
    }





    public double getMaxCCPCont() throws IOException {

        double res = 0;

        res = (shCPPTable.getRow(3).getCell(1).getNumericCellValue());

        return res;
    }

    public double getMaxEICont() throws IOException {

       return shEITable.getRow(3).getCell(1).getNumericCellValue();

    }

    //Calculate Federal income tax deductions.
    public double deductFedTax(double pay, int payPeriods, int claimCode) throws IOException {

        double res = 0;
        double baseRate = 0;
        double K = 0;
        double K1, K2,K3, K4;
        double salary = pay * payPeriods;

        try {

            for(int i = 1; i < shFedTable.getPhysicalNumberOfRows();i++){
                if(salary >= shFedTable.getRow(i).getCell(0).getNumericCellValue() && salary <= shFedTable.getRow(i).getCell(1).getNumericCellValue()){
                    baseRate = shFedTable.getRow(i).getCell(2).getNumericCellValue();
                    K = round(shFedTable.getRow(i).getCell(3).getNumericCellValue(),2);

                }
            }
            K1 = getFedClaimCodeAmount(claimCode);
            K2 = calcK2(baseRate, payPeriods, salary);
            K3 = 0;

            if( (baseRate * salary) >= (baseRate * 1222)) {
                K4 = round(baseRate * 1222,2);
            }else {
                K4 = baseRate * salary;
            }

            res = (baseRate * salary) - K - K1 - K2 - K3 - K4;
            res = round((res / payPeriods),3);

        }catch(FileNotFoundException e) {
            e.getStackTrace();

        }

        return res;

    }



    //Calculate British Columbia's provincial tax deductions.
    public double calcBCTax(int payPeriods, double salary, Sheet sh, double TCP) throws IOException {
        double res = 0;
        double rate = 0;
        double V1 = 0;
        double S = 0;
        double LCP = 0;
        double KP;
        double baseRate = sh.getRow(1).getCell(2).getNumericCellValue();


        if(salary <= sh.getRow(1).getCell(1).getNumericCellValue()) {
            rate = baseRate;
            KP = 0;
        }else if(salary >= sh.getRow(2).getCell(0).getNumericCellValue() && salary <= sh.getRow(2).getCell(1).getNumericCellValue()){
            rate = sh.getRow(2).getCell(2).getNumericCellValue();
            KP = sh.getRow(2).getCell(3).getNumericCellValue();
        }else if(salary >= sh.getRow(3).getCell(0).getNumericCellValue() && salary <= sh.getRow(3).getCell(1).getNumericCellValue()) {
            rate = sh.getRow(3).getCell(2).getNumericCellValue();
            KP = sh.getRow(3).getCell(3).getNumericCellValue();
        }else if(salary >= sh.getRow(4).getCell(0).getNumericCellValue() && salary <= sh.getRow(4).getCell(1).getNumericCellValue()) {
            rate = sh.getRow(4).getCell(2).getNumericCellValue();
            KP = sh.getRow(4).getCell(3).getNumericCellValue();
        }else if(salary >= sh.getRow(5).getCell(0).getNumericCellValue() && salary <= sh.getRow(5).getCell(1).getNumericCellValue()){
            rate = sh.getRow(5).getCell(2).getNumericCellValue();
            KP = sh.getRow(5).getCell(3).getNumericCellValue();
        }else {
            rate = sh.getRow(6).getCell(2).getNumericCellValue();
            KP = sh.getRow(6).getCell(3).getNumericCellValue();
        }

        double K1P = TCP;
        double K2P = calcK2(baseRate, payPeriods, salary);
        double K3P = 0;

        double T4 = (rate * salary) - KP - K1P - K2P - K3P;

        if(salary <= 20668) {
            if(T4 > 464) {
                S = 464;
            }else {
                S = T4;
            }
        }else if(salary > 20668 && salary <= 33702) {
            if(T4 > (464 -(salary - 20688) * 0.0356)) {
                S = 464 - (salary - 20688) * 0.0356;
            }else {
                S = T4;
            }
        }else {
            S = 0;
        }

        return round(res,2);
    }


    //Calculate New Brunswick provincial tax deductions.
    public double calcNBTax(int payPeriods, double salary, Sheet sh, double TCP)throws IOException {
        double res = 0;
        double rate = 0;
        double V1 = 0;
        double S = 0;
        double LCP = 0;
        double KP;
        double baseRate = sh.getRow(1).getCell(2).getNumericCellValue();


        if(salary <= sh.getRow(1).getCell(1).getNumericCellValue()) {
            rate = baseRate;
            KP = 0;
        }else if(salary >= sh.getRow(2).getCell(0).getNumericCellValue() && salary <= sh.getRow(2).getCell(1).getNumericCellValue()){
            rate = sh.getRow(2).getCell(2).getNumericCellValue();
            KP = sh.getRow(2).getCell(3).getNumericCellValue();
        }else if(salary >= sh.getRow(3).getCell(0).getNumericCellValue() && salary <= sh.getRow(3).getCell(1).getNumericCellValue()) {
            rate = sh.getRow(3).getCell(2).getNumericCellValue();
            KP = sh.getRow(3).getCell(3).getNumericCellValue();
        }else if(salary >= sh.getRow(4).getCell(0).getNumericCellValue() && salary <= sh.getRow(4).getCell(1).getNumericCellValue()) {
            rate = sh.getRow(4).getCell(2).getNumericCellValue();
            KP = sh.getRow(4).getCell(3).getNumericCellValue();
        }else {
            rate = sh.getRow(5).getCell(2).getNumericCellValue();
            KP = sh.getRow(5).getCell(3).getNumericCellValue();
        }


        double K1P = TCP;
        double K2P = calcK2(baseRate, payPeriods, salary);
        double K3P = 0;

        double T4 = (rate * salary) - KP - K1P - K2P - K3P;

        res = T4 / payPeriods;


        return round(res,2);
    }

    //Calculate Newfoundland & Labrador provincial tax deductions.
    public double calcNLTax(int payPeriods, double salary, Sheet sh, double TCP)throws IOException {
        double res = 0;

        double rate = 0;
        double V1 = 0;
        double S = 0;
        double LCP = 0;
        double KP;
        double baseRate = sh.getRow(1).getCell(2).getNumericCellValue();


        if(salary <= sh.getRow(1).getCell(1).getNumericCellValue()) {
            rate = baseRate;
            KP = 0;
        }else if(salary >= sh.getRow(2).getCell(0).getNumericCellValue() && salary <= sh.getRow(2).getCell(1).getNumericCellValue()){
            rate = sh.getRow(2).getCell(2).getNumericCellValue();
            KP = sh.getRow(2).getCell(3).getNumericCellValue();
        }else if(salary >= sh.getRow(3).getCell(0).getNumericCellValue() && salary <= sh.getRow(3).getCell(1).getNumericCellValue()) {
            rate = sh.getRow(3).getCell(2).getNumericCellValue();
            KP = sh.getRow(3).getCell(3).getNumericCellValue();
        }else if(salary >= sh.getRow(4).getCell(0).getNumericCellValue() && salary <= sh.getRow(4).getCell(1).getNumericCellValue()) {
            rate = sh.getRow(4).getCell(2).getNumericCellValue();
            KP = sh.getRow(4).getCell(3).getNumericCellValue();
        }else {
            rate = sh.getRow(5).getCell(2).getNumericCellValue();
            KP = sh.getRow(5).getCell(3).getNumericCellValue();
        }


        double K1P = TCP;
        double K2P = calcK2(baseRate, payPeriods, salary);
        double K3P = 0;

        double T4 = (rate * salary) - KP - K1P - K2P - K3P;

        res = T4 / payPeriods;


        return round(res,2);

    }


    public double calcNSTax(int payPeriods, double salary, Sheet sh, double TCP)throws IOException {
        double res = 0;
        double rate = 0;
        double V1 = 0;
        double S = 0;
        double LCP = 0;
        double KP =0;
        double baseRate = sh.getRow(1).getCell(2).getNumericCellValue();

        if(salary <= sh.getRow(1).getCell(1).getNumericCellValue()) {
            rate = baseRate;
            KP = 0;
        }else if(salary >= sh.getRow(2).getCell(0).getNumericCellValue() && salary <= sh.getRow(2).getCell(1).getNumericCellValue()){
            rate = sh.getRow(2).getCell(2).getNumericCellValue();
            KP = sh.getRow(2).getCell(3).getNumericCellValue();
        }else if(salary >= sh.getRow(3).getCell(0).getNumericCellValue() && salary <= sh.getRow(3).getCell(1).getNumericCellValue()) {
            rate = sh.getRow(3).getCell(2).getNumericCellValue();
            KP = sh.getRow(3).getCell(3).getNumericCellValue();
        }else if(salary >= sh.getRow(4).getCell(0).getNumericCellValue() && salary <= sh.getRow(4).getCell(1).getNumericCellValue()) {
            rate = sh.getRow(4).getCell(2).getNumericCellValue();
            KP = sh.getRow(4).getCell(3).getNumericCellValue();
        }else {
            rate = sh.getRow(5).getCell(2).getNumericCellValue();
            KP = sh.getRow(5).getCell(3).getNumericCellValue();
        }

        double K1P = TCP;
        double K2P = calcK2(baseRate, payPeriods, salary);
        double K3P = 0;

        double T4 = (rate * salary) - KP - K1P - K2P - K3P;

        res = T4 / payPeriods;


        return round(res,2);
    }

    public double calcONTax(int payPeriods, double salary, Sheet sh, double TCP)throws IOException {
        double res = 0;

        double rate = 0;
        double V1 = 0;
        double S = 0;
        double LCP = 0;
        double KP =0;
        double baseRate = sh.getRow(1).getCell(2).getNumericCellValue();

        if(salary <= sh.getRow(1).getCell(1).getNumericCellValue()) {
            rate = baseRate;
            KP = 0;
        }else if(salary >= sh.getRow(2).getCell(0).getNumericCellValue() && salary <= sh.getRow(2).getCell(1).getNumericCellValue()){
            rate = sh.getRow(2).getCell(2).getNumericCellValue();
            KP = sh.getRow(2).getCell(3).getNumericCellValue();
        }else if(salary >= sh.getRow(3).getCell(0).getNumericCellValue() && salary <= sh.getRow(3).getCell(1).getNumericCellValue()) {
            rate = sh.getRow(3).getCell(2).getNumericCellValue();
            KP = sh.getRow(3).getCell(3).getNumericCellValue();
        }else if(salary >= sh.getRow(4).getCell(0).getNumericCellValue() && salary <= sh.getRow(4).getCell(1).getNumericCellValue()) {
            rate = sh.getRow(4).getCell(2).getNumericCellValue();
            KP = sh.getRow(4).getCell(3).getNumericCellValue();
        }else {
            rate = sh.getRow(5).getCell(2).getNumericCellValue();
            KP = sh.getRow(5).getCell(3).getNumericCellValue();
        }

        double K1P = TCP;
        double K2P = calcK2(baseRate, payPeriods, salary);
        double K3P = 0;

        double T4 = (rate * salary) - KP - K1P - K2P - K3P;

        res = T4 / payPeriods;


        return round(res,2);
    }

    public double calcPETax(int payPeriods, double salary, Sheet sh, double TCP)throws IOException {
        double res = 0;
        double rate = 0;
        double V1 = 0;
        double S = 0;
        double LCP = 0;
        double KP =0;
        double baseRate = sh.getRow(1).getCell(2).getNumericCellValue();

        if(salary <= sh.getRow(1).getCell(1).getNumericCellValue()) {
            rate = baseRate;
        }else if(salary >= sh.getRow(2).getCell(0).getNumericCellValue() && salary <= sh.getRow(2).getCell(1).getNumericCellValue()){
            rate = sh.getRow(2).getCell(2).getNumericCellValue();
            KP = sh.getRow(2).getCell(3).getNumericCellValue();
        }else{
            rate = sh.getRow(3).getCell(2).getNumericCellValue();
            KP = sh.getRow(3).getCell(3).getNumericCellValue();
        }



        double K1P = TCP;
        double K2P = calcK2(baseRate, payPeriods, salary);
        double K3P = 0;

        double T4 = (rate * salary) - KP - K1P - K2P - K3P;

        res = T4 / payPeriods;


        return round(res,2);
    }

    public double calcSKTax(int payPeriods, double salary, Sheet sh, double TCP)throws IOException {
        double res = 0;
        double rate = 0;
        double V1 = 0;
        double S = 0;
        double LCP = 0;
        double KP =0;
        double baseRate = sh.getRow(1).getCell(2).getNumericCellValue();

        if(salary <= sh.getRow(1).getCell(1).getNumericCellValue()) {
            rate = baseRate;
        }else if(salary >= sh.getRow(2).getCell(0).getNumericCellValue() && salary <= sh.getRow(2).getCell(1).getNumericCellValue()){
            rate = sh.getRow(2).getCell(2).getNumericCellValue();
            KP = sh.getRow(2).getCell(3).getNumericCellValue();
        }else{
            rate = sh.getRow(3).getCell(2).getNumericCellValue();
            KP = sh.getRow(3).getCell(3).getNumericCellValue();
        }

        //LCP????

        double K1P = TCP;
        double K2P = calcK2(baseRate, payPeriods, salary);
        double K3P = 0;

        double T4 = (rate * salary) - KP - K1P - K2P - K3P;

        res = T4 / payPeriods;


        return round(res,2);
    }

    public double calcYTTax(int payPeriods, double salary, Sheet sh, double TCP)throws IOException {
        double res = 0;
        double rate = 0;
        double V1 = 0;
        double S = 0;
        double LCP = 0;
        double KP =0;
        double baseRate = sh.getRow(1).getCell(2).getNumericCellValue();

        if(salary <= sh.getRow(1).getCell(1).getNumericCellValue()) {
            rate = baseRate;
            KP = 0;
        }else if(salary >= sh.getRow(2).getCell(0).getNumericCellValue() && salary <= sh.getRow(2).getCell(1).getNumericCellValue()){
            rate = sh.getRow(2).getCell(2).getNumericCellValue();
            KP = sh.getRow(2).getCell(3).getNumericCellValue();
        }else if(salary >= sh.getRow(3).getCell(0).getNumericCellValue() && salary <= sh.getRow(3).getCell(1).getNumericCellValue()) {
            rate = sh.getRow(3).getCell(2).getNumericCellValue();
            KP = sh.getRow(3).getCell(3).getNumericCellValue();
        }else if(salary >= sh.getRow(4).getCell(0).getNumericCellValue() && salary <= sh.getRow(4).getCell(1).getNumericCellValue()) {
            rate = sh.getRow(4).getCell(2).getNumericCellValue();
            KP = sh.getRow(4).getCell(3).getNumericCellValue();
        }else {
            rate = sh.getRow(5).getCell(2).getNumericCellValue();
            KP = sh.getRow(5).getCell(3).getNumericCellValue();
        }

        double K1P = TCP;
        double K2P = calcK2(baseRate,payPeriods,salary);
        double K3P = 0;

        double T4 = (rate * salary) - KP - K1P - K2P - K3P;

        res = T4/payPeriods;

        return round(res,2);

    }


    //AB, MB, NT, NU use T4 with no exceptions;
    public double calcT4(int payPeriods, double pay, double TCP)throws IOException{
        double res = 0;
        double rate = 0;
        double KP =0;
        double baseRate = 0;

        double salary = round(pay*payPeriods,2);

        for(int i = 1; i < shProvTable.getPhysicalNumberOfRows(); i++) {
            if(salary >= shProvTable.getRow(i).getCell(0).getNumericCellValue() && salary <= shProvTable.getRow(i).getCell(1).getNumericCellValue()) {
                rate = shProvTable.getRow(i).getCell(2).getNumericCellValue();
                baseRate = shProvTable.getRow(1).getCell(2).getNumericCellValue();
                KP = shProvTable.getRow(i).getCell(3).getNumericCellValue();
                KP = round(KP,3);
                break;
            }
        }

        double K1P = TCP;
        double K2P = calcK2(baseRate,payPeriods,salary);
        double K3P = 0;

        double T4 = (rate * salary) - KP - K1P - K2P - K3P;

        res = T4/payPeriods;
        System.out.println(round(res,2));
        return round(res,2);
    }


    //getters & setters
    public String getProvince(){
        return this.province;
    }

    public void setProvince(String province){
        this.province = province;
    }

    public int getTaxYear(){
        return this.taxYear;
    }

    public void setTaxYear(int year){
        this.taxYear = year;
    }

}
