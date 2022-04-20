package com.ljh.service;

import com.ljh.dto.Exposer;
import com.ljh.dto.SeckillExecution;
import com.ljh.entity.Seckill;
import com.ljh.exception.RepeatKillException;
import com.ljh.exception.SeckillCloseException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        log.info("list={}", list);
    }

    @Test
    public void testGetById() {
        long id = 1000;
        Seckill seckill = seckillService.getById(id);
        log.info("seckill={}", seckill);
    }

    /**
     * 1.seckillService.exportSeckillUrl(id)
     * 2.seckillService.executeSeckill(id, phone, md5)
     * 集成测试代码完整逻辑，注意可重复执行
     */
    @Test
    public void testSeckillLogic() {
        long id = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            log.info("expose={}", exposer);
            long phone = 13502171127L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
                log.info("result={}", execution);
            } catch (SeckillCloseException | RepeatKillException e) {
                log.error(e.getMessage());
            }
        } else {
            // 秒杀未开启
            log.warn("expose={}", exposer);
        }
    }

    @Test
    public void testExecuteSeckillProcedure() {
        long seckillId = 1001;
        long phone = 13680111023L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            log.info(execution.getStateInfo());
        }
    }
}