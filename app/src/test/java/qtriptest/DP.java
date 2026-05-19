package qtriptest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook ;
import org.testng.annotations.DataProvider;

public class DP {

    @DataProvider(name = "dpMethod")
    public Object[][] dpMethod(Method m) throws IOException {
        List<List<String>> outputList = new ArrayList<>();

        String filePath = System.getProperty("user.dir") + "/src/test/resources/DatasetsforQTrip.xlsx";
        FileInputStream excelFile = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(excelFile);

        // Map method name to sheet name. E.g., TestCase01_UserOnboardingFlow -> TestCase01
        String methodName = m.getName();
        String sheetName = methodName.contains("_") ? methodName.split("_")[0] : methodName;

        Sheet selectedSheet = workbook.getSheet(sheetName);
        if (selectedSheet == null) {
            workbook.close();
            excelFile.close();
            throw new RuntimeException("DP.dpMethod: sheet not found for test method. Expected sheet: " + sheetName + " in file: " + filePath);
        }

        System.out.println("Reading sheet: " + sheetName);

        Iterator<Row> iterator = selectedSheet.iterator();
        int rowIndex = 0;
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            // skip header row
            if (rowIndex == 0) {
                rowIndex++;
                continue;
            }
            List<String> innerList = new ArrayList<>();
            // start from column 1 (skip serial col if present)
            for (int c = 1; c < nextRow.getLastCellNum(); c++) {
                Cell cell = nextRow.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String val;
                if (cell.getCellType() == CellType.STRING) {
                    val = cell.getStringCellValue();
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    double d = cell.getNumericCellValue();
                    if (d == (long) d) {
                        val = String.valueOf((long) d);
                    } else {
                        val = String.valueOf(d);
                    }
                } else if (cell.getCellType() == CellType.BOOLEAN) {
                    val = String.valueOf(cell.getBooleanCellValue());
                } else {
                    val = cell.toString();
                }
                innerList.add(val.trim());
            }
            if (!innerList.isEmpty()) {
                outputList.add(innerList);
            }
            rowIndex++;
        }

        workbook.close();
        excelFile.close();

        System.out.println("Data loaded for " + m.getName() + ": " + outputList);

        Object[][] data = new Object[outputList.size()][];
        for (int i = 0; i < outputList.size(); i++) {
            data[i] = outputList.get(i).toArray(new Object[0]);
        }
        return data;
    }
}
