package com.jt.controller;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 实现文件上传之后，返回特定数据
     * url：file
     * 参数：fileImage
     * 返回值要求
     * 注意事项：默认条件下 文件上传不能超过1M
     */
    @RequestMapping("/file")
    public String file(MultipartFile fileImage) throws IOException {
        //确定文件目录结构
        String path = "F:/JT_IMAGE/";
        File file = new File(path);
        if (!file.exists()){ //表示取非
            file.mkdirs();   //可以创建多级目录
        }
        //拼接文件路径信息
        String fileName = fileImage.getOriginalFilename(); //获取图片真实名称
        String filePath = path + fileName;
        //按照指定的路径上传图片
        fileImage.transferTo(new File(filePath));
        return "文件上传成功";
    }

    /**
     * 图片上传实现
     * url:pic/upload?dir=image
     * args:uploadFile
     * 返回值结果：ImageVO对象
     */
    @RequestMapping("/pic/upload")
    public ImageVO fileUpload(MultipartFile uploadFile){
        return fileService.fileUpload(uploadFile);
    }
}
