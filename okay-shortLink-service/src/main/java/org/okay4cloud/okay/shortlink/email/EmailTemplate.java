package org.okay4cloud.okay.shortlink.email;

import lombok.experimental.UtilityClass;

/**
 * @author wnhyang
 * @date 2023/3/25
 **/
@UtilityClass
public class EmailTemplate {

    public final String EXPIRE_TEMPLATE = "<table border=\"1\" cellpadding=\"3\" style=\"border-collapse:collapse; width:80%;\" >\n" +
            "   <thead style=\"font-weight: bold;color: #ffffff;background-color: #ff8c00;\" >" +
            "      <tr>\n" +
            "         <td width=\"20%\" >" + "短链接" + "</td>\n" +
            "         <td width=\"10%\" >" + "链接" + "</td>\n" +
            "         <td width=\"20%\" >" + "描述" + "</td>\n" +
            "         <td width=\"10%\" >" + "过期时间" + "</td>\n" +
            "      </tr>\n" +
            "   </thead>\n" +
            "   <tbody>\n" +
            "      <tr>\n" +
            "         <td>{0}</td>\n" +
            "         <td>{1}</td>\n" +
            "         <td>{2}</td>\n" +
            "         <td>{3}</td>\n" +
            "      </tr>\n" +
            "   </tbody>\n" +
            "</table>";
}
