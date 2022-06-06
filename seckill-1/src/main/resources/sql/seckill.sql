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

 Date: 01/05/2022 03:46:04
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
) ENGINE = InnoDB AUTO_INCREMENT = 1003 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '秒杀库存表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of seckill
-- ----------------------------
INSERT INTO `seckill` VALUES (1000, '1000元秒杀iphone6', 100, '2022-01-01 00:00:00', '2030-01-01 00:00:00', '2022-04-18 18:48:12');
INSERT INTO `seckill` VALUES (1001, '500元秒杀ipad2', 200, '2022-01-01 00:00:00', '2030-01-01 00:00:00', '2022-04-18 18:48:12');
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

-- ----------------------------
-- Procedure structure for execute_seckill
-- ----------------------------
DROP PROCEDURE IF EXISTS `execute_seckill`;
-- 定义分隔符为 ;;
delimiter ;;
CREATE PROCEDURE `execute_seckill`(in v_seckill_id bigint, in v_phone bigint, in v_kill_time timestamp, out r_result int)
    BEGIN
		DECLARE insert_count int DEFAULT 0;
		START TRANSACTION;
		insert ignore into success_killed (seckill_id, user_phone, create_time)
		values (v_seckill_id, v_phone, v_kill_time);
		-- row_count()：返回上一条修改类型 sql(delete,insert,update) 的影响行数
		-- 返回值：0：未修改数据；>0：表示修改的行数；<0：sql 错误 / 未执行修改的 sql
		select row_count() into insert_count;
		IF (insert_count = 0) THEN
            ROLLBACK;
            -- 重复秒杀
            set r_result = -1;
		ELSEIF (insert_count < 0) THEN
            ROLLBACK;
            set r_result = -2;
		ELSE
            update seckill set number = number - 1
            where seckill_id = v_seckill_id
                and end_time > v_kill_time
                and start_time < v_kill_time
                and number > 0;
            select row_count() into insert_count;
            If (insert_count = 0) THEN
                ROLLBACK;
                -- 秒杀结束
                set r_result = 0;
            ELSEIF (insert_count < 0) THEN
                ROLLBACK;
                set r_result = -2;
            ELSE
                COMMIT;
                -- 秒杀成功
                set r_result = 1;
            END IF;
		END IF;
	END
;;
-- 定义分隔符为 ;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;

-- 存储过程
-- 1：存储过程优化：事务行级锁持有的时间
-- 2：不要过度依赖存储过程
-- 3：简单的逻辑可以应用存储过程
-- 4：QPS：一个秒杀单接近6000/qps
