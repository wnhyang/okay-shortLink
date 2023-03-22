package org.okay4cloud.okay.shortlink.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wnhyang
 * @date 2023/3/11
 **/
@Data
public class VisitsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String date;

    private String count;
}
