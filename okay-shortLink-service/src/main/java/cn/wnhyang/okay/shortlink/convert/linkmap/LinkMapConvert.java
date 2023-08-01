package cn.wnhyang.okay.shortlink.convert.linkmap;

import cn.wnhyang.okay.framework.common.pojo.PageResult;
import cn.wnhyang.okay.shortlink.entity.LinkMapDO;
import cn.wnhyang.okay.shortlink.vo.linkmap.LinkMapCreateReqVO;
import cn.wnhyang.okay.shortlink.vo.linkmap.LinkMapRespVO;
import cn.wnhyang.okay.shortlink.vo.linkmap.LinkMapUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wnhyang
 * @date 2023/7/31
 **/
@Mapper
public interface LinkMapConvert {
    LinkMapConvert INSTANCE = Mappers.getMapper(LinkMapConvert.class);

    PageResult<LinkMapRespVO> convert(PageResult<LinkMapDO> page);

    LinkMapDO convert(LinkMapCreateReqVO reqVO);

    LinkMapDO convert(LinkMapUpdateReqVO reqVO);

    LinkMapRespVO convert(LinkMapDO linkMap);

    List<LinkMapRespVO> convertList(List<LinkMapDO> list);
}
