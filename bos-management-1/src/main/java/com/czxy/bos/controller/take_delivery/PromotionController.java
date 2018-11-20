package com.czxy.bos.controller.take_delivery;

import com.czxy.bos.domain.take_delivery.Promotion;
import com.czxy.bos.domain.vo.DataGridResult;
import com.czxy.bos.service.take_delivery.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/promotion")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 如何传递参数？
     * @RequestBody Promotion promotion:报错
     * @return
     *
     * 上传的图片正常保存到本地的tomcat中：缺点：每次重启之后，tomcat中的内容都会被清空，不合理
     *     保存到阿里云的图片服务器
     */
    //@PostMapping
    public ResponseEntity<Void> savePromotion_old(MultipartFile titleImgFile,Promotion promotion){
        // 上传图片
        // 1 获取tomcat的绝对路径
        ServletContext application = request.getServletContext();
        String savePath = application.getRealPath("/")+"/upload";
        // 判断savePath是否存在
        File saveFile = new File(savePath);
        if(!saveFile.exists()){
            saveFile.mkdir();
        }

        // 2 获取tomcat的相对路径
        String saveUrl = request.getContextPath() + "/upload";
        // 3 随机文件名
        // 3.1 获取文件的名字
        String filename = titleImgFile.getOriginalFilename();
        // 3.2 获取文件的后缀名
        String ext = filename.substring(filename.lastIndexOf("."));
        // 3.3 随机文件名
        String newFileName = UUID.randomUUID().toString() + ext;
        // 4 组装新的地址
        savePath += "/" + newFileName;
        File newFile = new File(savePath);
        // 5 复制
        try {
            titleImgFile.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 6 把地址给promotion
        saveUrl += "/" + newFileName;
        promotion.setTitleImg(saveUrl);

        // 调用Service层保存数据
        promotionService.savePromotion(promotion);

        return null;
    }


    /**
     *
     * 新的图片上传吗，不上传到tomcat的服务器上，直接放在服务器的tomcat外面的文件夹中
     *
     * @param titleImgFile
     * @param promotion
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> savePromotion(MultipartFile titleImgFile,Promotion promotion){
        // 上传图片
        // 1 获取tomcat的绝对路径
        String savePath = "e:/"+"/upload";
        // 判断savePath是否存在
        File saveFile = new File(savePath);
        if(!saveFile.exists()){
            saveFile.mkdir();
        }

        // 2 相对路径---相对路径不改的原因是：
        String saveUrl =  "/upload";
        // 3 随机文件名
        // 3.1 获取文件的名字
        String filename = titleImgFile.getOriginalFilename();
        // 3.2 获取文件的后缀名
        String ext = filename.substring(filename.lastIndexOf("."));
        // 3.3 随机文件名
        String newFileName = UUID.randomUUID().toString() + ext;
        // 4 组装新的地址
        savePath += "/" + newFileName;
        File newFile = new File(savePath);
        // 5 复制
        try {
            titleImgFile.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 6 把地址给promotion
        saveUrl += "/" + newFileName;
        promotion.setTitleImg(saveUrl);

        // 调用Service层保存数据
        promotionService.savePromotion(promotion);

        return null;
    }





    /**
     * bos系统的分页功能
     * @param page
     * @param rows
     * @return
     */
    @GetMapping
    public ResponseEntity<DataGridResult> findPromotionByPage(Integer page,Integer rows){
        try {
            DataGridResult result = promotionService.findPromotionByPage(page, rows);
            return new ResponseEntity<>(result,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }



}
