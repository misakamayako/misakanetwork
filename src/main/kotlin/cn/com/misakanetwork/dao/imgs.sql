create database if not exists MisakaNetworks
    DEFAULT CHARACTER SET utf8mb4;
use MisakaNetworks;

create table if not exists Img_Tag
(
    id   int primary key auto_increment,
    tagText  varchar(60) unique not null
);
create table if not exists Img_Folder
(
    id   int primary key auto_increment,
    name varchar(80) unique not null
);
create table if not exists img(
    id int primary key auto_increment,
    name text,
    location text,
    belong int
);
create table if not exists Img_Folder_To_Tag(
    folderId int,
    tagId int,
    foreign key (folderId) references Img_Folder(id),
    foreign key (tagId) references Img_Tag(id) on delete CASCADE
);

create table if not exists Img_To_Tag(
    imgId int not null ,
    tagId int not null ,
    foreign key (imgId) references img(id) on delete CASCADE ,
    foreign key (tagId) references Img_Tag(id) on delete CASCADE
);
