/**
* Source Server Type    : MySQL
* Source Server AppInfo : 5.7.26
* Source Host           : 127.0.0.1:3306
* Source Schema         : jsp_netdisk
* FileOperation Encoding         : utf-8
* Date: 2021/4/6 22:26:19
*/

/*
* drop database if exists `jsp_netdisk`
*/

create database jsp_netdisk DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
use jsp_netdisk;

SET FOREIGN_KEY_CHECKS = 0;

/*
drop table if exists account_file

drop table if exists tb_directory

drop table if exists tb_account_group_info

drop table if exists dir_file

drop table if exists tb_file

drop table if exists tb_permission

drop table if exists tb_account
*/

/*==============================================================*/
/* Table: tb_account                                            */
/*==============================================================*/
create table tb_account
(
    `account_id`          bigint    not null PRIMARY KEY AUTO_INCREMENT comment '账号ID',
    `username`            char(32)  not null comment '用户名',
    `password_hash`       char(128) NOT NULL comment '账号密码的哈希值',
    `password_salt`       char(128) NOT NULL comment '账号密码的哈希值计算的佐料',
    `permission_id`       bigint    not null comment '账号权限ID',
    `register_time`       DATETIME  not null comment '注册时间，精确至秒',
    `register_time_in_ms` int       not null comment '注册时间之毫秒',
    `root_directory_id`   bigint comment '用户网盘的根目录',
    `email`               char(255) comment '账号绑定的电子邮箱',
    `gender`              char(1)   not null comment '用户性别',
    `account_status`      int       not null comment '账号状态代码',
    unique key index_username (username),
    unique key index_email (email),
    unique key boost_query_all (username, password_hash, password_salt,
                                permission_id, register_time, register_time_in_ms,
                                root_directory_id, email, gender, account_status)
) AUTO_INCREMENT = 10000
  ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

/*==============================================================*/
/* Table: tb_directory                                          */
/* 缺少很多File属性，后期版本迭代时补上。（文件夹本身也是一种特殊的文件）   */
/*==============================================================*/
create table tb_directory
(
    directory_id        bigint    not null PRIMARY KEY AUTO_INCREMENT comment '目录ID',
    parent_directory_id bigint comment '父目录ID',
    account_id          bigint    not null comment '目录所属账号',
    directory_name      char(255) not null comment '目录名称',
    foreign key (parent_directory_id) references tb_directory (directory_id) on delete cascade on update cascade,
    unique unique_dir (parent_directory_id, directory_name),
    unique boost_query_all (parent_directory_id, account_id, directory_name)
) AUTO_INCREMENT = 10000
  ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

/*==============================================================*/
/* Table: tb_permission                                         */
/*==============================================================*/
create table tb_permission
(
    permission_id          bigint not null PRIMARY KEY AUTO_INCREMENT comment '权限条目ID',
    upload_file            int    not null comment '上传文件权限的代码',
    download_file          int    not null comment '下载文件权限的代码',
    account_operation      int    not null comment '管理员权限的代码',
    account_info_operation int    not null default 0 comment '账号信息公开程度的代码',
    file_operation         int    not null comment '文件操作权限的代码',
    unique key least_waste (upload_file, download_file, account_operation, account_info_operation, file_operation)
) AUTO_INCREMENT = 10000
  ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

/*==============================================================*/
/* Table: tb_file                                               */
/*==============================================================*/
create table tb_file
(
    file_id          bigint    not null PRIMARY KEY AUTO_INCREMENT comment '文件ID',
    file_name        char(255) not null comment '文件名',
    file_size        bigint    not null comment '文件大小',
    file_type        bigint    not null comment '文件类型',
    file_status      int       not null comment '文件状态代码',
    file_upload_time DATETIME  not null comment '文件上传时间',
    file_hash_md5    char(32) comment '文件MD5哈希值',
    file_hash_sha256 char(64) comment '文件SHA256哈希值',
    unique key index_unique_file (file_size, file_hash_md5(32), file_hash_sha256(64)),
    unique key boost_query_all (file_name, file_size, file_type, file_status, file_upload_time, file_hash_md5,
                                file_hash_sha256)
) AUTO_INCREMENT = 10000
  ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;


