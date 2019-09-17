-- 2019.06.17------------start------------------------
alter table quality_task_detail add column is_standard varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '是否为内置规则，N-否，Y-是';
alter table tb_metadata  modify column path varchar(2048);
-- 2019.06.17------------end--------------------------


-- ------2019.07.02----------start------------
-- Table structure for mail_config
-- ----------------------------
DROP TABLE IF EXISTS `mail_config`;
CREATE TABLE `mail_config` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `host` varchar(255) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL COMMENT '邮箱用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '邮箱密码',
  `protocol` varchar(255) DEFAULT NULL COMMENT '协议名称',
  `from` varchar(255) DEFAULT NULL COMMENT '邮件来源',
  `enable_ssl` varchar(20) DEFAULT NULL COMMENT 'false-禁用，true-启用',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统邮箱服务器配置信息';
-- ------2019.07.02----------end------------

-- ------2019.07.11----------start----------
delete from `datagov`.`tb_metamodel` where `id` = 10;
-- ------2019.07.11----------end  ----------

-- ------2019.07.17----------start----------
alter table quality_task_log add column level varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '日志级别:INFO/WARN/ERROR';
-- ------2019.07.17----------end  ----------

-- ------2019.09.03----------start----------
DROP TABLE IF EXISTS `tb_datasource_layer`;
CREATE TABLE `tb_datasource_layer` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `datasource_id` bigint(20) DEFAULT NULL COMMENT '数据源id',
  `layer_id` bigint(20) DEFAULT NULL COMMENT '分层id',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='数据源-数据源分层中间表';

alter table tb_source_layer add column delete_flag varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '删除标记';
-- ------2019.09.03----------end  ----------