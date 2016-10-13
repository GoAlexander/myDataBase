package backend;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelWorker {

	public void importToExcel(String folder, String[][] data) {

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Product database");

		int rowNum = 0;
		Row row = sheet.createRow(rowNum);
		row.createCell(0).setCellValue("Name");
		row.createCell(1).setCellValue("Order date");
		row.createCell(2).setCellValue("Price (roubles)");
		row.createCell(3).setCellValue("Quantity");

		for (int i = 0; i < data.length; i++) {
			createSheetHeader(sheet, ++rowNum, data[i]);
		}

		try (FileOutputStream out = new FileOutputStream(new File(folder + "/Excel_File.xls"))) {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Excel file successfully created!");

	}

	private static void createSheetHeader(HSSFSheet sheet, int rowNum, String[] product) {
		Row row = sheet.createRow(rowNum);

		row.createCell(0).setCellValue(product[0]);
		row.createCell(1).setCellValue(product[1]);
		row.createCell(2).setCellValue(product[2]);
		row.createCell(3).setCellValue(product[3]);
	}

}
