create database if not exists MisakaNetworks
	DEFAULT CHARACTER SET utf8mb4;
use MisakaNetworks;

create table if not exists album
(
	id      int primary key auto_increment,
	title   varchar(20) not null,
	nsfw    bool default false,
	private bool default false
);

create table if not exists img
(
	id          int primary key auto_increment,
	eigenvalues varchar(64) not null unique,
	name        varchar(64) not null,
	nsfw        bool default false,
	private     bool default false,
	album       int  default null,
	foreign key (album) references album (id)
);
