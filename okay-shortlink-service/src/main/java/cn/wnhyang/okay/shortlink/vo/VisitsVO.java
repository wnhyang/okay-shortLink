package cn.wnhyang.okay.shortlink.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wnhyang
 * @date 2023/3/11
 **/
@Data
public class VisitsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    private String date;

    /**
     * 数量
     */
    private String count;
}
