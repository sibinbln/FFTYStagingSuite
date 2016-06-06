package TestCases.Temp;

import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

public class Demo {
 
 public static XSSFSheet ExcelWShseet;
 public static XSSFWorkbook ExcelWBook;
 public static XSSFCell cell;
 public static XSSFRow row;
 
 @Test
 public void testDemo() throws Exception
 { 
  int rowcounter = 0;
 //public static void readExcel(int rowcounter){
  FileInputStream fis = new FileInputStream("E://eclipse//TestData.xlsx");
  XSSFWorkbook srcBook = new XSSFWorkbook(fis);
  XSSFSheet srcSheet = srcBook.getSheetAt(0);
  int rownum = rowcounter;
  XSSFRow srcRow = srcSheet.getRow(rownum);
  XSSFCell cell1 = srcRow.getCell(1);
  System.out.println(cell1);
  srcBook.close();
 }
 }