package cn.wnhyang.okay.shortlink.vo.linkmap;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author wnhyang
 * @date 2023/7/31
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class LinkMapUpdateReqVO extends LinkMapBaseVO {

    /**
     * 主键id
     */
    @NotNull(message = "链接不能为空")
    private Long id;
}
