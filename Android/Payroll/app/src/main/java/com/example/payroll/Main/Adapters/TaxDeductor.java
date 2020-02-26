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


public class TaxDeductor {

    private Sheet sh;
    private AssetManager assetManager;

    public TaxDeductor(Context context){
        assetManager = context.getAssets();
    }
}
