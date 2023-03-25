package org.okay4cloud.okay.shortlink.email;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wnhyang
 * @date 2023/3/25
 **/
@Data
public class Email implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 邮箱地址
     */
    private String emailAddress;

    /**
     * 主题
     */
    private String subject;

    /**
     * 内容(优先)
     */
    private String content;
}
