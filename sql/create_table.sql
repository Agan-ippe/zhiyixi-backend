# 数据库初始化
# @author <a href="https://github.com/Agan-ippe">知莫</a>

-- 创建库
create database if not exists zhibi;
-- create database if not exists your_db;

-- 切换库
use zhibi;
-- use your_db;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    user_account  varchar(256)                           not null comment '账号',
    user_password varchar(512)                           not null comment '密码',
    user_name     varchar(256)                           null comment '用户昵称',
    user_avatar   varchar(1024)                          null comment '用户头像',
    user_role     varchar(256) default 'user'            not null comment '用户角色：user/admin',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete tinyint default 0 not null comment '是否删除，0-否，1-是',
    index idx_userAccount (user_account)
    ) comment '用户表' collate = utf8mb4_unicode_ci;

-- 数据分析表
create table if not exists chart
(
    id           bigint auto_increment comment 'id' primary key,
    user_id    bigint            null comment '创建用户ID',
    chart_name varchar(128)      null comment '图表名称',
    goal  		 text			null comment '分析目标',
    chart_data   text			null comment '图表数据',
    chart_type 	 varchar(128)	null comment '图表类型',
    gen_chart_info	text		null comment 'AI生成的图表信息',
    gen_chart_result	text	null comment 'AI生成的分析结论',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete  tinyint default 0 not null comment '是否删除，0-否，1-是'
    ) comment '数据分析表' collate = utf8mb4_unicode_ci;
