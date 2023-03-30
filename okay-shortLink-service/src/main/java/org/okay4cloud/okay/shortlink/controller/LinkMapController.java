package org.okay4cloud.okay.shortlink.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.okay4cloud.okay.common.core.util.R;
import org.okay4cloud.okay.shortlink.api.dto.LinkMapDTO;
import org.okay4cloud.okay.shortlink.api.entity.LinkMap;
import org.okay4cloud.okay.shortlink.api.vo.Visits;
import org.okay4cloud.okay.shortlink.service.LinkMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 短链接
 *
 * @author wnhyang
 * @since 2023-02-21
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/linkMap")
public class LinkMapController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkMapController.class);

    private final LinkMapService linkMapService;

    /**
     * 查看链接映射列表
     *
     * @return 链接映射列表
     */
    @GetMapping("/all")
    public R<List<LinkMap>> all() {
        LOGGER.info("查询全部数据。。。");
        return R.ok(linkMapService.list());
    }

    /**
     * 分页查询链接映射列表
     *
     * @param page 分页对象
     * @return 链接映射列表
     */
    @GetMapping("/list")
    public R<IPage<LinkMap>> list(Page page, LinkMap linkMap) {
        LOGGER.info("分页查询 size {},current {},linkMap {}", page.getSize(), page.getCurrent(), linkMap);
        return R.ok(linkMapService.page(page, Wrappers.query(linkMap)));
    }

    /**
     * 通过id查询链接映射
     *
     * @param id 链接id
     * @return R
     */
    @GetMapping("/{id:\\d+}")
    public R<LinkMap> getById(@PathVariable Long id) {
        LOGGER.info("查询短链接id {}", id);
        return R.ok(linkMapService.getLinkMapById(id));
    }

    /**
     * 新增链接映射
     *
     * @param linkMapDTO 链接
     * @return true/false
     */
    @PostMapping
    public R<Boolean> save(@Valid @RequestBody LinkMapDTO linkMapDTO) {
        LOGGER.info("生成短链接 {}", linkMapDTO);
        return R.ok(linkMapService.saveLinkMap(linkMapDTO));
    }

    /**
     * 更新链接映射
     *
     * @param linkMap 链接
     * @return true/false
     */
    @PutMapping
    public R<Boolean> update(@Valid @RequestBody LinkMap linkMap) {
        LOGGER.info("更新链接 {}", linkMap);
        return R.ok(linkMapService.updateLinkMapById(linkMap));
    }

    /**
     * 根据id删除链接映射
     *
     * @param ids 链接ids
     * @return R
     */
    @DeleteMapping("/{ids}")
    public R<Boolean> deleteByIds(@PathVariable List<Long> ids) {
        LOGGER.info("删除链接ids {}", ids.toString());
        return R.ok(linkMapService.deleteLinkMapByIds(ids));
    }

    /**
     * 根据id查询链接访问量
     *
     * @param id 链接id
     * @return R
     */
    @GetMapping("/v/{id:\\d+}")
    public R<Visits> getVisits(@PathVariable Long id) {
        LOGGER.info("查看链接访问量 {}", id);
        return R.ok(linkMapService.getVisits(id));
    }

    /**
     * 清空链接缓存
     *
     * @return true
     */
    @DeleteMapping("/cache")
    public R<Boolean> clearLinkMapCache() {
        LOGGER.info("清空链接缓存。。。");
        linkMapService.clearLinkMapCache();
        return R.ok(true);
    }
}
