package com.johndoll.utilityfunctions.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Jonathan Doll
 */
public class ExcelWriter {

    private FileOutputStream fout;
    private XSSFWorkbook workbook;
    private XSSFSheet worksheet;
    private XSSFCell cell;
    private XSSFRow row;

    public ExcelWriter(String path, String fileName) throws FileNotFoundException, IOException {
        fout = new FileOutputStream(path + "//" + fileName + ".xlsx");
        workbook = new XSSFWorkbook();
        worksheet = workbook.createSheet();
    }

    public void writeToCell(int rowNum, int column, String data) {
        if (worksheet.getRow(rowNum) == null) {
            row = worksheet.createRow(rowNum);
        } else {
            row = worksheet.getRow(rowNum);
        }
        cell = row.createCell(column);
        cell.setCellValue(data);
    }

    public void writeToCell(int rowNum, int column, int data) {
        if (worksheet.getRow(rowNum) == null) {
            row = worksheet.createRow(rowNum);
        } else {
            row = worksheet.getRow(rowNum);
        }
        cell = row.createCell(column);
        cell.setCellValue(data);
    }

    public void writeToCell(int rowNum, int column, double data) {
        if (worksheet.getRow(rowNum) == null) {
            row = worksheet.createRow(rowNum);
        } else {
            row = worksheet.getRow(rowNum);
        }
        cell = row.createCell(column);
        cell.setCellValue(data);
    }

    public void arrayToRow(int row, String[] stringArray) {
        for (int i = 0; i < stringArray.length; i++) {
            writeToCell(row, i, stringArray[i]);
        }
    }

    public void arrayToRow(int row, int[] intArray) {
        for (int i = 0; i < intArray.length; i++) {
            writeToCell(row, i, intArray[i]);
        }
    }

    public void arrayToRow(int row, double[] doubleArray) {
        for (int i = 0; i < doubleArray.length; i++) {
            writeToCell(row, i, doubleArray[i]);
        }
    }
    
    public void arrayToColumn(int column, int startingRow, String[] array){
        for(int i = 0; i < array.length; i++){
            writeToCell(startingRow + i, column, array[i]);
        }
    }
    
    public void arrayToColumn(int column, int startingRow, int[] array){
        for(int i = 0; i < array.length; i++){
            writeToCell(startingRow + i, column, array[i]);
        }
    }
    
    public void arrayToColumn(int column, int startingRow, double[] array){
        for(int i = 0; i < array.length; i++){
            writeToCell(startingRow + i, column, array[i]);
        }
    }
    
    public void arrayToWorksheet(String[][] array){
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[0].length; j++){
                writeToCell(i, j, array[i][j]);
            }
        }        
    }
    
    public void arrayToWorksheet(int[][] array){
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[0].length; j++){
                writeToCell(i, j, array[i][j]);
            }
        }        
    }
    
    public void arrayToWorksheet(double[][] array){
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[0].length; j++){
                writeToCell(i, j, array[i][j]);
            }
        }        
    }
    
    public void deleteRow(int rowNum) {
        worksheet.createRow(rowNum);
    }

    public void close() throws IOException {
        workbook.write(fout);
        fout.flush();
        fout.close();
    }
    
    public void createSheet(String sheetName){
        workbook.createSheet(sheetName);
    }
    
    public void setSheet(String sheetName){
        worksheet = workbook.getSheet(sheetName);
    }
    
    public String getSheet(){
        return worksheet.getSheetName();
    }

}
