package com.xmut.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("xmut_log")
@ApiModel(value = "Log对象", description = "系统日志表")
public class Log {
    
    @ApiModelProperty(value = "日志ID")
    @TableId
    private Integer logId;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "操作内容")
    private String action;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;
}