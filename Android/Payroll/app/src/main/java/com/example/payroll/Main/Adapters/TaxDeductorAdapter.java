package com.example.payroll.Main.Adapters;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import static com.example.payroll.Main.Utils.MathUtil.round;

public class TaxDeductorAdapter {


    private Sheet sh;
    private AssetManager assetManager;

    public TaxDeductorAdapter(Context context){
        assetManager = context.getAssets();
    }

    public double deductCPP(int payPeriods, double pay) throws IOException {

        double res = 0;
        double maxPenEarnings = 0;
        double bscExcemptAmt = 0;
        double contRate = 0;

        try {

            InputStream  myInputStream = assetManager.open("Tax Tables/CPP Deduction Amounts - 2019.xls");

            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInputStream);
            HSSFWorkbook wb = new HSSFWorkbook(myFileSystem);
            sh = wb.getSheetAt(0);

            maxPenEarnings = sh.getRow(1).getCell(1).getNumericCellValue();
            bscExcemptAmt = round((sh.getRow(5).getCell(1).getNumericCellValue()),2);
            contRate = round(sh.getRow(2).getCell(1).getNumericCellValue(),3);

            //Close file system and Input Stream.
            myFileSystem.close();
            myInputStream.close();

            bscExcemptAmt = bscExcemptAmt / payPeriods;


            res = (pay - bscExcemptAmt) * contRate;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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

        double res = 0;

        InputStream fis = assetManager.open("Tax Tables/Claim Codes - 2019.xls");
        POIFSFileSystem myFileSystem = new POIFSFileSystem(fis);
        Workbook wb = new HSSFWorkbook(myFileSystem);

        sh = wb.getSheetAt(0);
        res = sh.getRow(claimCode+1).getCell(4).getNumericCellValue();

        myFileSystem.close();
        fis.close();

        return res;

    }

    //get provincial claim code deduction constant.
    public double getProvClaimCode(String province, int claimCode) throws IOException {

        double res = 0;

        try {


            // Claim codes  for provincial taxes.
            InputStream fis = assetManager.open("Tax Tables/Claim Codes - 2019.xls");
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fis);
            Workbook wb = new HSSFWorkbook(poifsFileSystem);


            switch(province) {
                case "AB":
                    sh = wb.getSheet("Alberta Claim Codes");
                    break;
                case "BC":
                    sh = wb.getSheet("British Columbia Claim Codes");
                    break;
                case "MB":
                    sh = wb.getSheet("Manitoba Claim Codes");
                    break;
                case "NB":
                    sh = wb.getSheet("New Brunswick Claim Codes");
                    break;
                case "NL":
                    sh = wb.getSheet("Newfoundland and Labrador Claim Codes");
                    break;
                case "NT":
                    sh = wb.getSheet("Northwest Territories Claim Codes");
                    break;
                case "NU":
                    sh = wb.getSheet("Nunavut Claim Codes");
                    break;
                case "ON":
                    sh = wb.getSheet("Ontario Claim Codes");
                    break;
                case "PE":
                    sh = wb.getSheet("Prince Edward Island Claim Codes");
                    break;
                case "SK":
                    sh = wb.getSheet("Saskatchewan Claim Codes");
                    break;
                case "YT":
                    sh = wb.getSheet("Yukon Claim Codes");
                    break;
            }
            res = sh.getRow(claimCode+1).getCell(4).getNumericCellValue();


            fis.close();


        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        return res;


    }



