create database if not exists MisakaNetworks
	DEFAULT CHARACTER SET utf8mb4;
use MisakaNetworks;

create table if not exists user
(
	id         int primary key auto_increment,
	name       varchar(25) default '' comment '用户名称，唯一',
	privateKey varchar(255) not null comment '私钥，注册时生成，用于验证密码',
	password   varchar(255) not null comment '加密后的密码',
	sessionId  varchar(255)
);
