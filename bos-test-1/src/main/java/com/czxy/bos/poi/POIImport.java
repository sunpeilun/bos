package com.czxy.bos.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;

/**
 * 导入测试
 */
public class POIImport {

    public static void main(String[] args) throws Exception {
        //1 读取导入文件---输入流
        FileInputStream is = new FileInputStream(new File("d:\\area.xls"));
        //2 创建Workbook对象--依赖文件
        Workbook wb = new HSSFWorkbook(is);
        //3 读取sheet
        Sheet sheet = wb.getSheetAt(0);
        //4 读取行
        for(Row row:sheet){
            //5 读取单元格
            //5.1 读取
            System.out.print(row.getCell(0).getStringCellValue()+":");// 区域编码
            System.out.print(row.getCell(1).getStringCellValue()+":");
            System.out.print(row.getCell(2).getStringCellValue()+":");
            System.out.print(row.getCell(3).getStringCellValue()+":");
            System.out.println(row.getCell(4).getStringCellValue()+":");


            row.getCell(0).getBooleanCellValue();//布尔
            row.getCell(0).getNumericCellValue();//数值类型
            row.getCell(0).getDateCellValue();//日期类型 2012-02-03
            row.getCell(0).getRichStringCellValue();//文字

        }

        //6 调用Servie层保存数据---打印输出


    }


}
