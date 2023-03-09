package org.okay4cloud.okay.shortlink.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.okay4cloud.okay.common.core.util.R;
import org.okay4cloud.okay.shortlink.api.entity.LinkMap;
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
        return R.ok(linkMapService.list());
    }

    /**
     * 分页查询链接映射列表
     *
     * @param page 分页对象
     * @return 链接映射列表
     */
    @GetMapping("/list")
    public R list(Page page) {
        return R.ok(linkMapService.page(page));
    }

    /**
     * 通过id查询链接映射
     *
     * @param linkMapId 链接id
     * @return R
     */
    @GetMapping("/{linkMapId}")
    public R<LinkMap> getById(@PathVariable Long linkMapId) {
        return R.ok(linkMapService.getById(linkMapId));
    }

    /**
     * 新增链接映射
     *
     * @param linkMap 链接
     * @return true/false
     */
    @PostMapping
    public R<Boolean> save(@Valid @RequestBody LinkMap linkMap) {
        LOGGER.debug("生成短链接{}", linkMap);
        return R.ok(linkMapService.saveLinkMap(linkMap));
    }

    /**
     * 更新链接映射
     *
     * @param linkMap 链接
     * @return true/false
     */
    @PutMapping
    public R<Boolean> update(@Valid @RequestBody LinkMap linkMap) {
        return R.ok(linkMapService.updateById(linkMap));
    }

    /**
     * 根据id删除链接映射
     *
     * @param linkMapId 短链接id
     * @return R
     */
    @DeleteMapping("/{linkMapId}")
    public R<Boolean> deleteById(@PathVariable Long linkMapId) {
        return R.ok(linkMapService.removeById(linkMapId));
    }
}
