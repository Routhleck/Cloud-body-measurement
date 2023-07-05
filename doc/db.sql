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

 Date: 05/07/2023 11:59:59
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
  PRIMARY KEY (`action_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '动作表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of action
-- ----------------------------
INSERT INTO `action` VALUES (1, 'Running');
INSERT INTO `action` VALUES (2, 'Swimming');
INSERT INTO `action` VALUES (3, 'Cycling');
INSERT INTO `action` VALUES (4, 'Weightlifting');
INSERT INTO `action` VALUES (5, 'Yoga');
INSERT INTO `action` VALUES (6, 'Pilates');
INSERT INTO `action` VALUES (7, 'Basketball');
INSERT INTO `action` VALUES (8, 'Football');
INSERT INTO `action` VALUES (9, 'Tennis');
INSERT INTO `action` VALUES (10, 'Golf');

-- ----------------------------
-- Table structure for practice
-- ----------------------------
DROP TABLE IF EXISTS `practice`;
CREATE TABLE `practice`  (
  `user_id` int NOT NULL COMMENT '用户ID',
  `action_id` int NOT NULL COMMENT '动作ID',
  `time` datetime NULL DEFAULT NULL COMMENT '运动时间',
  `practice_time` int NULL DEFAULT NULL COMMENT '运动持续时间(second)',
  `count` int NULL DEFAULT NULL COMMENT '运动计数(仅限计数运动)',
  `value` float NULL DEFAULT NULL COMMENT '运动数值(其它运动, 单位可能为cm, m..)',
  INDEX `practice_user`(`user_id` ASC) USING BTREE,
  INDEX `practice_action`(`action_id` ASC) USING BTREE,
  CONSTRAINT `practice_action` FOREIGN KEY (`action_id`) REFERENCES `action` (`action_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `practice_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '训练运动记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of practice
-- ----------------------------
INSERT INTO `practice` VALUES (1, 1, '2023-07-04 09:00:00', 1800, NULL, NULL);
INSERT INTO `practice` VALUES (1, 2, '2023-07-03 15:30:00', 2400, NULL, NULL);
INSERT INTO `practice` VALUES (2, 3, '2023-07-05 12:00:00', 3600, NULL, 15);
INSERT INTO `practice` VALUES (2, 4, '2023-07-02 08:45:00', 1200, 20, NULL);
INSERT INTO `practice` VALUES (3, 5, '2023-07-04 17:15:00', 1500, NULL, NULL);
INSERT INTO `practice` VALUES (3, 6, '2023-07-03 14:30:00', 2700, 45, NULL);
INSERT INTO `practice` VALUES (5, 9, '2023-07-04 13:45:00', 3300, NULL, 75);
INSERT INTO `practice` VALUES (5, 10, '2023-07-03 19:45:00', 1800, 30, NULL);

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test`  (
  `user_id` int NOT NULL COMMENT '用户ID',
  `test_time` datetime NULL DEFAULT NULL COMMENT '体侧时间',
  `height` float NULL DEFAULT NULL COMMENT '身高(cm)',
  `weight` float NULL DEFAULT NULL COMMENT '体重(kg)',
  `vital_capacity` int NULL DEFAULT NULL COMMENT '肺活量(ml)',
  `standing_long_jump` float NULL DEFAULT NULL COMMENT '立定跳远(cm)',
  `sit_and_reach` float NULL DEFAULT NULL COMMENT '坐位体前屈(cm)',
  `pull_or_sit-up` int NULL DEFAULT NULL COMMENT '引体向上/仰卧起坐(time)',
  `50m` float NULL DEFAULT NULL COMMENT '50米跑(second)',
  `800_or_1000m` int NULL DEFAULT NULL COMMENT '800或1000米(second)',
  PRIMARY KEY (`user_id`) USING BTREE,
  CONSTRAINT `test_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '体侧表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test
-- ----------------------------
INSERT INTO `test` VALUES (1, '2023-07-02 09:00:00', 175, 70, 3500, 200, 30, 15, 7.5, 300);
INSERT INTO `test` VALUES (2, '2023-07-04 10:30:00', 180, 75, 3200, 190, 28, 20, 7, 310);
INSERT INTO `test` VALUES (3, '2023-07-03 14:15:00', 170, 68, 3400, 195, 31, 18, 7.2, 290);
INSERT INTO `test` VALUES (5, '2023-07-01 13:00:00', 178, 72, 3600, 205, 32, 19, 7.6, 310);
INSERT INTO `test` VALUES (6, '2023-07-03 17:30:00', 172, 66, 3300, 195, 30, 17, 7.3, 300);
INSERT INTO `test` VALUES (7, '2023-07-05 12:30:00', 190, 80, 3800, 220, 35, 14, 8, 330);
INSERT INTO `test` VALUES (8, '2023-07-01 09:15:00', 183, 78, 3700, 215, 34, 15, 7.9, 320);
INSERT INTO `test` VALUES (9, '2023-07-04 16:45:00', 176, 70, 3500, 200, 30, 16, 7.5, 310);
INSERT INTO `test` VALUES (10, '2023-07-03 14:45:00', 179, 74, 3600, 205, 32, 18, 7.7, 300);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int NOT NULL COMMENT '用户ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `isAdmin` int NULL DEFAULT NULL COMMENT '用户是否为管理员',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin', 1);
INSERT INTO `user` VALUES (2, 'John Doe', 'john123', 0);
INSERT INTO `user` VALUES (3, 'Jane Smith', 'jane456', 0);
INSERT INTO `user` VALUES (5, 'Alice Johnson', 'alice789', 0);
INSERT INTO `user` VALUES (6, 'Bob Williams', 'bob321', 0);
INSERT INTO `user` VALUES (7, 'Emily Davis', 'emily654', 0);
INSERT INTO `user` VALUES (8, 'Michael Brown', 'michael987', 0);
INSERT INTO `user` VALUES (9, 'Olivia Taylor', 'olivia123', 0);
INSERT INTO `user` VALUES (10, 'Sophia Anderson', 'sophia456', 0);
INSERT INTO `user` VALUES (11, 'David Wilson', 'david789', 0);

SET FOREIGN_KEY_CHECKS = 1;
