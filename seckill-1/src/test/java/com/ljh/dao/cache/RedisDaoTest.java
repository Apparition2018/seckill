package com.ljh.dao.cache;

import com.ljh.dao.SeckillDao;
import com.ljh.entity.Seckill;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class RedisDaoTest {

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testSeckill() {
        // get and put
        long id = 1001;
        Seckill seckill = redisDao.getSeckill(id);
        if (seckill == null) {
            seckill = seckillDao.queryById(id);
            if (seckill != null) {
                String result = redisDao.putSeckill(seckill);
                System.err.println(result);
                seckill = redisDao.getSeckill(id);
                System.err.println(seckill);
            }
        } else {
            System.err.println(seckill);
        }
    }
}
