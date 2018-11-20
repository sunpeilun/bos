package com.czxy.bos.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 导出测试
 */
public class POIExport {

    /**
     * 不管是操作excel2003还是excel2007，操作步骤都是一直的
     * excel2003----->HSSFWorkbook
     * excel2007----->XSSFWorkbook
     *
     * @param args
     */
    public static void main(String[] args) throws  Exception{
        //1 创建Excel工作簿
        Workbook wb = new HSSFWorkbook();
        //2 创建工作表Sheet
        Sheet sheet = wb.createSheet();
        //3 创建行对象
        Row row = sheet.createRow(3);
        //4 创建单元格
        Cell cell = row.createCell(3);
        //5 设置内容
        cell.setCellValue("czdx，一统江湖，千秋万载");
        //6 设置样式
        // 6.1 创建字体
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)36);
        font.setFontName("华文彩云");
        font.setColor(Font.COLOR_RED);
        // 6.2 创建单元格样式
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(font);

        // 6.3 单元格作用字体
        cell.setCellStyle(cellStyle);


        //7 保存到硬盘   main函数-->输出流
        FileOutputStream os = new FileOutputStream(new File("d:\\a.xls"));
        // 将wb写到os中
        wb.write(os);
        os.flush();
        os.close();

        //8 下载(web项目)

        System.out.println("okok...");

    }

}
