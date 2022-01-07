package cn.com.misakanetwork.plugins

import org.ktorm.database.Database
import org.ktorm.support.mysql.MySqlFormatter

class CustomSqlFormatter(database: Database, beautifySql: Boolean, indentSize: Int) :
    MySqlFormatter(database, beautifySql, indentSize) {

}
