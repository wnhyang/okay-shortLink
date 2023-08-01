package cn.wnhyang.okay.shortlink.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.wnhyang.okay.shortlink.email.EmailService;
import cn.wnhyang.okay.shortlink.entity.LinkMapDO;
import cn.wnhyang.okay.shortlink.mapper.LinkMapMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author wnhyang
 * @date 2023/3/24
 **/
@Component
@RequiredArgsConstructor
@Slf4j
public class MyXxlJob {

    private final LinkMapMapper linkMapMapper;

    private final EmailService emailService;

    /**
     * 扫表，3天内过期的进行邮件通知
     */
    @XxlJob("emailJobHandler")
    public void emailJobHandler() {
        log.info("emailJobHandler exec......");
        XxlJobHelper.log("XXL-JOB, emailJobHandler.");
        LocalDate now = LocalDate.now();
        LocalDate now3 = now.plusDays(3);
        // 用户分组发邮件
        List<LinkMapDO> linkMaps = linkMapMapper.selectList(new LambdaQueryWrapper<LinkMapDO>()
                .ge(LinkMapDO::getExpireTime, now)
                .le(LinkMapDO::getExpireTime, now3));
        log.info("即将过期的短链接数量{}", linkMaps.size());
        XxlJobHelper.log("即将过期的短链接数量{}", linkMaps.size());
        if (!CollUtil.isEmpty(linkMaps)) {
            Map<String, List<LinkMapDO>> collect = linkMaps.stream().collect(Collectors.groupingBy(LinkMapDO::getEmail));
            log.info("分组后{}", collect);
            for (Map.Entry<String, List<LinkMapDO>> entry : collect.entrySet()) {
                String to = entry.getKey();
                if (StrUtil.isNotBlank(to)) {
                    Map<String, Object> model = new HashMap<>(8);
                    model.put("subject", "短链接即将过期提醒");
                    model.put("title", "您的短链接将在三天内失效，请务必确认是否还需要，过期的前三天我们都会发送此邮件来向您确认，即将过期的短链接如下：");
                    List<LinkMapDO> list = entry.getValue();
                    model.put("list", list);
                    model.put("instance", UUID.randomUUID().toString());
                    emailService.sendHtmlMail(to, "短链接即将过期提醒", model);
                    log.info("发给 {} 数量{}", to, list.size());
                    XxlJobHelper.log("发给 {} 数量{}", to, list.size());
                }
            }
        }
    }
}
