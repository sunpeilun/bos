package com.czxy.bos.controller.print;

import com.czxy.bos.domain.take_delivery.WayBill;
import com.czxy.bos.service.take_delivery.WayBillService;
import com.czxy.bos.util.DownloadUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private WayBillService wayBillService;

    @GetMapping("/exportXls")
    public void exportXls(HttpServletResponse response) throws Exception{

        // 导出  运单信息
        List<WayBill> wayBillList = wayBillService.findAll();

        //1 创建工作簿 HSSFWorkbook   2003    XSSFWorkbook   2007
        Workbook wb = new XSSFWorkbook();
        //2 创建工作表
        Sheet sheet = wb.createSheet();

        // 设置列宽
        sheet.setColumnWidth(0,10*256);
        sheet.setColumnWidth(1,10*256);
        sheet.setColumnWidth(2,10*256);
        sheet.setColumnWidth(3,20*256);
        sheet.setColumnWidth(4,20*256);
        sheet.setColumnWidth(5,20*256);
        sheet.setColumnWidth(6,20*256);
        sheet.setColumnWidth(7,20*256);
        sheet.setColumnWidth(8,20*256);




        /***
         * 定义公共变量
         */
        int rowNo = 0,cellNo = 0;
        Row nRow = null;
        Cell nCell = null;

        /**************大标题*************/
        //3 创建行
        nRow = sheet.createRow(rowNo);
        // 设置行高
        nRow.setHeightInPoints(36);

        //4 创建单元格
        nCell = nRow.createCell(cellNo);
        //5 设置内容
        nCell.setCellValue("bos系统运单信息"+new Date().toLocaleString());
        //6 设置内容格式
        // 合并单元格
        //参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, (short) 0, (short) 9));

        // 横向居中  +   水平居中   +  红色宋体22号
        nCell.setCellStyle(bigTitleCellStyle(wb));


        /*************小标题输出**************/
        // 行号rowNo需要变化吗  列需要变化吗？
        rowNo++;

        String[] titles = {"id","运单号","订单号","寄件人姓名","寄件人电话","寄件人地址","收件人姓名","收件人电话","收件人地址"};

        //3 创建行
        nRow = sheet.createRow(rowNo);
        for (String title:titles){

            //4 创建单元格
            nCell = nRow.createCell(cellNo++);// 先创建cell单元格，然后在自增
            //5 设置内容
            nCell.setCellValue(title);
            //6 设置内容格式
            nCell.setCellStyle(titleCellStyle(wb));
        }


        /**************内容*************/
        // 行号和列号需要变化？
        rowNo++;


        for(WayBill wayBill:wayBillList){
            cellNo=0;
            //3 创建行
            nRow = sheet.createRow(rowNo++);

            //4 创建单元格
            nCell = nRow.createCell(cellNo++);
            //5 设置内容
            nCell.setCellValue(wayBill.getId()+"");
            //6 设置内容格式
            nCell.setCellStyle(contentCellStyle(wb));
            // wayBillNum
            nCell = nRow.createCell(cellNo++);
            nCell.setCellValue(wayBill.getWayBillNum());
            nCell.setCellStyle(contentCellStyle(wb));
            //订单号
            nCell = nRow.createCell(cellNo++);
            nCell.setCellValue(wayBill.getOrderId());
            nCell.setCellStyle(contentCellStyle(wb));
            //发件人姓名
            nCell = nRow.createCell(cellNo++);
            nCell.setCellValue(wayBill.getSendName());
            nCell.setCellStyle(contentCellStyle(wb));
            //发件人电话
            nCell = nRow.createCell(cellNo++);
            nCell.setCellValue(wayBill.getSendMobile());
            nCell.setCellStyle(contentCellStyle(wb));
            //发件人地址
            nCell = nRow.createCell(cellNo++);
            nCell.setCellValue(wayBill.getSendAddress());
            nCell.setCellStyle(contentCellStyle(wb));
            //收件人姓名
            nCell = nRow.createCell(cellNo++);
            nCell.setCellValue(wayBill.getRecName());
            nCell.setCellStyle(contentCellStyle(wb));

            //收件人电话
            nCell = nRow.createCell(cellNo++);
            nCell.setCellValue(wayBill.getRecMobile());
            nCell.setCellStyle(contentCellStyle(wb));
            //收件人地址
            nCell = nRow.createCell(cellNo++);
            nCell.setCellValue(wayBill.getRecAddress());
            nCell.setCellStyle(contentCellStyle(wb));
        }




        /*************7 下载**********************/
        DownloadUtil downloadUtil = new DownloadUtil();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 将wb写入流
        wb.write(byteArrayOutputStream);

        /**
        byteArrayOutputStream 将文件内容写入ByteArrayOutputStream
        response HttpServletResponse	写入response
        returnName 返回的文件名

        */
        downloadUtil.download(byteArrayOutputStream,response,"bos运单表.xlsx");


    }

    public CellStyle bigTitleCellStyle(Workbook wb){
//        横向居中  +   水平居中   +  红色宋体22号
        CellStyle cellStyle = wb.createCellStyle();
        // 横向居中
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        // 垂直居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        Font font = wb.createFont();
        font.setFontHeight((short) 440);
        font.setColor(Font.COLOR_RED);
        font.setFontName("宋体");

        cellStyle.setFont(font);

        return cellStyle;
    }

    public CellStyle titleCellStyle(Workbook wb){
        // 宋体16号  倾斜   边框线   水平垂直居中
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setItalic(true);
        font.setBold(true);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(font);

        // 边框线
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);// 细线
        cellStyle.setBorderRight(CellStyle.BORDER_DASHED);//圆点....
        cellStyle.setBorderBottom(CellStyle.BORDER_DOTTED);// 矩形的虚线_ _ _ _ _
        cellStyle.setBorderLeft(CellStyle.BORDER_DOUBLE);// 双线
        // 横向居中
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        // 垂直居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        return cellStyle;

    }
    public CellStyle contentCellStyle(Workbook wb){
        // 边框线   水平垂直居中
        CellStyle cellStyle = wb.createCellStyle();
        // 边框线
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);// 细线
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);//
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);//
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);//

        return cellStyle;

    }

}
