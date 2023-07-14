package cn.com.misakanetwork.tools

import io.ktor.http.*

enum class IndexType {
    NULL,
    Empty,
    INDEX
}

/**
 * @param indexType   数组的格式化模式
 * 					- NULL: a=1&a=2&a=3
 * 					- Empty: a[]=1&a[]=2&a[]=3
 * 					- INDEX: a[1]=1&a[2]=2&a[3]=3
 */
fun Parameters.getAsList(source: String, indexType: IndexType = IndexType.INDEX): List<String>? {
    return when (indexType) {
        IndexType.NULL -> this.getAll(source)
        IndexType.Empty -> this.getAll("$source[]")
        IndexType.INDEX -> {
            val result = ArrayList<String>()
            var index = 0
            while (true) {
                this["$source[${index++}]"]?.also { result.add(it) } ?: break
            }
            if (result.isEmpty()) {
                null
            } else {
                result
            }
        }
    }
}
