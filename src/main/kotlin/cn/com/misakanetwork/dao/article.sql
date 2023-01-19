create database if not exists MisakaNetworks
	DEFAULT CHARACTER SET utf8mb4;
use MisakaNetworks;
create table if not exists article
(
	id       int primary key auto_increment,
	title    varchar(80) not null,
	brief    varchar(120),
	createAt DATETIME    not null
);
create table if not exists article_to_category
(
	article  int,
	category int,
	foreign key (article) references article (id),
	foreign key (category) references category (id)
);
