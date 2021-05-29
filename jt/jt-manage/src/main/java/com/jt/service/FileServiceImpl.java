package com.jt.service;

import com.jt.vo.ImageVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService{

    //设定本地存储的根目录
    @Value("${image.localPath}")
    private String localPath;   // "F:/JT_IMAGE/";
    @Value("${image.urlPath}")
    private String urlPath;

    /**
     * 1.图片校验 jpg png gif
     * 2.是否为恶意程序
     * 3.分目录存储。目的：控制目录中图片的数量
     * 4.防止重名
     *
     * 解决方案：
     *  1.根据if判断，判断后缀是否包含指定的名称？
     *  2.将数据把他填充到图片的对象中。利用工具API获取宽高
     *  3.利用hash方式进行目录存储，利用时间进行目录存储
     *  4.随机数，UUID
     */
    @Override
    public ImageVO fileUpload(MultipartFile uploadFile) {
        //1.需要获取图片名称
        String fileName = uploadFile.getOriginalFilename();
        fileName = fileName.toLowerCase();
        //2.利用正则表达式判断是否为空
        if(!fileName.matches("^.+\\.(jpg|png|git)$")){
            //表示不是图片信息
            return ImageVO.fail();
        }

        //判断图片是否为恶意程序
        try {
            BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            if (width == 0 || height == 0) {
                return ImageVO.fail();
            }
            //分目录存储
            String dateDir = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
            String localDir = localPath + dateDir;
            File file = new File(localDir);
            if (!file.exists()){
                //如果目录不存在创建目录
                file.mkdirs();
            }
            //为了防止文件重名，指定文件名称
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            String realFileName = uuid + fileType;

            //实现文件上传
            String realFilePath = localDir + realFileName;
            File realFile = new File(realFilePath);
            uploadFile.transferTo(realFile);

            //设定虚拟的地址
            String url = urlPath + dateDir + realFileName;
            //String url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F5b67505c56b5f5963decee6b7ed3d426762836f79384-hWntBr_fw236&refer=http%3A%2F%2Fhbimg.b0.upaiyun.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622811884&t=220e0a30293c60649b469d5764263c40";
            return ImageVO.success(url,width,height);
        } catch (IOException e) {
            e.printStackTrace();
            return ImageVO.fail();
        }
    }
}
