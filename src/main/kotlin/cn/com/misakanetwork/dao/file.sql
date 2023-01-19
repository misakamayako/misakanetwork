create database if not exists MisakaNetworks
	DEFAULT CHARACTER SET utf8mb4;
use MisakaNetworks;
create table if not exists file_mapping
(
	id          int auto_increment primary key,
	eigenvalues varchar(64) not null unique,
	real_name   varchar(126) default null,
	create_at   datetime    not null,
	delete_flag bool         default false
);
alter table file_mapping
	add create_at datetime not null

