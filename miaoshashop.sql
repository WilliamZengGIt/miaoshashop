/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : miaoshashop

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2020-08-31 18:36:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_order`
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '表Id',
  `out_trade_no` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一。',
  `out_wx_no` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '微信订单号',
  `total_fee` int(88) DEFAULT NULL COMMENT '订单总金额，单位为分.',
  `order_status` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '订单交易状态 4001 交易成功 4002交易失败 4003交易取消',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `product_type` int(1) DEFAULT NULL COMMENT '商品类型 0爆款 1普通',
  `pay_status` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '支付状态 5001 支付成功  5002 支付失败 5003支付异常',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `address_id` int(11) DEFAULT NULL COMMENT '选择的地址id',
  `physique_id` int(11) DEFAULT NULL COMMENT '用户体形id',
  `fk_company_id` int(11) DEFAULT NULL,
  `updated` datetime NOT NULL,
  `created` datetime NOT NULL,
  `product_count` int(11) DEFAULT NULL COMMENT '商品数量',
  `product_name` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `product_price` double(11,2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=878 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES ('873', null, null, null, null, '18', '1', '5002', '3', '1', null, null, '2020-01-17 17:40:37', '2020-01-17 17:40:37', '1', 'iPhone11', '5600.00');
INSERT INTO `t_order` VALUES ('874', null, null, null, null, '19', '1', '5002', '3', '1', null, null, '2020-01-17 17:48:51', '2020-01-17 17:48:51', '1', 'iPhone11Pro', '5000.00');
INSERT INTO `t_order` VALUES ('875', null, null, null, null, '20', '1', '5002', '3', '1', null, null, '2020-01-17 17:54:13', '2020-01-17 17:54:13', '1', 'iPhone12', '5000.00');
INSERT INTO `t_order` VALUES ('876', null, null, null, null, '21', '1', '5002', '3', '1', null, null, '2020-01-17 18:07:11', '2020-01-17 18:07:11', '1', 'iphone', '5000.00');
INSERT INTO `t_order` VALUES ('877', null, null, null, null, '22', '1', '5002', '3', '1', null, null, '2020-01-17 18:19:03', '2020-01-17 18:19:03', '1', 'iPhone13', '5000.00');

-- ----------------------------
-- Table structure for `t_order_miaosha`
-- ----------------------------
DROP TABLE IF EXISTS `t_order_miaosha`;
CREATE TABLE `t_order_miaosha` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '表Id',
  `product_id` int(11) NOT NULL COMMENT '商品id',
  `order_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `updated` datetime NOT NULL,
  `created` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_order_miaosha
-- ----------------------------
INSERT INTO `t_order_miaosha` VALUES ('1', '18', '873', '3', '2020-01-17 17:40:37', '2020-01-17 17:40:37');
INSERT INTO `t_order_miaosha` VALUES ('2', '19', '874', '3', '2020-01-17 17:48:51', '2020-01-17 17:48:51');
INSERT INTO `t_order_miaosha` VALUES ('3', '20', '875', '3', '2020-01-17 17:54:13', '2020-01-17 17:54:13');
INSERT INTO `t_order_miaosha` VALUES ('4', '21', '876', '3', '2020-01-17 18:07:11', '2020-01-17 18:07:11');
INSERT INTO `t_order_miaosha` VALUES ('5', '22', '877', '3', '2020-01-17 18:19:03', '2020-01-17 18:19:03');

-- ----------------------------
-- Table structure for `t_product`
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '表Id',
  `product_name` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名',
  `product_link` varchar(40) COLLATE utf8_bin DEFAULT NULL,
  `product_info` text COLLATE utf8_bin COMMENT '商品详情  富文本',
  `product_menu_id` int(11) DEFAULT NULL COMMENT '商品所属菜单Id',
  `product_price` double(11,2) DEFAULT '0.00' COMMENT '价格',
  `product_stock` int(11) DEFAULT NULL COMMENT '商品库存',
  `fk_company_id` int(11) DEFAULT NULL COMMENT '企业id',
  `updated` datetime NOT NULL COMMENT '更新时间',
  `created` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_product
-- ----------------------------

