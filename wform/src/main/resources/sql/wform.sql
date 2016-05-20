CREATE DATABASE IF NOT EXISTS `wform`;
USE `wform`;

CREATE TABLE IF NOT EXISTS `t_fixed_form` (
  `id` varchar(50) DEFAULT NULL ,
  `xm` varchar(50) DEFAULT NULL,
  `lx` varchar(50) DEFAULT NULL
);

INSERT INTO `t_fixed_form` (`id`, `xm`, `lx`) VALUES
	('1', '哈哈', '1');


CREATE TABLE IF NOT EXISTS `t_fixed_form_10002` (
  `id` int(11) NOT NULL,
  `value` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `t_fixed_form_10002` (`id`, `value`) VALUES
	(1, '类型不明');

CREATE TABLE IF NOT EXISTS `t_form` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `table_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `t_form` (`id`, `version`, `name`, `title`, `table_name`) VALUES
	(1001, 1, 'firstForm', '什么', 't_fixed_form');

CREATE TABLE IF NOT EXISTS `t_form_component` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `render_type` int(11) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `display` varchar(50) DEFAULT NULL,
  `form_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `t_form_component` (`id`, `name`, `render_type`, `type`, `display`, `form_id`) VALUES (10001, 'mc', 1, 'text', '名称', 1001);
INSERT INTO `t_form_component` (`id`, `name`, `render_type`, `type`, `display`, `form_id`) VALUES (10002, 'lx', 2, 'select', '类型', 1001);

CREATE TABLE IF NOT EXISTS `t_form_form` (
  `id` int(11) NOT NULL,
  `cmp_id` varchar(50) DEFAULT NULL,
  `is_editable` int(11) DEFAULT NULL,
  `form_version` int(11) DEFAULT NULL,
  `row` int(11) DEFAULT NULL,
  `column` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `t_form_form` (`id`, `cmp_id`, `is_editable`, `form_version`, `row`, `column`) VALUES (1, '10001', 1, 1, 1, 1);
INSERT INTO `t_form_form` (`id`, `cmp_id`, `is_editable`, `form_version`, `row`, `column`) VALUES (2, '10002', 1, 1, 1, 2);


CREATE TABLE IF NOT EXISTS `t_form_list` (
  `id` int(11) NOT NULL,
  `cmp_id` int(11) DEFAULT NULL,
  `form_id` int(1) DEFAULT NULL,
  `form_version` int(3) DEFAULT NULL,
  `order` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `t_form_list` (`id`, `cmp_id`, `form_id`, `form_version`, `order`) VALUES(101, 10001, 1001, 1, 1);
INSERT INTO `t_form_list` (`id`, `cmp_id`, `form_id`, `form_version`, `order`) VALUES	(102, 10002, 1001, 1, 2);

CREATE TABLE IF NOT EXISTS `t_form_list_search` (
  `id` int(11) NOT NULL,
  `cmp_id` int(11) DEFAULT NULL,
  `is_editable` int(1) DEFAULT NULL,
  `list_id` int(1) DEFAULT NULL,
  `column` int(2) DEFAULT NULL,
  `row` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `t_form_list_search` (`id`, `cmp_id`, `is_editable`, `list_id`, `column`, `row`) VALUES (1, 1, 1, 101, 1, 1);
INSERT INTO `t_form_list_search` (`id`, `cmp_id`, `is_editable`, `list_id`, `column`, `row`) VALUES (2, 2, 1, 102, 1, 2);
