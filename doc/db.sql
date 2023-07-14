/*
 Navicat Premium Data Transfer

 Source Server         : aliyun heyi
 Source Server Type    : MySQL
 Source Server Version : 80025 (8.0.25)
 Source Host           : 8.141.236.100:3306
 Source Schema         : action

 Target Server Type    : MySQL
 Target Server Version : 80025 (8.0.25)
 File Encoding         : 65001

 Date: 14/07/2023 15:51:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for action
-- ----------------------------
DROP TABLE IF EXISTS `action`;
CREATE TABLE `action`  (
  `action_id` int NOT NULL COMMENT '动作ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '动作名',
  `limit_time` int NULL DEFAULT NULL COMMENT '动作体测时间限制',
  PRIMARY KEY (`action_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '动作表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test`  (
  `test_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '用户ID',
  `height` float NULL DEFAULT NULL COMMENT '身高(cm)',
  `weight` float NULL DEFAULT NULL COMMENT '体重(kg)',
  `vital_capacity` int NULL DEFAULT NULL COMMENT '肺活量(ml)',
  `standing_long_jump` float NULL DEFAULT NULL COMMENT '立定跳远(cm)',
  `sit_and_reach` float NULL DEFAULT NULL COMMENT '坐位体前屈(cm)',
  `pull_up` int NULL DEFAULT NULL COMMENT '引体向上',
  `sprint_50m` float NULL DEFAULT NULL COMMENT '50米跑(second)',
  `long_distance_run` float NULL DEFAULT NULL COMMENT '800或1000米(second)',
  `push_up` int NULL DEFAULT NULL COMMENT '俯卧撑',
  `sit_up` int NULL DEFAULT NULL COMMENT '仰卧起坐',
  `squat` int NULL DEFAULT NULL COMMENT '深蹲',
  `test_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '体测年份',
  PRIMARY KEY (`test_id`) USING BTREE,
  INDEX `test_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `test_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '体侧表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for train
-- ----------------------------
DROP TABLE IF EXISTS `train`;
CREATE TABLE `train`  (
  `user_id` int NOT NULL COMMENT '用户ID',
  `action_id` int NOT NULL COMMENT '动作ID',
  `time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运动时间',
  `practice_time` int NULL DEFAULT NULL COMMENT '运动持续时间(second)',
  `count` int NULL DEFAULT NULL COMMENT '运动计数(仅限计数运动)',
  `value` float NULL DEFAULT NULL COMMENT '运动数值(其它运动, 单位可能为cm, m..)',
  `practice_id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`practice_id`) USING BTREE,
  INDEX `practice_user`(`user_id` ASC) USING BTREE,
  INDEX `practice_action`(`action_id` ASC) USING BTREE,
  CONSTRAINT `practice_action` FOREIGN KEY (`action_id`) REFERENCES `action` (`action_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `practice_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '训练运动记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int NOT NULL COMMENT '用户ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `is_admin` int NULL DEFAULT NULL COMMENT '用户是否为管理员',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
