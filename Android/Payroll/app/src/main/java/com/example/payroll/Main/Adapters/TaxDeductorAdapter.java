package com.example.payroll.Main.Adapters;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import static com.example.payroll.Main.Utils.MathUtil.round;

public class TaxDeductorAdapter {


    private Sheet shFedTable, shCPPTable, shEITable;
    private HSSFWorkbook wbProvTables, wbClaimCodeTable;
    private AssetManager assetManager;

    public TaxDeductorAdapter(Context context) throws IOException{
        assetManager = context.getAssets();
        initTables();
    }

    private void initTables() throws IOException{
        initClaimCodeTable(2019);
        initCPPTable(2019);
        initEITable();
        initIncomeTaxTables();

    }

    private void initClaimCodeTable(int year) throws IOException{
        try{
            InputStream mInputStream = assetManager.open("Tax Tables/Claim Codes - 2019.xls");
            POIFSFileSystem mFileSystem = new POIFSFileSystem(mInputStream);
            HSSFWorkbook wbClaimCodeTable = new HSSFWorkbook(mFileSystem);
            mInputStream.close();

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void initCPPTable(int year) throws IOException{
        try{
            InputStream mInputStream = assetManager.open("Tax Tables/CCP Deduction Amounts - 2019.xls");
            POIFSFileSystem mFileSystem = new POIFSFileSystem(mInputStream);
            HSSFWorkbook mWorkBook = new HSSFWorkbook(mFileSystem);
            shCPPTable = mWorkBook.getSheetAt(0);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void initEITable() throws IOException{
        try{
            InputStream mInputStream = assetManager.open("Tax Tables/EI Table.xls");
            POIFSFileSystem mFileSystem = new POIFSFileSystem(mInputStream);
            HSSFWorkbook mWorkBook = new HSSFWorkbook(mFileSystem);
            shEITable = mWorkBook.getSheetAt(0);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void initIncomeTaxTables() throws IOException{
        try{
            InputStream mInputStream = assetManager.open("Tax Tables/Income Tax Brackets.xls");
            POIFSFileSystem mFileSystem = new POIFSFileSystem(mInputStream);
            HSSFWorkbook mWorkBook = new HSSFWorkbook(mFileSystem);
            wbProvTables = mWorkBook;
            shFedTable = mWorkBook.getSheet("Federal");
            mInputStream.close();
        }catch (FileNotFoundException e){
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

    public double DeductFederalTax(){


        return 0;
    }


    //get federal claim code deduction constant.
    public double getFedClaimCode(int claimCode) throws IOException {

        Sheet tempSh = wbClaimCodeTable.getSheetAt(0);

        return tempSh.getRow(claimCode+1).getCell(4).getNumericCellValue();

    }

    //get provincial claim code deduction constant.
    public double getProvClaimCode(String province, int claimCode) throws IOException {

        double res = 0;

            // Claim codes  for provincial taxes.
            Sheet tempSh = null;


            switch(province) {
                case "AB":
                    tempSh = wbClaimCodeTable.getSheet("Alberta Claim Codes");
                    break;
                case "BC":
                    tempSh = wbClaimCodeTable.getSheet("British Columbia Claim Codes");
                    break;
                case "MB":
                    tempSh = wbClaimCodeTable.getSheet("Manitoba Claim Codes");
                    break;
                case "NB":
                    tempSh = wbClaimCodeTable.getSheet("New Brunswick Claim Codes");
                    break;
                case "NL":
                    tempSh = wbClaimCodeTable.getSheet("Newfoundland and Labrador Claim Codes");
                    break;
                case "NT":
                    tempSh = wbClaimCodeTable.getSheet("Northwest Territories Claim Codes");
                    break;
                case "NU":
                    tempSh = wbClaimCodeTable.getSheet("Nunavut Claim Codes");
                    break;
                case "ON":
                    tempSh = wbClaimCodeTable.getSheet("Ontario Claim Codes");
                    break;
                case "PE":
                    tempSh = wbClaimCodeTable.getSheet("Prince Edward Island Claim Codes");
                    break;
                case "SK":
                    tempSh = wbClaimCodeTable.getSheet("Saskatchewan Claim Codes");
                    break;
                case "YT":
                    tempSh = wbClaimCodeTable.getSheet("Yukon Claim Codes");
                    break;
            }

            res = tempSh.getRow(claimCode+1).getCell(4).getNumericCellValue();

        return res;


    }

    public double deductProvTax(String province, int payPeriods, double salary, int claimCode) throws IOException {

        double res = 0;

        try {
            switch(province) {
                case "AB":
                    return calcT4(payPeriods, salary, wbProvTables.getSheet("Alberta"), getProvClaimCode("AB",claimCode));
                case "BC":
                    return calcT4(payPeriods, salary, wbProvTables.getSheet("British Columbia"), getProvClaimCode("BC", claimCode));
                case "MB":
                    return calcT4(payPeriods,salary,wbProvTables.getSheet("Manitoba"),getProvClaimCode("MB",claimCode));
                case "NB":
                    return calcT4(payPeriods,salary,wbProvTables.getSheet("New Brunswick"),getProvClaimCode("NB",claimCode));
                case "NL":
                    return calcT4(payPeriods,salary,wbProvTables.getSheet("Newfoundland and Labrador"),getProvClaimCode("NL",claimCode));
                //	case "NS":
                //		  sh = wb.getSheet("Nova Scotia");
                //		  res = calcT4(payPeriods, salary, sh, getProvClaimCode("NS",claimCode));
                case "NT":
                    return calcT4(payPeriods,salary,wbProvTables.getSheet("Northwest Territories"),getProvClaimCode("NT",claimCode));
                case "NU":
                    return calcT4(payPeriods,salary,wbProvTables.getSheet("Nunavut"),getProvClaimCode("NU",claimCode));
                case "ON":
                    return calcT4(payPeriods,salary,wbProvTables.getSheet("Ontario"),getProvClaimCode("ON",claimCode));
                case "PE":
                    return calcT4(payPeriods,salary,wbProvTables.getSheet("Prince Edward Island"),getProvClaimCode("PE",claimCode));
                case "SK":
                    return calcT4(payPeriods,salary,wbProvTables.getSheet("Saskatchewan"),getProvClaimCode("SK",claimCode));
                case "YT":
                    return calcMBTax(payPeriods,salary,wbProvTables.getSheet("Yukon"),getProvClaimCode("YT",claimCode));
            }


        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }


        return 0;
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
        double baseRate, rate, K, K1, K2,K3, K4 = 0;

        double salary = pay * payPeriods;


        try {



            baseRate = shFedTable.getRow(1).getCell(2).getNumericCellValue();

            if(salary <= shFedTable.getRow(1).getCell(1).getNumericCellValue()) {
                K = shFedTable.getRow(1).getCell(3).getNumericCellValue();
                rate = baseRate;
            }else if(salary > shFedTable.getRow(1).getCell(1).getNumericCellValue() && salary <=  shFedTable.getRow(2).getCell(1).getNumericCellValue()){
                K = shFedTable.getRow(2).getCell(3).getNumericCellValue();
                rate = shFedTable.getRow(2).getCell(2).getNumericCellValue();
            }else if(salary > shFedTable.getRow(2).getCell(1).getNumericCellValue() && salary <= shFedTable.getRow(3).getCell(1).getNumericCellValue()){
                K = shFedTable.getRow(3).getCell(3).getNumericCellValue();
                rate = shFedTable.getRow(3).getCell(2).getNumericCellValue();
            }else if(salary > shFedTable.getRow(3).getCell(1).getNumericCellValue() && salary <= shFedTable.getRow(4).getCell(1).getNumericCellValue()){
                K = shFedTable.getRow(4).getCell(3).getNumericCellValue();
                rate = shFedTable.getRow(4).getCell(2).getNumericCellValue();
            }else {
                K = shFedTable.getRow(5).getCell(3).getNumericCellValue();
                rate = shFedTable.getRow(5).getCell(2).getNumericCellValue();
            }

            K1 = getFedClaimCode(claimCode);
            K2 = calcK2(baseRate, payPeriods, salary);
            K3 = 0;

            if( (baseRate * salary) >= (baseRate * 1222)) {
                K4 = round(baseRate * 1222,2);
            }else {
                K4 = baseRate * salary;
            }

            res = (rate * salary) - K - K1 - K2 - K3 - K4;
            res = round((res / payPeriods),3);

        }catch(FileNotFoundException e) {
            e.getStackTrace();

        }

        return res;

    }


    //Provincial tax deduction methods.

    //Calculate Alberta's Provincial Tax deductions.
    public double calcABTax(int payPeriods, double salary, Sheet sh, double TCP) throws IOException {
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
        //double T2 = T4 + V1 - S - LCP;

        res = T4 / payPeriods;


        return round(res,2);
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

    //Calculate Manatoba's provincial tax deductions.
    public double calcMBTax(int payPeriods, double salary, Sheet sh, double TCP) throws IOException {
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
        }else {
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

    public double calcNTTax(int payPeriods, double salary, Sheet sh, double TCP) throws IOException {
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
        }else if(salary >= sh.getRow(3).getCell(0).getNumericCellValue() && salary <= sh.getRow(3).getCell(1).getNumericCellValue()){
            rate = sh.getRow(3).getCell(2).getNumericCellValue();
            KP = sh.getRow(3).getCell(3).getNumericCellValue();
        }else {
            rate = sh.getRow(4).getCell(2).getNumericCellValue();
            KP = sh.getRow(3).getCell(3).getNumericCellValue();
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

    public double calcNUTax(int payPeriods, double salary, Sheet sh, double TCP)throws IOException {
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
        }else if(salary >= sh.getRow(3).getCell(0).getNumericCellValue() && salary <= sh.getRow(3).getCell(1).getNumericCellValue()){
            rate = sh.getRow(3).getCell(2).getNumericCellValue();
            KP = sh.getRow(3).getCell(3).getNumericCellValue();
        }else {
            rate = sh.getRow(4).getCell(2).getNumericCellValue();
            KP = sh.getRow(3).getCell(3).getNumericCellValue();
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

    public double calcT4(int payPeriods, double salary, Sheet sh, double TCP)throws IOException{
        double res = 0;
        double rate = 0;
        double V1 = 0;
        double S = 0;
        double LCP = 0;
        double KP =0;
        double baseRate = 0;


        for(int i = 1; i < sh.getPhysicalNumberOfRows(); i++) {
            if(salary >= sh.getRow(i).getCell(0).getNumericCellValue() && salary <= sh.getRow(i).getCell(1).getNumericCellValue()) {
                rate = sh.getRow(i).getCell(2).getNumericCellValue();
                baseRate = sh.getRow(1).getCell(2).getNumericCellValue();
                KP = sh.getRow(i).getCell(3).getNumericCellValue();
                KP = round(KP,3);
                break;
            }
        }


        System.out.println("rate:" + rate + " base Rate:" + baseRate + " KP:" + KP);

        double K1P = TCP;
        double K2P = calcK2(baseRate,payPeriods,salary);
        double K3P = 0;

        double T4 = (rate * salary) - KP - K1P - K2P - K3P;

        res = T4/payPeriods;
        System.out.println(round(res,2));
        return round(res,2);
    }
}
