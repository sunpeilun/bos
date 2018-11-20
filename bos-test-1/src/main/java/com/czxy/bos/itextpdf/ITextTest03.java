package com.czxy.bos.itextpdf;

import com.itextpdf.awt.AsianFontMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ITextTest03 {

    public static void main(String[] args) throws Exception{
        // 准备数据
        List<Object[]> list = new ArrayList<>();
        list.add(new Object[]{"月份","去年销量","今年销量"});
        list.add(new Object[]{"七月份",1000,1500});
        list.add(new Object[]{"八月份",1200,1300});
        list.add(new Object[]{"九月份",900,1100});


        //1 创建文档对象
        Document document = new Document();
        //2 设置输出位置
        PdfWriter.getInstance(document,new FileOutputStream(new File("d:\\出货表.pdf")));
        //3 打开文档
        document.open();
        //4 输出内容
        // 创建基础字体支持中文
        BaseFont baseFont = BaseFont.createFont(AsianFontMapper.ChineseSimplifiedFont, AsianFontMapper.ChineseSimplifiedEncoding_H, false);

        /****大标题输出******/
        Font titleFont = new Font(baseFont, 30, Font.BOLD, BaseColor.RED);
        // 居中
        Paragraph bigTitleParagraph = new Paragraph("出货表", titleFont);
        bigTitleParagraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(bigTitleParagraph);

        /****作者输出******/
        Font authorFont = new Font(baseFont, 15, Font.NORMAL, BaseColor.BLACK);
        Paragraph authorParagraph = new Paragraph("传智学院", authorFont);
        authorParagraph.setAlignment(Paragraph.ALIGN_RIGHT);

        document.add(authorParagraph);

        /****表格输出******/
        Font contentFont = new Font(baseFont, 15, Font.NORMAL, BaseColor.BLUE);
        // 参数：列数
        PdfPTable table = new PdfPTable(3);
        // 循环数据
        for(Object[] values:list){
            table.addCell(new PdfPCell(new Phrase(values[0].toString(),contentFont)));
            table.addCell(new PdfPCell(new Phrase(values[1].toString(),contentFont)));
            table.addCell(new PdfPCell(new Phrase(values[2].toString(),contentFont)));
        }

        // 设置元素距离上一个元素的距离
        table.setSpacingBefore(10);

        // 将表格添加到文档中
        document.add(table);

        //5 关闭文档
        document.close();
    }

}