/*==============================================================*/
/* Table: tb_account_group_info                                 */
/*==============================================================*/
create table tb_account_group_info
(
    group_admin_id  bigint not null comment '管理员的账号ID',
    group_member_id bigint not null comment '被管理员管理的账号ID',
    foreign key (group_admin_id) references tb_account (account_id) on delete cascade on update cascade,
    foreign key (group_member_id) references tb_account (account_id) on delete cascade on update cascade,
    unique key boost_query_all (group_admin_id, group_member_id)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;
#
# /*==============================================================*/
# /* Table: account_file                                          */
# /*==============================================================*/
# create table account_file
# (
#     account_id bigint not null comment '账号ID',
#     file_id    bigint not null comment '账号拥有的文件',
#     foreign key (file_id) references tb_file (file_id) on delete cascade on update cascade,
#     foreign key (account_id) references tb_account (account_id) on delete cascade on update cascade,
#     index index_file_id (file_id),
#     index index_account_id (account_id)
# ) ENGINE = InnoDB
#   CHARACTER SET = utf8mb4
#   COLLATE = utf8mb4_unicode_ci
#   ROW_FORMAT = Dynamic;


/*==============================================================*/
/* Table: dir_file                                              */
/*==============================================================*/
create table dir_file
(
    account_id            bigint    not null comment '账号ID',
    directory_id          bigint    not null comment '目录ID',
    file_id               bigint    not null comment '目录下的文件',
    user_custom_file_name char(255) not null comment '用户自定义的文件名',
    unique key unique_file (directory_id, user_custom_file_name),
    foreign key (file_id) references tb_file (file_id) on delete cascade on update cascade,
    foreign key (directory_id) references tb_directory (directory_id) on delete cascade on update cascade,
    foreign key (account_id) references tb_account (account_id) on delete cascade on update cascade,
    unique key boost_query_all (account_id, file_id, directory_id, user_custom_file_name)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

/*==============================================================*/
/* Table: account_session                                       */
/*==============================================================*/
create table account_session
(
    account_id bigint    not null comment '账号ID',
    session_id char(255) not null comment '会话ID',
    foreign key (account_id) references tb_account (account_id) on delete cascade on update cascade,
    unique key index_account_id (account_id),
    unique key index_session_id (session_id)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;


/* MySQL 5.7 定义单行数据最大 65535bytes，故除去email字段的255*4个字节，
   剩余 16128*4个字节用于其它字段
 */
/*==============================================================*/
/* Table: email_verification                                    */
/*==============================================================*/
create table email_verification
(
    `email`            char(255)      not null comment '账号绑定的电子邮箱',
    `verification_log` varchar(16128) not null comment '发送过的验证码都以JSON文本形式记录下来',
    unique key index_email (email)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

/**
 超级用户的账号权限
 */
INSERT INTO `tb_permission` (`permission_id`, `upload_file`, `download_file`, `account_operation`,
                             `account_info_operation`, `file_operation`)
VALUES ('1', '30', '30', '0', '0', '666');

/**
 新注册用户但邮箱未激活的账号权限
 */
INSERT INTO `tb_permission` (`permission_id`, `upload_file`, `download_file`, `account_operation`,
                             `account_info_operation`, `file_operation`)
VALUES ('2', '0', '0', '30', '0', '0');

/**
 新注册用户且邮箱已激活的账号权限（正式会员，普通用户）
 */
INSERT INTO `tb_permission` (`permission_id`, `upload_file`, `download_file`, `account_operation`,
                             `account_info_operation`, `file_operation`)
VALUES ('3', '10', '10', '20', '0', '600');


/**
 超级用户的账号
 */
INSERT INTO `tb_account` (`account_id`, username, `password_hash`,
                          `password_salt`,
                          `permission_id`, `root_directory_id`,
                          `register_time`, `register_time_in_ms`, `email`, `gender`, `account_status`)
VALUES ('1', 'root', 'bda02209db09767384dd86fee7f21cff',
        '7e1eaa80738a082f3783ec471f2b60fa21958447f537f812f7bbb9e4c2fbec3a791fe7f9be190db5e5944fb31830ea156ee87ec4bf51973f2da3f28ac3f01720',
        '1', NULL,
        '2021-01-08 00:00:00', '0', '1046539849@qq.com', '0', '1');

/**
 基本的目录结构
 */
INSERT INTO `tb_directory` (`directory_id`, `parent_directory_id`, `account_id`, `directory_name`)
VALUES ('2', NULL, '1', 'sys');
INSERT INTO `tb_directory` (`directory_id`, `parent_directory_id`, `account_id`, `directory_name`)
VALUES ('10', '2', '1', 'media');
INSERT INTO `tb_directory` (`directory_id`, `parent_directory_id`, `account_id`, `directory_name`)
VALUES ('20', NULL, '1', 'root');
INSERT INTO `tb_directory` (`directory_id`, `parent_directory_id`, `account_id`, `directory_name`)
VALUES ('21', '20', '1', 'home');
INSERT INTO `tb_directory` (`directory_id`, `parent_directory_id`, `account_id`, `directory_name`)
VALUES ('30', NULL, '1', 'data');
INSERT INTO `tb_directory` (`directory_id`, `parent_directory_id`, `account_id`, `directory_name`)
VALUES ('31', '30', '1', 'user');
INSERT INTO `tb_directory` (`directory_id`, `parent_directory_id`, `account_id`, `directory_name`)
VALUES ('40', '30', '1', 'file');
INSERT INTO `tb_directory` (`directory_id`, `parent_directory_id`, `account_id`, `directory_name`)
VALUES ('50', NULL, '1', 'blackhouse');


/**
  为账号表添加外键约束，如果其它表要删除记录，先检查账号表是否关联了这些记录。
  如果有关联，则不允许删除，只有先从账号表修改关联到其它记录，才能删除这些记录。
 */
alter table tb_account
    add foreign key (root_directory_id) references tb_directory (directory_id) on delete set null on update cascade;
alter table tb_account
    add foreign key (permission_id) references tb_permission (permission_id) on delete restrict on update cascade;
alter table tb_directory
    add foreign key (account_id) references tb_account (account_id) on delete cascade on update cascade;

/**
  默认新用户的根目录是 小黑屋 （id=50,directory_name=blackhouse)
 */

alter table tb_account
    alter column root_directory_id set default 50;

/**
 更新超级用户的根目录
 */
UPDATE tb_account
SET root_directory_id = NULL
WHERE account_id = 1;

