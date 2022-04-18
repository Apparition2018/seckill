/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : seckill

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 19/04/2022 02:48:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for seckill
-- ----------------------------
DROP TABLE IF EXISTS `seckill`;
CREATE TABLE `seckill`  (
  `seckill_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
  `name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
  `number` int(0) NOT NULL COMMENT '库存数量',
  `start_time` timestamp(0) NOT NULL COMMENT '秒杀开始时间',
  `end_time` timestamp(0) NOT NULL COMMENT '秒杀结束时间',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`) USING BTREE,
  INDEX `idx_start_time`(`start_time`) USING BTREE,
  INDEX `idx_end_time`(`end_time`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1004 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '秒杀库存表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of seckill
-- ----------------------------
INSERT INTO `seckill` VALUES (1000, '1000元秒杀iphone6', 100, '2015-11-01 00:00:00', '2015-11-02 00:00:00', '2022-04-18 18:48:12');
INSERT INTO `seckill` VALUES (1001, '500元秒杀ipad2', 200, '2015-11-01 00:00:00', '2015-11-02 00:00:00', '2022-04-18 18:48:12');
INSERT INTO `seckill` VALUES (1002, '300元秒杀小米4', 300, '2015-11-01 00:00:00', '2015-11-02 00:00:00', '2022-04-18 18:48:12');
INSERT INTO `seckill` VALUES (1003, '200元秒杀红米note', 400, '2015-11-01 00:00:00', '2015-11-02 00:00:00', '2022-04-18 18:48:12');

-- ----------------------------
-- Table structure for success_killed
-- ----------------------------
DROP TABLE IF EXISTS `success_killed`;
CREATE TABLE `success_killed`  (
  `seckill_id` bigint(0) NOT NULL COMMENT '秒杀商品id',
  `user_phone` bigint(0) NOT NULL COMMENT '用户手机号',
  `state` tinyint(0) NOT NULL DEFAULT -1 COMMENT '状态标示：-1：无效 0：成功 1：已付款 2：已发货',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`, `user_phone`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '秒杀成功明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of success_killed
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
