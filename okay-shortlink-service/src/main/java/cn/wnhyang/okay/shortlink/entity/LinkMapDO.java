package cn.wnhyang.okay.shortlink.entity;

import cn.wnhyang.okay.framework.mybatis.core.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author wnhyang
 * @since 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("link_map")
public class LinkMapDO extends BaseDO {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
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

    /**
     * 通知邮箱
     */
    @Email
    @TableField("email")
    private String email;

}
