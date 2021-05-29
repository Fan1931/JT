package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@TableName(value = "tb_item_desc")
@JsonIgnoreProperties(ignoreUnknown=true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ItemDesc extends BasePojo{

    private static final long serialVersionUID = 7990706577191245891L;
    @TableId
    private Long itemId; //主键信息
    private String itemDesc; //商品详情信息

}
