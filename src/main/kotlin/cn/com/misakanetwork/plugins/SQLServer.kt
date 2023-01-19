package cn.com.misakanetwork.plugins

import cn.com.misakanetwork.tools.AesEncrypto
import com.alibaba.druid.pool.DruidDataSource
import org.ktorm.database.Database
import org.ktorm.expression.SqlFormatter
import org.ktorm.support.mysql.MySqlDialect
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.system.exitProcess

val database: Database by InitSQL

object InitSQL : ReadOnlyProperty<Nothing?, Database> {
	@JvmField
	val password: String = AesEncrypto.decrypt("LDV1KU86fX3ahywIQxVggw==")
	private const val url =
		"jdbc:mysql://localhost:3306/misakanetworks?" +
				"useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC"
	private val SQLInstance: Database
	override fun getValue(thisRef: Nothing?, property: KProperty<*>) = SQLInstance

	init {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver")
		} catch (e: ClassNotFoundException) {
			e.printStackTrace()
			exitProcess(-1)
		}
		val dataSource = DruidDataSource()
		dataSource.url = url
		dataSource.username = "root"
		dataSource.password = password
		SQLInstance = Database.connect(dataSource, dialect = object : MySqlDialect() {
			override fun createSqlFormatter(database: Database, beautifySql: Boolean, indentSize: Int): SqlFormatter {
				return GroupConcatFormatter(database, beautifySql, indentSize)
			}
		})
	}
}
