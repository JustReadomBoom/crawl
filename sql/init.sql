DROP TABLE IF EXISTS `crawl`.`dfcf_record`;
CREATE TABLE `crawl`.`dfcf_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `stock_market` varchar(32) DEFAULT NULL COMMENT '股市名',
  `stock_rank` varchar(32) DEFAULT NULL COMMENT '排行榜名',
  `stock_code` varchar(64) DEFAULT NULL COMMENT '股票代码',
  `stock_name` varchar(64) DEFAULT NULL COMMENT '股票名字',
  `price_new` decimal(12,3) DEFAULT NULL COMMENT '股票最新价格',
  `stock_change` decimal(6,2) DEFAULT NULL COMMENT '涨跌幅',
  `process_date` date DEFAULT NULL COMMENT '处理日期',
  `main_net_inflow_amount` decimal(12,4) DEFAULT NULL COMMENT '主力净额,净流入,主力流入：超大单加大单买入成交额之和',
  `main_net_proportion` decimal(5,2) DEFAULT NULL COMMENT '主力净流入--净占比',
  `super_big_part_net_inFlow_amount` decimal(12,4) DEFAULT NULL COMMENT '超大单净额,净流入,超大单：大于等于50万股或者100万元的成交单',
  `super_big_part_net_proportion` decimal(5,2) DEFAULT NULL COMMENT '超大单净流入--净占比',
  `big_part_net_inFlow_amount` decimal(12,4) DEFAULT NULL COMMENT '大单净额,净流入,大单：大于等于10万股或者20万元且小于50万股和100万元的成交单',
  `big_part_net_proportion` decimal(5,2) DEFAULT NULL COMMENT '大单净流入--净占比',
  `middle_part_net_inFlow_amount` decimal(12,4) DEFAULT NULL COMMENT '中单净额,净流入,中单：大于等于2万股或者4万元且小于10万股和20万元的成交单',
  `middle_part_net_proportion` decimal(5,2) DEFAULT NULL COMMENT '中单净流入--净占比',
  `litter_part_net_inFlow_amount` decimal(12,4) DEFAULT NULL COMMENT '小单净额,净流入,小单：小于2万股和4万元的成交单',
  `litter_part_net_proportion` decimal(5,2) DEFAULT NULL COMMENT '小单净流入--净占比',
  `stock_page` int(3) DEFAULT NULL COMMENT '第几页',
  `count_time` datetime DEFAULT NULL COMMENT '统计数据时间',
  `someInfo` longtext COMMENT '扩展数据',
  `time_version` varchar(64) DEFAULT NULL COMMENT '批次号',
  `crawler_version` varchar(64) DEFAULT NULL COMMENT '抓取版本，规则：股市#排行榜#股市闭市时间',
  `crawl_count` int(3) DEFAULT NULL COMMENT '抓取次数',
  `c_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `u_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `IDX_PRO_DATE` (`process_date`) USING BTREE,
  UNIQUE KEY `IDX_PDATE_STOCK_CODE_MARKET` (`stock_code`,`stock_market`,`process_date`) USING BTREE,
  KEY `IDX_STOCK_NAME` (`stock_name`) USING BTREE,
  KEY `IDX_STOCK_MARKET` (`stock_market`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='东方财富-资金流向-个股资金流的数据';

DROP TABLE IF EXISTS `crawl`.`opt_record`;
CREATE TABLE `crawl`.`opt_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `channel_type` varchar(10) NOT NULL COMMENT '通道类型 DFCF-东方财富',
  `process_date` date NOT NULL COMMENT '处理日期',
  `status` char(1) DEFAULT NULL COMMENT '状态 P-处理中 S-完成 E-失败异常',
  `count` int(3) DEFAULT NULL COMMENT '操作次数',
  `c_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `u_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_CHANNEL_TYPE_PDATE` (`channel_type`, `process_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作记录表';


