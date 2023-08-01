package cn.wnhyang.okay.shortlink.service;

import cn.wnhyang.okay.framework.common.pojo.PageResult;
import cn.wnhyang.okay.shortlink.entity.LinkMapDO;
import cn.wnhyang.okay.shortlink.vo.Visits;
import cn.wnhyang.okay.shortlink.vo.linkmap.LinkMapCreateReqVO;
import cn.wnhyang.okay.shortlink.vo.linkmap.LinkMapPageReqVO;
import cn.wnhyang.okay.shortlink.vo.linkmap.LinkMapUpdateReqVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wnhyang
 * @since 2023-02-21
 */
public interface LinkMapService {
    /**
     * 重定向
     *
     * @param code 短链接
     * @return 长链接
     */
    String redirect(String code);

    /**
     * 根据id获取链接访问量
     * 默认30天
     *
     * @param id 链接id
     * @return 访问量列表
     */
    Visits getVisits(Long id);

    /**
     * 根据id获取链接访问量
     *
     * @param id   链接id
     * @param days 天数
     * @return 访问量
     */
    Visits getVisits(Long id, long days);

    /**
     * 清空链接缓存
     */
    void clearLinkMapCache();

    /**
     * 获取分页列表
     *
     * @param reqVO 分页请求
     * @return 分页列表
     */
    PageResult<LinkMapDO> getLinkMapPage(LinkMapPageReqVO reqVO);

    /**
     * 创建数据
     *
     * @param reqVO 创建数据
     * @return 编号
     */
    Long createLinkMap(LinkMapCreateReqVO reqVO);

    /**
     * 更新数据
     *
     * @param reqVO 更新数据 只允许更新备注，类型，邮箱、过期时间
     */
    void updateLinkMap(LinkMapUpdateReqVO reqVO);

    /**
     * 获取链接映射
     *
     * @param id id
     * @return 数据
     */
    LinkMapDO getLinkMap(Long id);

    /**
     * 获取列表
     *
     * @return 列表
     */
    List<LinkMapDO> getLinkMapList();

    /**
     * 删除数据
     *
     * @param id id
     */
    void deleteLinkMap(Long id);
}
