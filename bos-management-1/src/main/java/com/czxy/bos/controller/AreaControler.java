package com.czxy.bos.controller;

import com.czxy.bos.domain.base.Area;
import com.czxy.bos.domain.vo.DataGridResult;
import com.czxy.bos.service.AreaService;
import com.czxy.bos.util.DownloadUtil;
import com.czxy.bos.util.PinYin4jUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/area")
public class AreaControler {

    @Autowired
    private AreaService areaService;

    /**
     * 文件上传的method一定是POST
     * @param file : SpringMVC中接收上传文件的对象MultipartFile
     * @return
     */
    @PostMapping
    @RequestMapping("/batchImport")
    public ResponseEntity<Void> batchImport(@RequestParam("myFile") MultipartFile file){
        try {
            // 默认存储位置：C:\Users\Administrator\AppData\Local\Temp\tomcat.4498439699212789950.8088\work\Tomcat\localhost\ROOT
            // 此位置是个临时位置，当该次请求完成的时候，该文件自动删除，如果想保留，就复制到另外一个位置d    f

            // 1 将上传的文件保存起来
            // 1.1 获取文件名
            String originalFilename = file.getOriginalFilename();// 文件名
            String name = file.getName();//此名字对应<input> 文本框的name属性的值
            // 1.2 设置保存的path
            String path = "d:\\"+ UUID.randomUUID().toString()+originalFilename;
            // 1.3 创建File--path
            File myFile = new File(path);
            // 1.4 复制
            file.transferTo(myFile);


            // 解析xls，将数据存入数据库
            // 2 创建Workbook
            // 2.1 创建输入流
//            FileInputStream is = new FileInputStream(myFile);
            InputStream is = file.getInputStream();

            // 2.2 创建Workbook
            Workbook wb = new HSSFWorkbook(is);
            // 3 获取Sheet
            Sheet sheet = wb.getSheetAt(0);

            // 定义List集合存放数据，最后一次性保存
            List<Area> list = new ArrayList<>();

            // 4 获取row
            for(Row row:sheet){
                // 5 获取单元格数据
                // 跳过第一行
                if(row.getRowNum()==0){
                    continue;
                }
                // 如果第一列为空，整条数据都不读取
                if(row.getCell(0)==null || StringUtils.isBlank(row.getCell(0).getStringCellValue())){
                    continue;
                }

                Area area = new Area();
                // 5.1 区域编码id
                area.setId(row.getCell(0).getStringCellValue());
                // 5.2 省份
                area.setProvince(row.getCell(1).getStringCellValue());
                // 5.3 城市
                area.setCity(row.getCell(2).getStringCellValue());
                // 5.4 区域
                area.setDistrict(row.getCell(3).getStringCellValue());
                // 5.5 邮编
                area.setPostcode(row.getCell(4).getStringCellValue());


                /***********使用pinyin4j准备城市编码和城市简码************/
                //5.6 城市编码
                String cityName = area.getProvince().substring(0,area.getProvince().length()-1)+
                                  area.getCity().substring(0,area.getCity().length()-1)+
                                  area.getDistrict().substring(0,area.getDistrict().length()-1);
                String cityCode = PinYin4jUtils.hanziToPinyin(cityName,"");

                //5.7 城市简码
                String shortCode = PinYin4jUtils.stringArrayToString(PinYin4jUtils.getHeadByString(cityName));

                // 5.8 设置area的值
                area.setCitycode(cityCode);
                area.setShortcode(shortCode);

                list.add(area);
            }


            // 6 保存至数据库
            areaService.saveAreas(list);


        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @Autowired
//    private HttpServletResponse response;

    /**
     * 都以excel2003为基础
     * @return
     */
    @GetMapping("/batchExport")
    public ResponseEntity<Void> batchExport(HttpServletResponse response){
        try {

            //1 准备导出的Workbook--创建Workbook
//            Workbook wb = new HSSFWorkbook(); //excel2003
            Workbook wb = new XSSFWorkbook(); // excel2007+
            //2 创建sheet
            Sheet sheet = wb.createSheet();

            //3 设置标题
            // 创建标题
            // 定义公共变量
            Row nRow = null;
            Cell nCell = null;
            int rowNo = 0;// 行号
            int cellNo = 0;// 列号

            String[] titles = {"区域编码","省份","城市","地区","邮编","城市编码","城市简码"};
            /*****************将标题添加至workbook中********************/
            // 创建标题行
            nRow = sheet.createRow(rowNo);
            for(String title:titles){
                nCell = nRow.createCell(cellNo++);
                nCell.setCellValue(title);
            }

            //4 遍历数据列表，创建row---? 多少条数据，就需要创建多少条row--循环----》条件：数据库中所有的数据
            //4.1  查出t_area表中的所有数据
            List<Area> list = areaService.findAllAreas();
            //4.2 遍历数据 28320
            for (int i=0;i<100;i++)
            for(Area area:list){
                // 行号++
                rowNo++;
                // 列号要变化吗？
                cellNo = 0;
                // 创建行
                nRow = sheet.createRow(rowNo);

                // 创建列
                //"区域编码",
                nCell = nRow.createCell(cellNo++);
                nCell.setCellValue(area.getId());
                //"省份","
                nCell = nRow.createCell(cellNo++);
                nCell.setCellValue(area.getProvince());
                //城市",
                nCell = nRow.createCell(cellNo++);
                nCell.setCellValue(area.getCity());
                //"地区",
                nCell = nRow.createCell(cellNo++);
                nCell.setCellValue(area.getDistrict());
                //"邮编","
                nCell = nRow.createCell(cellNo++);
                nCell.setCellValue(area.getPostcode());
                //城市编码",
                nCell = nRow.createCell(cellNo++);
                nCell.setCellValue(area.getCitycode());
                //"城市简码"
                nCell = nRow.createCell(cellNo++);
                nCell.setCellValue(area.getShortcode());

            }

            //5 下载
            DownloadUtil down = new DownloadUtil();
            //5.1 准备流
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            wb.write(byteArrayOutputStream);

            /**
             * 参数一：ByteArrayOutputStream byteArrayOutputStream, 下载文件的流
             * 参数二：HttpServletResponse response：response对象
             * 参数三：String returnName：下载的文件名字
             */
            down.download(byteArrayOutputStream,response,"地区.xlsx");


        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }


    /**
     * 分页查找信息
     * @param page 页码
     * @param rows 总条数
     * @param area 查询条件
     * @return
     */
    @GetMapping
    public ResponseEntity<DataGridResult> findAreaByPage(Integer page, Integer rows, Area area){
        try {
            DataGridResult pageResult = areaService.findAreaByPage(page, rows, area);
            return new ResponseEntity<>(pageResult,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }







}
