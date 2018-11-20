package com.czxy.bos.controller.take_delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/imagessss")
public class ImageController {
    /**
     * 为什么返回Map类型，因为项目中的很多错误信息通过getError的方法返回，而getError方法返回的就是map集合
     *
     * @return
     */
    @PostMapping("/upload")
    public Map<String, Object> upload(HttpServletRequest request, HttpServletResponse response) {
        //获取服务器的绝对路径
        ServletContext application = request.getServletContext();
        // 通过application获取服务器的绝对路径,带盘符
        String savePath = application.getRealPath("/") + "/images";
        // 获取相对路径：保存到数据库的，是相对于服务器的路径
        String saveUrl = request.getContextPath() + "/images";
        // 定义允许上传的文件扩展名
        //定义允许上传的文件扩展名
        HashMap<String, String> extMap = new HashMap<String, String>();
        extMap.put("image", "gif,jpg,jpeg,png,bmp");
        extMap.put("flash", "swf,flv");
        extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

        // 设置文件上传大小,单位kb
        long maxSize = 1000000;

        response.setContentType("text/html; charset=UTF-8");
        // 判断是否是否是文件上传的request
        if (!ServletFileUpload.isMultipartContent(request)) {
            return getError("请选择文件。");
        }
        //检查目录  d:\\tomcat\\images
        File uploadDir = new File(savePath);
        //这个uploadDir的路径一定存在吗?
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        if (!uploadDir.isDirectory()) {
            return getError("上传目录不存在。");
        }
        //检查目录写权限
        if (!uploadDir.canWrite()) {
            return getError("上传目录没有写权限。");
        }
        // dir:image    flash   media   file
        String dirName = request.getParameter("dir");
        // 如果dirname为空，默认照片
        if (dirName == null) {
            dirName = "image";
        }
        // 判断dirName是否在extMap存在
        if (!extMap.containsKey(dirName)) {
            return getError("目录名不正确。");
        }
        //创建文件夹 d:\\tomcat\\images\\image
        //d:\\tomcat\\images\\flash
        //d:\\tomcat\\images\\media
        //d:\\tomcat\\images\\file
        savePath += dirName + "/";
        saveUrl += dirName + "/";
        // 创建保存位置
        File saveDirFile = new File(savePath);
        if (!saveDirFile.exists()) {
            saveDirFile.mkdirs();
        }
        //创建文件夹 d:\\tomcat\\images\\image\\20180917
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(new Date());
        savePath += ymd + "/";
        saveUrl += ymd + "/";
        //  创建加上时间之后的文件夹
        File dirFile = new File(savePath);
        // 如果文件夹不存在，则创建
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        // 创建上传文件的保存的工厂
        MultipartHttpServletRequest servletRequest = (MultipartHttpServletRequest) request;
        // 获取上传的多个文件的名字
        Iterator<String> iterator = servletRequest.getFileNames();
        // 遍历名字
        while (iterator.hasNext()) {
            // 获取文件的名字
            String fileName = iterator.next();
            // 获取文件
            MultipartFile file = servletRequest.getFile(fileName);
            //检查文件大小
            if (file.getSize() > maxSize) {
                return getError("上传文件大小超过限制。");
            }
            //检查扩展名 jpg   avi
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            // 判断后缀名在对应的类型中是否存在
            if (!extMap.get(dirName).contains(ext)) {
                return getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
            }
            // 给文件随机一个名字
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + ext;
            // 复制内容
            try {
                File uploadedFile = new File(savePath, newFileName);
                file.transferTo(uploadedFile);
            } catch (Exception e) {
                return getError("上传文件失败。");
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("error", 0);
            map.put("url", saveUrl + newFileName);
            return map;

        }

        return null;
    }

    private Map<String, Object> getError(String message) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("error", 1);
        map.put("message", message);
        return map;
    }


