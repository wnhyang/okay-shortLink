package org.okay4cloud.okay.shortlink.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.okay4cloud.okay.shortlink.api.dto.LinkMapDTO;
import org.okay4cloud.okay.shortlink.api.entity.LinkMap;
import org.okay4cloud.okay.shortlink.api.vo.VisitsVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wnhyang
 * @since 2023-02-21
 */
public interface LinkMapService extends IService<LinkMap> {
    /**
     * 重定向
     *
     * @param code 短链接
     * @return 长链接
     */
    String redirect(String code);

    /**
     * 添加链接映射
     *
     * @param linkMapDTO 链接
     * @return true/false
     */
    Boolean saveLinkMap(LinkMapDTO linkMapDTO);

    /**
     * 根据id查询链接
     *
     * @param id 链接id
     * @return 链接
     */
    LinkMap getLinkMapById(Long id);


    /**
     * 更新链接映射
     *
     * @param linkMap 链接映射，只允许更新备注，类型，邮箱、过期时间
     * @return true/false
     */
    Boolean updateLinkMapById(LinkMap linkMap);

    /**
     * 根据ids删除链接
     *
     * @param ids 链接ids
     * @return true/false
     */
    Boolean deleteLinkMapByIds(List<Long> ids);

    /**
     * 根据id获取链接访问量
     * 默认30天
     *
     * @param id 链接id
     * @return 访问量列表
     */
    List<VisitsVO> getVisits(Long id);

    /**
     * 根据id获取链接访问量
     *
     * @param id   链接id
     * @param days 天数
     * @return 访问量
     */
    List<VisitsVO> getVisits(Long id, long days);

    /**
     * 清空链接缓存
     */
    void clearLinkMapCache();

}
