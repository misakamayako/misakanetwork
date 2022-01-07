package cn.com.misakanetwork.plugins

import com.alibaba.druid.pool.DruidDataSource
import org.ktorm.database.Database
import org.ktorm.expression.SqlFormatter
import org.ktorm.support.mysql.MySqlDialect
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

val database: Database by InitSQL

object InitSQL : ReadOnlyProperty<Nothing?, Database> {
    var password: String = ""//"gXbAtJ*i95Zz4yQ"
    private const val url =
        "jdbc:mysql://localhost:3306/misakanetworks?" +
                "useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC"
    private lateinit var SQLInstance: Database
    override fun getValue(thisRef: Nothing?, property: KProperty<*>): Database {
        if (::SQLInstance.isInitialized) {
            return SQLInstance
        }
        val scanner = Scanner(System.`in`)
        println("please input password of database gXbAtJ*i95Zz4yQ")
        password = scanner.nextLine()
        println("success")
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        val dataSource = DruidDataSource()
        dataSource.url = url
        dataSource.username = "root"
        dataSource.password = password
        SQLInstance = Database.connect(dataSource, dialect = object : MySqlDialect() {
            override fun createSqlFormatter(database: Database, beautifySql: Boolean, indentSize: Int): SqlFormatter {
                return CustomSqlFormatter(database, beautifySql, indentSize)
            }
        })
        return SQLInstance
    }
}
