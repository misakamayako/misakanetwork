create table if not exists category
(
	id          int primary key auto_increment,
	description varchar(80) unique not null,
	type int not null comment '标签类型，暂定：1 文章，2：图片'
);
