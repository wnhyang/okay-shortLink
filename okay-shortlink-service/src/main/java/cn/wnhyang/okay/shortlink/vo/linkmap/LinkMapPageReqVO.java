package cn.wnhyang.okay.shortlink.vo.linkmap;

import cn.wnhyang.okay.framework.common.pojo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * @author wnhyang
 * @date 2023/7/30
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class LinkMapPageReqVO extends PageParam {

    /**
     * 长链接
     */
    private String link;

    /**
     * 短链接
     */
    @Length(min = 6, max = 6, message = "短链接长度必须为6为位")
    private String code;

    /**
     * 类型
     */
    private String type;

}
