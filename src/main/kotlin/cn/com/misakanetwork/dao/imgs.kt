package cn.com.misakanetwork.dao

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface ImgTag : Entity<ImgTag> {
    val id: Int
    val text: String
}

interface ImgFolder : Entity<ImgFolder> {
    val id: Int
    val name: String
}

interface Img : Entity<Img> {
    val id: Int
    val name: String
    val location: String
    val belong: ImgFolder?
}

interface ImgFolderToTag : Entity<ImgFolderToTag> {
    val folder: ImgFolder
    val tag: ImgTag
}

interface ImgToTag : Entity<ImgToTag> {
    val img: Img
    val tag: ImgTag
}

object ImgTagsDAO : Table<ImgTag>("Img_Tag") {
    val id = int("id").primaryKey().bindTo { it.id }
    val tagText = varchar("tagText").bindTo { it.text }
}

object ImgFoldersDAO : Table<ImgFolder>("Img_Folder") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
}

object ImgDAO : Table<Img>("img") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val location = varchar("location").bindTo { it.location }
    val belong = int("belong").references(ImgFoldersDAO) { it.belong }
}

object ImgFolderToTagsDAO : Table<ImgFolderToTag>("Img_Folder_To_Tag") {
    val folderId = int("id").references(ImgFoldersDAO) { it.folder }
    val tagId = int("tagId").references(ImgTagsDAO) { it.tag }
}

object ImgToTagsDAO : Table<ImgToTag>("Img_To_Tag") {
    val imgId = int("imgId").references(ImgDAO) { it.img }
    val tagId = int("tagId").references(ImgTagsDAO){it.tag}
}
