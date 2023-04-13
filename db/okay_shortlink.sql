CREATE TABLE `link_map` (
                            `id` bigint(20) NOT NULL COMMENT '主键id',
                            `link` varchar(255) NOT NULL COMMENT '长链接',
                            `code` varchar(8) NOT NULL COMMENT '短链接',
                            `type` char(1) NOT NULL DEFAULT '0' COMMENT '类型',
                            `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '说明',
                            `expire_time` datetime NOT NULL COMMENT '过期时间',
                            `email` varchar(50) DEFAULT NULL COMMENT '通知邮件',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                            `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
                            `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_code` (`code`) USING BTREE COMMENT '短链接唯一索引',
                            UNIQUE KEY `uk_link` (`link`) USING BTREE COMMENT '长链接唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='短链接表';