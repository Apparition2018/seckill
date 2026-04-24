package com.ljh.dao;

import com.ljh.entity.Seckill;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

/**
 * spring 和 junit 整合，junit 启动时加载 springIOC 容器
 * spring-test，junit
 */
@ExtendWith(SpringExtension.class)
// 告诉 junit spring 配置文件
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SeckillDaoTest {

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testQueryById() {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void testQueryAll() {
        seckillDao.queryAll(0, 100).forEach(System.out::println);
    }

    @Test
    public void testReduceNumber() {
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L, killTime);
        System.out.println(updateCount);
    }
}
