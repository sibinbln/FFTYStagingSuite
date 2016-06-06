package Operation;

import java.io.File;
import java.io.IOException;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReadExcel 
{

    private String inputFile;
    private int sheetnumber;
    String[][] data = null;
    public void setInputFile(String inputFile, int sheetno) 
    {
        this.inputFile = inputFile;
        this.sheetnumber = sheetno;
    }

    public String[][] readFile() throws IOException  
    {
        File inputWorkbook = new File(inputFile);
        Workbook w;

        try 
        {
            w = Workbook.getWorkbook(inputWorkbook);
            
            // Get the first sheet
            Sheet sheet = w.getSheet(sheetnumber);
            data = new String[sheet.getColumns()][sheet.getRows()];
            
            // Loop over first 10 column and lines
            for (int j = 0; j <sheet.getColumns(); j++) 
            {
                for (int i = 0; i < sheet.getRows(); i++) 
                {
                    Cell cell = sheet.getCell(j, i);
                    data[j][i] = cell.getContents();
                    //System.out.println(cell.getContents());
                }
            }
        } 
        catch (BiffException e) 
        {
            e.printStackTrace();
        }
    return data;
    }
}