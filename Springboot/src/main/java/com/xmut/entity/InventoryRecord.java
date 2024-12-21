package com.xmut.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("xmut_inventory_record")
@ApiModel(value = "InventoryRecord对象", description = "出入库记录表")
public class InventoryRecord {
    
    @ApiModelProperty(value = "记录ID")
    @TableId
    private Integer recordId;

    @ApiModelProperty(value = "货品ID")
    private Integer productId;

    @ApiModelProperty(value = "仓库ID")
    private Integer warehouseId;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "操作类型（0入库，1出库，2调拨）")
    private Byte operationType;

    @ApiModelProperty(value = "关联记录ID（调拨对应的另一条记录）")
    private Integer relatedRecordId;

    @ApiModelProperty(value = "操作人用户ID")
    private Integer operatorId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime recordTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "对接人")
    private String contactPerson;
}   