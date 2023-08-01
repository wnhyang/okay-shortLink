package cn.wnhyang.okay.shortlink.mapper;

import cn.wnhyang.okay.framework.common.pojo.PageResult;
import cn.wnhyang.okay.framework.mybatis.core.mapper.BaseMapperX;
import cn.wnhyang.okay.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.wnhyang.okay.shortlink.entity.LinkMapDO;
import cn.wnhyang.okay.shortlink.vo.linkmap.LinkMapPageReqVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Arrays;

/**
 * Mapper 接口
 *
 * @author wnhyang
 * @since 2023-02-21
 */
@Mapper
public interface LinkMapMapper extends BaseMapperX<LinkMapDO> {

    default PageResult<LinkMapDO> selectPage(LinkMapPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<LinkMapDO>()
                .eqIfPresent(LinkMapDO::getLink, reqVO.getLink())
                .eqIfPresent(LinkMapDO::getCode, reqVO.getCode())
                .eqIfPresent(LinkMapDO::getType, reqVO.getType())
                .orderByDesc(Arrays.asList(LinkMapDO::getType, LinkMapDO::getExpireTime)));
    }
}
