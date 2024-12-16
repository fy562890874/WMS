package com.xmut.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("xmut_token")
@ApiModel(value = "Token对象", description = "Token表")
public class Token {
    
    @ApiModelProperty(value = "用户ID")
    @TableId
    private Integer userId;

    @ApiModelProperty(value = "登录token")
    private String token;

    @ApiModelProperty(value = "过期时间")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}