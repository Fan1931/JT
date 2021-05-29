package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImageVO {

    private Integer error; //检查图片上传是否有误 0正确
    private String url; //访问图片的地址信息
    private Integer width;
    private Integer height;

    /**
     * 封装工具API简化程序调用过程
     */
    public static ImageVO fail(){
        return new ImageVO(1,null,null,null);
    }

    public static ImageVO success(String url,Integer width,Integer height){
        //业务：需要检查返回值是否为null？
        if (StringUtils.isEmpty(url) || width == null || width <= 0 || height == null || height <= 0) {
            //return new ImageVO(1,null,null,null);
            return ImageVO.fail();
        }
        return new ImageVO(0, url, width, height);
    }
}
