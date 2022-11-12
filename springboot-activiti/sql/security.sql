/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 127.0.0.1:3306
 Source Schema         : activiti

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 09/09/2022 15:06:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
                               `id` bigint(11) NOT NULL AUTO_INCREMENT,
                               `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                               `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                               `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                               `pid` bigint(11) NOT NULL,
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, '/user/common', 'common', NULL, 0);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
                         `id` bigint(11) NOT NULL AUTO_INCREMENT,
                         `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'USER');
INSERT INTO `role` VALUES (2, 'ADMIN');
INSERT INTO `role` VALUES (3, 'ACTIVITI_USER');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
                                    `id` int(11) NOT NULL AUTO_INCREMENT,
                                    `role_id` bigint(11) NOT NULL,
                                    `permission_id` bigint(11) NOT NULL,
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (1, 1, 1);
INSERT INTO `role_permission` VALUES (2, 2, 1);
INSERT INTO `role_permission` VALUES (3, 2, 2);
INSERT INTO `role_permission` VALUES (4, 3, 3);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
                         `id` bigint(11) NOT NULL AUTO_INCREMENT,
                         `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                         `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'user', '$2a$10$4zd/aj2BNJhuM5PIs5BupO8tiN2yikzP7JMzNaq1fXhcXUefWCOF2');
INSERT INTO `user` VALUES (2, 'admin', '$2a$10$4zd/aj2BNJhuM5PIs5BupO8tiN2yikzP7JMzNaq1fXhcXUefWCOF2');
INSERT INTO `user` VALUES (3, 'Jack', '$2a$10$4zd/aj2BNJhuM5PIs5BupO8tiN2yikzP7JMzNaq1fXhcXUefWCOF2');
INSERT INTO `user` VALUES (4, 'Marry', '$2a$10$4zd/aj2BNJhuM5PIs5BupO8tiN2yikzP7JMzNaq1fXhcXUefWCOF2');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
                              `id` int(11) NOT NULL AUTO_INCREMENT,
                              `user_id` bigint(11) NOT NULL,
                              `role_id` bigint(11) NOT NULL,
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 1);
INSERT INTO `user_role` VALUES (3, 2, 2);
INSERT INTO `user_role` VALUES (4, 2, 3);
INSERT INTO `user_role` VALUES (5, 3, 3);
INSERT INTO `user_role` VALUES (6, 4, 3);

SET FOREIGN_KEY_CHECKS = 1;
