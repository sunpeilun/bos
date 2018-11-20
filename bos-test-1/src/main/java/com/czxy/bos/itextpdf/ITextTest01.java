package com.czxy.bos.itextpdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

public class ITextTest01 {

    public static void main(String[] args) throws Exception{
        //1 创建文档对象
        Document document = new Document();
        //2 设置输出位置
        // 第一个参数：文档数据
        // 第二个参数：输出位置
        PdfWriter.getInstance(document,new FileOutputStream(new File("d:\\java1.pdf")));
        //3 打开文档
        document.open();
        //4 写入内容
        document.add(new Paragraph("czdx,一统江湖，千秋万代,yi  tong   jiang   hu"));
        //5 关闭文档
        document.close();
    }


}