    /*******************manage************************/
    /**
     * 整体实现思路：遍历d:\\images\\image\\2017-02-03\\...*.jpg   *.gif...
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/manage", method = {RequestMethod.GET, RequestMethod.POST})
    public void manage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletOutputStream out = response.getOutputStream();
        ServletContext application = request.getServletContext();
        //根目录路径，可以指定绝对路径，比如 /var/www/attached/
        String rootPath = application.getRealPath("/") + "images/";
        //根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
        String rootUrl = request.getContextPath() + "/images/";
        //图片扩展名
        String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};

        String dirName = request.getParameter("dir");
        if (dirName != null) {
            if (!Arrays.<String>asList(new String[]{"image", "flash", "media", "file"}).contains(dirName)) {
                out.println("Invalid Directory name.");
                return;
            }
            rootPath += dirName + "/";
            rootUrl += dirName + "/";
            File saveDirFile = new File(rootPath);
            if (!saveDirFile.exists()) {
                saveDirFile.mkdirs();
            }
        }
        //根据path参数，设置各路径和URL
        String path = request.getParameter("path") != null ? request.getParameter("path") : "";
        String currentPath = rootPath + path;
        String currentUrl = rootUrl + path;
        String currentDirPath = path;
        String moveupDirPath = "";
        if (!"".equals(path)) {
            String str = currentDirPath.substring(0, currentDirPath.length() - 1);
            moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
        }

        //排序形式，name or size or type
        String order = request.getParameter("order") != null ? request.getParameter("order").toLowerCase() : "name";

        //不允许使用..移动到上一级目录
        if (path.indexOf("..") >= 0) {
            out.println("Access is not allowed.");
            return;
        }
        //最后一个字符不是/
        if (!"".equals(path) && !path.endsWith("/")) {
            out.println("Parameter is not valid.");
            return;
        }
        //目录不存在或不是目录
        File currentPathFile = new File(currentPath);
        if (!currentPathFile.isDirectory()) {
            out.println("Directory does not exist.");
            return;
        }

        //遍历目录取的文件信息
        List<Hashtable> fileList = new ArrayList<Hashtable>();
        if (currentPathFile.listFiles() != null) {
            for (File file : currentPathFile.listFiles()) {
                Hashtable<String, Object> hash = new Hashtable<String, Object>();
                String fileName = file.getName();
                if (file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else if (file.isFile()) {
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());
                    hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
                    hash.put("filetype", fileExt);
                }
                hash.put("filename", fileName);
                hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
                fileList.add(hash);
            }
        }

        if ("size".equals(order)) {
            Collections.sort(fileList, new SizeComparator());
        } else if ("type".equals(order)) {
            Collections.sort(fileList, new TypeComparator());
        } else {
            Collections.sort(fileList, new NameComparator());
        }
        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("moveup_dir_path", moveupDirPath);
        msg.put("current_dir_path", currentDirPath);
        msg.put("current_url", currentUrl);
        msg.put("total_count", fileList.size());
        msg.put("file_list", fileList);
        response.setContentType("application/json; charset=UTF-8");
        String msgStr = objectMapper.writeValueAsString(msg);
        out.println(msgStr);

    }

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public class NameComparator implements Comparator {
        public int compare(Object a, Object b) {
            Hashtable hashA = (Hashtable) a;
            Hashtable hashB = (Hashtable) b;
            if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
                return -1;
            } else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
                return 1;
            } else {
                return ((String) hashA.get("filename")).compareTo((String) hashB.get("filename"));
            }
        }
    }

    public class SizeComparator implements Comparator {
        public int compare(Object a, Object b) {
            Hashtable hashA = (Hashtable) a;
            Hashtable hashB = (Hashtable) b;
            if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
                return -1;
            } else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
                return 1;
            } else {
                if (((Long) hashA.get("filesize")) > ((Long) hashB.get("filesize"))) {
                    return 1;
                } else if (((Long) hashA.get("filesize")) < ((Long) hashB.get("filesize"))) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    public class TypeComparator implements Comparator {
        public int compare(Object a, Object b) {
            Hashtable hashA = (Hashtable) a;
            Hashtable hashB = (Hashtable) b;
            if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
                return -1;
            } else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
                return 1;
            } else {
                return ((String) hashA.get("filetype")).compareTo((String) hashB.get("filetype"));
            }
        }
    }


}
