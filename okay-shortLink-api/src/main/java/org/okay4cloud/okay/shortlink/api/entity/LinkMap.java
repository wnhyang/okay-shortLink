package org.okay4cloud.okay.shortlink.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.okay4cloud.okay.common.mybatis.base.BaseEntity;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wnhyang
 * @since 2023-02-21
 */
@Getter
@Setter
@TableName("link_map")
public class LinkMap extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 长链接
     */
    @NotBlank(message = "原链接不能为空")
    @TableField("link")
    private String link;

    /**
     * 短链接
     */
    @TableField("code")
    private String code;

    /**
     * 类型
     */
    @NotNull(message = "类型不能为空")
    @TableField("type")
    private String type;

    /**
     * 说明
     */
    @TableField("remark")
    private String remark;

    /**
     * 过期时间
     */
    @Future
    @TableField("expire_time")
    private LocalDateTime expireTime;

}
