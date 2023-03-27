package org.okay4cloud.okay.shortlink.api.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wnhyang
 * @date 2023/3/14
 **/
@Data
public class LinkMapDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 长链接
     */
    @NotBlank(message = "原链接不能为空")
    private String link;

    /**
     * 类型
     */
    @NotNull(message = "类型不能为空")
    private String type;

    /**
     * 说明
     */
    private String remark;

    /**
     * 过期时间
     */
    @Future
    private LocalDateTime expireTime;

    /**
     * 通知邮箱
     */
    @Email
    private String email;
}
