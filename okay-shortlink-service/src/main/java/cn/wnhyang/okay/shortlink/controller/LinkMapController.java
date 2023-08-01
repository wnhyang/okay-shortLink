package cn.wnhyang.okay.shortlink.controller;


import cn.wnhyang.okay.framework.common.pojo.CommonResult;
import cn.wnhyang.okay.framework.common.pojo.PageResult;
import cn.wnhyang.okay.shortlink.convert.linkmap.LinkMapConvert;
import cn.wnhyang.okay.shortlink.entity.LinkMapDO;
import cn.wnhyang.okay.shortlink.service.LinkMapService;
import cn.wnhyang.okay.shortlink.vo.Visits;
import cn.wnhyang.okay.shortlink.vo.linkmap.LinkMapCreateReqVO;
import cn.wnhyang.okay.shortlink.vo.linkmap.LinkMapPageReqVO;
import cn.wnhyang.okay.shortlink.vo.linkmap.LinkMapRespVO;
import cn.wnhyang.okay.shortlink.vo.linkmap.LinkMapUpdateReqVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static cn.wnhyang.okay.framework.common.pojo.CommonResult.success;

/**
 * 短链接
 *
 * @author wnhyang
 * @since 2023-02-21
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("shortlink/link-map")
public class LinkMapController {

    private final LinkMapService linkMapService;

    /**
     * 查看链接映射列表
     *
     * @return 链接映射列表
     */
    @GetMapping("/list")
    public CommonResult<List<LinkMapRespVO>> getLinkMapList() {
        List<LinkMapDO> list = linkMapService.getLinkMapList();
        return success(LinkMapConvert.INSTANCE.convertList(list));
    }

    /**
     * 分页查询链接映射列表
     *
     * @param reqVO 分页对象
     * @return 链接映射列表
     */
    @GetMapping("/page")
    public CommonResult<PageResult<LinkMapRespVO>> getLinkMapPage(LinkMapPageReqVO reqVO) {
        return success(LinkMapConvert.INSTANCE.convert(linkMapService.getLinkMapPage(reqVO)));
    }

    /**
     * 通过id查询链接映射
     *
     * @param id 链接id
     * @return R
     */
    @GetMapping("/get")
    public CommonResult<LinkMapRespVO> getById(@RequestParam("id") Long id) {
        return success(LinkMapConvert.INSTANCE.convert(linkMapService.getLinkMap(id)));
    }

    /**
     * 新增链接映射
     *
     * @param reqVO 链接
     * @return true/false
     */
    @PostMapping("/create")
    public CommonResult<Long> save(@Valid @RequestBody LinkMapCreateReqVO reqVO) {
        Long id = linkMapService.createLinkMap(reqVO);
        return success(id);
    }

    /**
     * 更新链接映射
     *
     * @param reqVO 链接
     * @return true/false
     */
    @PutMapping("/update")
    public CommonResult<Boolean> update(@Valid @RequestBody LinkMapUpdateReqVO reqVO) {
        linkMapService.updateLinkMap(reqVO);
        return success(true);
    }

    /**
     * 根据id删除链接映射
     *
     * @param id 链接id
     * @return R
     */
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteByIds(Long id) {
        linkMapService.deleteLinkMap(id);
        return success(true);
    }

    /**
     * 根据id查询链接访问量
     *
     * @param id 链接id
     * @return R
     */
    @GetMapping("/v/{id:\\d+}")
    public CommonResult<Visits> getVisits(@PathVariable Long id) {
        return success(linkMapService.getVisits(id));
    }

    /**
     * 清空链接缓存
     *
     * @return true
     */
    @DeleteMapping("/cache")
    public CommonResult<Boolean> clearLinkMapCache() {
        linkMapService.clearLinkMapCache();
        return success(true);
    }
}