-- ----------------------------
-- Table structure for `t_product_miaosha`
-- ----------------------------
DROP TABLE IF EXISTS `t_product_miaosha`;
CREATE TABLE `t_product_miaosha` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '表Id',
  `product_name` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名',
  `product_img_link` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名',
  `product_info` text COLLATE utf8_bin COMMENT '商品详情  富文本',
  `product_menu_id` int(11) DEFAULT NULL COMMENT '商品所属菜单Id',
  `product_price` double(11,2) DEFAULT '0.00' COMMENT '价格',
  `product_stock` int(11) DEFAULT NULL COMMENT '商品库存',
  `fk_company_id` int(11) DEFAULT NULL COMMENT '企业id',
  `start_date` datetime NOT NULL COMMENT '秒杀开始时间',
  `end_date` datetime NOT NULL COMMENT '秒杀结束时间',
  `updated` datetime NOT NULL COMMENT '更新时间',
  `created` datetime NOT NULL COMMENT '创建时间',
  `miaosha_price` double(11,2) NOT NULL COMMENT '商品秒杀价',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_product_miaosha
-- ----------------------------
INSERT INTO `t_product_miaosha` VALUES ('17', 'iPhone11ProMax', 'https://img14.360buyimg.com/cms/jfs/t1/88897/31/9088/764005/5e0b5cb0E2ef4cdbb/2052e06e4c2677a8.jpg', null, null, '9599.00', '10', null, '2020-01-16 16:45:19', '2020-01-16 18:45:23', '2020-01-16 16:45:31', '2020-01-16 16:45:35', '5000.00');
INSERT INTO `t_product_miaosha` VALUES ('18', 'iPhone11', 'https://img12.360buyimg.com/cms/jfs/t1/53149/8/12631/316476/5d9c63c8Ee131c296/0829af4e67e6a17f.jpg', null, null, '5999.00', '10', null, '2020-01-16 18:23:58', '2020-01-17 18:24:04', '2020-01-16 18:24:09', '2020-01-16 18:24:12', '5600.00');
INSERT INTO `t_product_miaosha` VALUES ('19', 'iPhone11Pro', 'https://img11.360buyimg.com/n1/s450x450_jfs/t1/104970/21/10529/210031/5e202904E2ae8eab0/050846d825843a3a.jpg', null, null, '7999.00', '10', null, '2020-01-17 17:46:07', '2020-01-18 17:46:11', '2020-01-17 17:46:17', '2020-02-04 17:46:20', '5000.00');
INSERT INTO `t_product_miaosha` VALUES ('20', 'iPhone12', 'https://img11.360buyimg.com/n1/s450x450_jfs/t1/104970/21/10529/210031/5e202904E2ae8eab0/050846d825843a3a.jpg', null, null, '9999.00', '10', null, '2020-01-17 17:50:31', '2020-01-18 17:50:35', '2020-01-17 17:50:40', '2020-01-17 17:50:44', '5000.00');
INSERT INTO `t_product_miaosha` VALUES ('21', 'iphone', 'www.baidu.com', null, null, '10000.00', '10', null, '2020-01-17 18:02:57', '2020-01-18 18:03:02', '2020-01-17 18:03:06', '2020-01-18 18:03:11', '5000.00');
INSERT INTO `t_product_miaosha` VALUES ('22', 'iPhone13', 'www.baidu.com', null, null, '11111.00', '9', null, '2020-01-17 18:03:40', '2020-01-18 18:03:43', '2020-01-17 18:03:47', '2020-01-17 18:03:52', '5000.00');
INSERT INTO `t_product_miaosha` VALUES ('23', 'iphone14', 'www.baidu.com', null, null, '1111.00', '10', null, '2020-01-18 18:20:01', '2020-01-17 18:20:04', '2020-01-17 18:20:06', '2020-01-17 18:20:09', '5000.00');

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户表id',
  `user_name` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '用户名',
  `open_id` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '微信唯一标识',
  `user_role` int(4) DEFAULT NULL COMMENT '用户角色   。是否允许后台登陆，10001，普通用户 2，会员。10003，管理员',
  `user_pwd` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '用户密码',
  `user_tel` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT '用户手机号码。  用户后台管理登陆账号',
  `wx_name` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '微信名',
  `wx_avatarUrl` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '头像地址',
  `wx_gender` int(1) DEFAULT NULL COMMENT '性别  0位置 1男 2女',
  `wx_province` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '省份',
  `wx_city` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '城市',
  `wx_country` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '国家',
  `updated` datetime DEFAULT NULL COMMENT '更新时间',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `fk_company_id` int(11) DEFAULT NULL COMMENT '企业id',
  `user_salt` varchar(11) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '2', '2', '2', '2', '13750466451', null, null, null, null, null, null, '2020-01-15 12:16:05', '2020-01-15 12:16:11', null, '1');
INSERT INTO `t_user` VALUES ('3', null, null, null, 'b7797cce01b4b131b433b6acf4add449', '13750466450', null, null, null, null, null, null, '2020-01-15 13:31:16', '2020-01-15 13:31:16', null, '1a2b3c');
