package seus;

import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RedisClient {
    public static void main(String[] args) {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接成功");
        // 获取数据并输出
        jedis.del("runoobkey","site-list");
        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            System.out.println(jedis.get(key));
        }
    }
}
