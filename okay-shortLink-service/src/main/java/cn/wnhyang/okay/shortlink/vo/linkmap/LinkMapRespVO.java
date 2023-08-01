package cn.wnhyang.okay.shortlink.vo.linkmap;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author wnhyang
 * @date 2023/7/31
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class LinkMapRespVO extends LinkMapBaseVO{

    /**
     * 主键id
     */
    private Long id;

    /**
     * 短链接
     */
    private String code;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;
}
