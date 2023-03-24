package org.okay4cloud.okay.shortlink.service.jobhandler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.okay4cloud.okay.shortlink.api.entity.LinkMap;
import org.okay4cloud.okay.shortlink.mapper.LinkMapMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * @author wnhyang
 * @date 2023/3/24
 **/
@Component
@RequiredArgsConstructor
public class MyXxlJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyXxlJob.class);

    private final LinkMapMapper linkMapMapper;

    @XxlJob("emailJobHandler")
    public void emailJobHandler() {
        LOGGER.info("emailJobHandler exec......");
        XxlJobHelper.log("XXL-JOB, emailJobHandler.");
        LocalDate now = LocalDate.now();
        LocalDate now3 = now.plusDays(3);
        List<LinkMap> linkMaps = linkMapMapper.selectList(new LambdaQueryWrapper<LinkMap>()
                .ge(LinkMap::getExpireTime, now)
                .le(LinkMap::getExpireTime, now3));
        XxlJobHelper.log(linkMaps.toString());
    }
}