    public double deductProvTax(String province, int payPeriods, double salary, int claimCode) throws IOException {

        double res = 0;

        try {

            InputStream fis = assetManager.open("Tax Tables/Income Tax Brackets.xls");
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fis);
            Workbook wb = new HSSFWorkbook(poifsFileSystem);

            switch(province) {
                case "AB":
                    sh = wb.getSheet("Alberta");
                    res = calcT4(payPeriods, salary, sh, getProvClaimCode("AB",claimCode));
                    break;
                case "BC":
                    sh = wb.getSheet("British Columbia");
                    res = calcT4(payPeriods, salary, sh, getProvClaimCode("BC", claimCode));
                    break;
                case "MB":
                    sh = wb.getSheet("Manitoba");
                    res = calcT4(payPeriods,salary,sh,getProvClaimCode("MB",claimCode));
                    break;
                case "NB":
                    sh = wb.getSheet("New Brunswick");
                    res = calcT4(payPeriods,salary,sh,getProvClaimCode("NB",claimCode));
                    break;
                case "NL":
                    sh = wb.getSheet("Newfoundland and Labrador");
                    res = calcT4(payPeriods,salary,sh,getProvClaimCode("NL",claimCode));
                    break;
                //	case "NS":
                //		  sh = wb.getSheet("Nova Scotia");
                //		  res = calcT4(payPeriods, salary, sh, getProvClaimCode("NS",claimCode));
                case "NT":
                    sh = wb.getSheet("Northwest Territories");
                    res = calcT4(payPeriods,salary,sh,getProvClaimCode("NT",claimCode));
                    break;
                case "NU":
                    sh = wb.getSheet("Nunavut");
                    res = calcT4(payPeriods,salary,sh,getProvClaimCode("NU",claimCode));
                    break;
                case "ON":
                    sh = wb.getSheet("Ontario");
                    res = calcT4(payPeriods,salary,sh,getProvClaimCode("ON",claimCode));
                    break;
                case "PE":
                    sh = wb.getSheet("Prince Edward Island");
                    res = calcT4(payPeriods,salary,sh,getProvClaimCode("PE",claimCode));
                    break;
                case "SK":
                    sh = wb.getSheet("Saskatchewan");
                    res = calcT4(payPeriods,salary,sh,getProvClaimCode("SK",claimCode));
                    break;
                case "YT":
                    sh = wb.getSheet("Yukon");
                    res = calcMBTax(payPeriods,salary,sh,getProvClaimCode("YT",claimCode));
                    break;
            }

            poifsFileSystem.close();
            fis.close();

        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }


        return res;
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

        InputStream fis = assetManager.open("Tax Tables/CPP Deduction Amounts - 2019.xls");
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fis);

        Workbook wb = new HSSFWorkbook(poifsFileSystem);
        sh = wb.getSheetAt(0);
        res = (sh.getRow(3).getCell(1).getNumericCellValue());

        poifsFileSystem.close();
        fis.close();

        return res;
    }

    public double getMaxEICont() throws IOException {

        double res = 0;

        InputStream fis = assetManager.open("Tax Tables/EI Table.xls");
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fis);
        Workbook wb = new HSSFWorkbook(poifsFileSystem);

        sh = wb.getSheetAt(0);
        wb.close();
        poifsFileSystem.close();
        fis.close();

        res = sh.getRow(3).getCell(1).getNumericCellValue();

        return res;
    }

    //Calculate Federal income tax deductions.
    public double deductFedTax(double pay, int payPeriods, int claimCode) throws IOException {

        double res = 0;
        double baseRate, rate, K, K1, K2,K3, K4 = 0;

        double salary = pay * payPeriods;


        try {

            InputStream fis = assetManager.open("Tax Tables/Income Tax Brackets.xls");
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fis);
            Workbook wb = new HSSFWorkbook(poifsFileSystem);
            sh = wb.getSheetAt(0);


            baseRate = sh.getRow(1).getCell(2).getNumericCellValue();

            if(salary <= sh.getRow(1).getCell(1).getNumericCellValue()) {
                K = sh.getRow(1).getCell(3).getNumericCellValue();
                rate = baseRate;
            }else if(salary > sh.getRow(1).getCell(1).getNumericCellValue() && salary <=  sh.getRow(2).getCell(1).getNumericCellValue()){
                K = sh.getRow(2).getCell(3).getNumericCellValue();
                rate = sh.getRow(2).getCell(2).getNumericCellValue();
            }else if(salary > sh.getRow(2).getCell(1).getNumericCellValue() && salary <= sh.getRow(3).getCell(1).getNumericCellValue()){
                K = sh.getRow(3).getCell(3).getNumericCellValue();
                rate = sh.getRow(3).getCell(2).getNumericCellValue();
            }else if(salary > sh.getRow(3).getCell(1).getNumericCellValue() && salary <= sh.getRow(4).getCell(1).getNumericCellValue()){
                K = sh.getRow(4).getCell(3).getNumericCellValue();
                rate = sh.getRow(4).getCell(2).getNumericCellValue();
            }else {
                K = sh.getRow(5).getCell(3).getNumericCellValue();
                rate = sh.getRow(5).getCell(2).getNumericCellValue();
            }

            K1 = getFedClaimCode(claimCode);
            K2 = calcK2(baseRate, payPeriods, salary);
            K3 = 0;

            if( (baseRate * salary) >= (baseRate * 1222)) {
                K4 = round(baseRate * 1222,2);
            }else {
                K4 = baseRate * salary;
            }

            poifsFileSystem.close();
            fis.close();

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
