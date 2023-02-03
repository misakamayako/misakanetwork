package cn.com.misakanetwork.plugins

import org.ktorm.database.Database
import org.ktorm.expression.ArgumentExpression
import org.ktorm.expression.ScalarExpression
import org.ktorm.expression.SqlExpression
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.SqlType
import org.ktorm.schema.TextSqlType
import org.ktorm.support.mysql.MySqlFormatter

data class GroupConcatExpression(
	val columns: List<ColumnDeclaring<*>>,
	val separator: String,
	override val sqlType: SqlType<String> = TextSqlType,
	override val isLeafNode: Boolean = false,
	override val extraProperties: Map<String, Any> = mapOf()
) : ScalarExpression<String>()

class GroupConcatFormatter(database: Database, beautifySql: Boolean, indentSize: Int) :
	MySqlFormatter(database, beautifySql, indentSize) {
	override fun visitUnknown(expr: SqlExpression): SqlExpression {
		if (expr is GroupConcatExpression) {
			write("GROUP_CONCAT(")
			if (expr.columns.any()) {
				visitExpressionList(expr.columns.map { it.asExpression() })
			} else {
				throw IllegalArgumentException()
			}
			if (expr.separator != ",") {
				write("SEPARATOR ")
				val expSep = ArgumentExpression(expr.separator, TextSqlType)
				visit(expSep)
			}
			removeLastBlank()
			write(") ")
			return expr
		} else {
			return super.visitUnknown(expr)
		}
	}
}

fun groupConcat(columns: List<ColumnDeclaring<*>>, separator: String = ","): GroupConcatExpression =
	GroupConcatExpression(columns, separator)

fun groupConcat(column: ColumnDeclaring<*>, separator: String = ","): GroupConcatExpression =
	GroupConcatExpression(listOf(column), separator)
