package cn.com.misakanetwork.plugins

import redis.clients.jedis.JedisPool

val redisPool by lazy {
	JedisPool("127.0.0.1", 6379)
}
