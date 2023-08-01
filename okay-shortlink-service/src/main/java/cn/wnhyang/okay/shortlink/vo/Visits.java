package cn.wnhyang.okay.shortlink.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wnhyang
 * @date 2023/3/30
 **/
@Data
public class Visits implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总数
     */
    private int total;

    /**
     * 访问量集合
     */
    private List<VisitsVO> visitsVOList;
}
