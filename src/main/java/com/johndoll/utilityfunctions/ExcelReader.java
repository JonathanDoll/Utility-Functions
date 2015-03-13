package com.johndoll.utilityfunctions;

/**
 * @author Jonathan Doll
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

    private XSSFSheet worksheet;
    private XSSFWorkbook workbook;
    private XSSFCell cell;

    public ExcelReader(String filePath) {
        try {
            FileInputStream file = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(file);
            worksheet = workbook.getSheetAt(0);
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public ExcelReader(String filePath, String sheetName) {
        try {
            FileInputStream file = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(file);
            worksheet = workbook.getSheet(sheetName);
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public String getCellData(int RowNum, int ColNum) {
        try {
            cell = worksheet.getRow(RowNum).getCell(ColNum);
            if (cell.getCellType() == 1) {
                return cell.getStringCellValue();
            } else {
                return cell.getRawValue();
            }
        } catch (NullPointerException e) {
            return "";
        }
    }

    public void setSheetName(String sheetName) {
        worksheet = workbook.getSheet(sheetName);
    }

    public int getColumnIndex(String columnName) {
        String iterator = "null";
        int column = 0;
        while (!iterator.equals("")) {
            iterator = getCellData(0, column);
            if (iterator.equals(columnName)) {
                return column;
            }
            column++;
        }
        return 0;
    }

    public int getColumnCount() {
        int i = 0;
        while (!getCellData(0, i).equals("")) {
            i++;
        }
        return i;
    }

    public int getRowCount() {
        int i = 0;
        while (!getCellData(i, 0).equals("")) {
            i++;
        }
        return i;
    }
    
    public int getRowCount(int columnNumber){
        int i = 0;
        while (!getCellData(i, columnNumber).equals("")) {
            i++;
        }
        return i;
    }
    
    public int getColumnCount(int rowNumber){
        int i = 0;
        while(!getCellData(rowNumber, i).equals("")){
            i++;
        }
        return i;
    }
    
    public String[] rowToArray(int rowNum){
        String[] row = new String[getColumnCount()];
        for(int i = 0; i < getColumnCount(); i++){
            row[i] = getCellData(rowNum, i);
        }
        return row;
    }
    
    public String[][] worksheetToArray(){
        String[][] wsheet = new String[getRowCount()][getColumnCount()];
        for(int i = 0; i < getRowCount(); i++){
            for(int j = 0; j < getColumnCount(); j++){
                wsheet[i][j] = getCellData(i, j);
            }
        }
        return wsheet;
    }
    
    public String[][] worksheetToArray(int startingRow){
        String[][] wsheet = new String[getRowCount()-startingRow][getColumnCount()];
        for(int i = startingRow; i < getRowCount(); i++){
            for(int j = 0; j < getColumnCount(); j++){
                wsheet[i-startingRow][j] = getCellData(i, j);
            }
        }
        return wsheet;
    }
    
    public String getCurrentSheet(){
        return workbook.getSheetName(workbook.getSheetIndex(worksheet));
    }
    
    public String[] cellToArray(int rowNum, int colNum, String delimeter){
        return getCellData(rowNum, colNum).split(delimeter);
    }

    public int getSheetCount(){
        return workbook.getNumberOfSheets();
    }
    
    public void setSheet(int index){
        worksheet = workbook.getSheetAt(index);
    }
}
