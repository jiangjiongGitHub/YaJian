/*
Navicat MySQL Data Transfer

Source Server         : 123.56.70.224_3306
Source Server Version : 50622
Source Host           : 123.56.70.224:3306
Source Database       : i_image

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2016-06-03 14:00:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `add_time` datetime DEFAULT NULL,
  `add_status` int(1) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `user_name_nick` varchar(255) DEFAULT NULL,
  `user_image` varchar(255) DEFAULT NULL,
  `user_telephone` varchar(255) DEFAULT NULL,
  `user_password` varchar(255) DEFAULT NULL,
  `user_level` int(11) DEFAULT NULL,
  `user_money` double(10,2) DEFAULT NULL,
  `login_count` int(11) DEFAULT NULL,
  `login_ip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='用户信息';

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '2016-05-11 15:03:02', '0', 'name', 'nickName', 'image', 'tel', 'pwd', '5', '9999.00', '9', '127.0.0.1');
INSERT INTO `t_user` VALUES ('2', '2016-06-03 13:58:48', '0', 'key_0', 'MD5', 'url', 'tel', '9a53cbcc7dbaf825c0f5309a57c63070', '1', '0.00', '1', '127.0.0.1');
INSERT INTO `t_user` VALUES ('3', '2016-06-03 13:58:48', '0', 'key_1', 'MD5', 'url', 'tel', 'bcc0f76ba3ff7262fd88be8c1728708f', '1', '0.00', '1', '127.0.0.1');
INSERT INTO `t_user` VALUES ('4', '2016-06-03 13:58:49', '0', 'key_2', 'MD5', 'url', 'tel', '24fd6a24d80aabe2116d80b6c3dc89e2', '1', '0.00', '1', '127.0.0.1');
INSERT INTO `t_user` VALUES ('5', '2016-06-03 13:58:49', '0', 'key_3', 'MD5', 'url', 'tel', '29e96a50c5e61e8f7ab39134d50e4b0b', '1', '0.00', '1', '127.0.0.1');
INSERT INTO `t_user` VALUES ('6', '2016-06-03 13:58:49', '0', 'key_4', 'MD5', 'url', 'tel', 'd261b2f59f6b454da23a5fd5444180c0', '1', '0.00', '1', '127.0.0.1');
INSERT INTO `t_user` VALUES ('7', '2016-06-03 13:58:49', '0', 'key_5', 'MD5', 'url', 'tel', '223e18b826c1df0f9c2421e873960f50', '1', '0.00', '1', '127.0.0.1');
INSERT INTO `t_user` VALUES ('8', '2016-06-03 13:58:49', '0', 'key_6', 'MD5', 'url', 'tel', 'a7bd61eb823e1f32694a6372fca93c07', '1', '0.00', '1', '127.0.0.1');
INSERT INTO `t_user` VALUES ('9', '2016-06-03 13:58:49', '0', 'key_7', 'MD5', 'url', 'tel', '45186a5b9ed6c5f908588a5e56382d5b', '1', '0.00', '1', '127.0.0.1');
INSERT INTO `t_user` VALUES ('10', '2016-06-03 13:58:49', '0', 'key_8', 'MD5', 'url', 'tel', '377570600c2f5cac5dd8e3c5afb03122', '1', '0.00', '1', '127.0.0.1');
INSERT INTO `t_user` VALUES ('11', '2016-06-03 13:58:50', '0', 'key_9', 'MD5', 'url', 'tel', '8089ed79c6a91779cbb3b406b229bdb0', '1', '0.00', '1', '127.0.0.1');
