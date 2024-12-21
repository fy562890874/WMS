package com.xmut.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("xmut_warehouse")
public class Warehouse {
    @TableId
    private Integer warehouseId;
    private String warehouseName;
    private String address;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}