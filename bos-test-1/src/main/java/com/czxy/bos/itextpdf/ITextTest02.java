package com.czxy.bos.itextpdf;

import com.itextpdf.awt.AsianFontMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

public class ITextTest02 {

    public static void main(String[] args) throws Exception{
        //1 创建文档对象
        Document document = new Document();
        //2 设置输出位置
        // 第一个参数：文档数据
        // 第二个参数：输出位置
        PdfWriter.getInstance(document,new FileOutputStream(new File("d:\\java1.pdf")));
        //3 打开文档
        document.open();

        // 创建支持中文的基础字体
        //String name:字体  宋体   楷体  隶书   行楷  ....  "STSong-Light"
        //String encoding：编码 +  对齐方式    UniGB-UCS2-H
        //boolean embedded：是否以内嵌的方式操作字体   true  内嵌-导出pdf会比较大   false不内嵌 --导出的pdf会比较小
        BaseFont baseFont = BaseFont.createFont(AsianFontMapper.ChineseSimplifiedFont, AsianFontMapper.ChineseSimplifiedEncoding_H, false);
//        BaseFont.createFont("STSong-Light","UniGB-UCS2-H",false);

        // 准备字体
        // 第一个参数：基础字体
        // 第二个参数：字体大小
        // 第三个参数：字体样式  加粗  倾斜  加粗倾斜 ..
        // 第四个参数：字体颜色
        Font font = new Font(baseFont, 20, Font.BOLDITALIC, BaseColor.PINK);


        //4 写入内容
        document.add(new Paragraph("czdx,一统江湖，千秋万代,yi  tong   jiang   hu",font));



        //5 关闭文档
        document.close();
    }

}
