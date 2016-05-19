-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        10.1.13-MariaDB - mariadb.org binary distribution
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.1.0.4867
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
-- 正在导出表  wform.t_form 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `t_form` DISABLE KEYS */;
INSERT INTO `t_form` (`id`, `version`, `name`, `title`) VALUES
	(1001, 1, 'firstForm', '什么东西');
/*!40000 ALTER TABLE `t_form` ENABLE KEYS */;

-- 正在导出表  wform.t_form_cmp 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `t_form_cmp` DISABLE KEYS */;
INSERT INTO `t_form_cmp` (`id`, `name`, `render_type`, `type`, `display`, `form_id`, `table_name`) VALUES
	(1, 'mc', 1, 'text', '名称', 1001, NULL),
	(2, 'lx', 2, 'select', '类型', 1001, NULL);
/*!40000 ALTER TABLE `t_form_cmp` ENABLE KEYS */;

-- 正在导出表  wform.t_form_cmp_edit 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `t_form_cmp_edit` DISABLE KEYS */;
INSERT INTO `t_form_cmp_edit` (`id`, `cmp_id`, `is_editable`, `form_version`) VALUES
	(1, '1', 1, 1),
	(2, '2', 1, 1);
/*!40000 ALTER TABLE `t_form_cmp_edit` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
