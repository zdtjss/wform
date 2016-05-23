/*
Navicat MariaDB Data Transfer

Source Server         : mariadb
Source Server Version : 100113
Source Host           : localhost:3306
Source Database       : wform

Target Server Type    : MariaDB
Target Server Version : 100113
File Encoding         : 65001

Date: 2016-05-23 18:16:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_fixed_form
-- ----------------------------
DROP TABLE IF EXISTS `t_fixed_form`;
CREATE TABLE `t_fixed_form` (
  `id` varchar(50) DEFAULT NULL,
  `mc` varchar(50) DEFAULT NULL,
  `lx` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_fixed_form
-- ----------------------------
INSERT INTO `t_fixed_form` VALUES ('1', '没有', '1');

-- ----------------------------
-- Table structure for t_fixed_form_10002
-- ----------------------------
DROP TABLE IF EXISTS `t_fixed_form_10002`;
CREATE TABLE `t_fixed_form_10002` (
  `id` int(11) NOT NULL,
  `value` varchar(50) DEFAULT NULL,
  `b_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_fixed_form_10002
-- ----------------------------
INSERT INTO `t_fixed_form_10002` VALUES ('1', '2', '1');

-- ----------------------------
-- Table structure for t_form
-- ----------------------------
DROP TABLE IF EXISTS `t_form`;
CREATE TABLE `t_form` (
  `id` int(11) NOT NULL,
  `version` int(3) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `table_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_form
-- ----------------------------
INSERT INTO `t_form` VALUES ('1001', '1', 'firstForm', '什么东西', 't_fixed_form');

-- ----------------------------
-- Table structure for t_form_component
-- ----------------------------
DROP TABLE IF EXISTS `t_form_component`;
CREATE TABLE `t_form_component` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `render_type` int(1) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `display` varchar(50) DEFAULT NULL,
  `form_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_form_component
-- ----------------------------
INSERT INTO `t_form_component` VALUES ('10001', 'mc', '1', 'text', '名称', '1001');
INSERT INTO `t_form_component` VALUES ('10002', 'lx', '2', 'select', '类型', '1001');
INSERT INTO `t_form_component` VALUES ('10003', 'bq', '3', 'label', '标签', '1001');

-- ----------------------------
-- Table structure for t_form_form
-- ----------------------------
DROP TABLE IF EXISTS `t_form_form`;
CREATE TABLE `t_form_form` (
  `id` int(11) NOT NULL,
  `cmp_id` int(11) DEFAULT NULL,
  `is_editable` int(1) DEFAULT NULL,
  `form_version` int(3) DEFAULT NULL,
  `row` int(2) DEFAULT NULL,
  `column` int(2) DEFAULT NULL,
  `column_span` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_form_form
-- ----------------------------
INSERT INTO `t_form_form` VALUES ('1', '10001', '1', '1', '2', '1', '1');
INSERT INTO `t_form_form` VALUES ('2', '10002', '1', '1', '3', '2', '1');
INSERT INTO `t_form_form` VALUES ('3', '10003', '0', '1', '1', '1', '2');

-- ----------------------------
-- Table structure for t_form_list
-- ----------------------------
DROP TABLE IF EXISTS `t_form_list`;
CREATE TABLE `t_form_list` (
  `id` int(11) NOT NULL,
  `cmp_id` int(11) DEFAULT NULL,
  `form_id` int(1) DEFAULT NULL,
  `form_version` int(3) DEFAULT NULL,
  `order` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of t_form_list
-- ----------------------------
INSERT INTO `t_form_list` VALUES ('101', '10001', '1001', '1', '1');
INSERT INTO `t_form_list` VALUES ('102', '10002', '1001', '1', '2');

-- ----------------------------
-- Table structure for t_form_list_search
-- ----------------------------
DROP TABLE IF EXISTS `t_form_list_search`;
CREATE TABLE `t_form_list_search` (
  `id` int(11) NOT NULL,
  `cmp_id` int(11) DEFAULT NULL,
  `is_editable` int(1) DEFAULT NULL,
  `list_id` int(11) DEFAULT NULL,
  `column` int(2) DEFAULT NULL,
  `row` int(2) DEFAULT NULL,
  `column_span` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of t_form_list_search
-- ----------------------------
INSERT INTO `t_form_list_search` VALUES ('1', '1', '1', '101', '1', '1', null);
INSERT INTO `t_form_list_search` VALUES ('2', '2', '1', '102', '1', '2', null);
