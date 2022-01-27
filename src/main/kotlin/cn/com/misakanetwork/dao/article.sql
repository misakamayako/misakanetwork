create database if not exists MisakaNetworks
    DEFAULT CHARACTER SET utf8mb4;
use MisakaNetworks;
create table if not exists article_category
(
    id          int primary key auto_increment,
    description varchar(80) not null
);
create table if not exists article
(
    id       int primary key auto_increment,
    title    varchar(80)  not null,
    location varchar(120) not null
);
create table if not exists category_to_article(
    article int,
    category int,
    foreign key (article) references article(id),
    foreign key (category) references article_category(id)
);
