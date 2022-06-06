package com.ljh.service.impl;

import com.ljh.dao.SeckillDao;
import com.ljh.dao.SuccessKilledDao;
import com.ljh.dao.cache.RedisDao;
import com.ljh.dto.Exposer;
import com.ljh.dto.SeckillExecution;
import com.ljh.entity.Seckill;
import com.ljh.entity.SuccessKilled;
import com.ljh.enums.SeckillStatEnum;
import com.ljh.exception.RepeatKillException;
import com.ljh.exception.SeckillCloseException;
import com.ljh.exception.SeckillException;
import com.ljh.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.*;

@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    private final SeckillDao seckillDao;
    private final SuccessKilledDao successKilledDao;
    private final RedisDao redisDao;

    public SeckillServiceImpl(SeckillDao seckillDao, SuccessKilledDao successKilledDao, RedisDao redisDao) {
        this.seckillDao = seckillDao;
        this.successKilledDao = successKilledDao;
        this.redisDao = redisDao;
    }

    private static final String slat = "wfeojo23ij#$#^j20394230j0j11k2lm3k1;2n";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        /* 缓存优化 */
        // 1.访问 Redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            // 2.访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                // 3.放入 Redis
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime())
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        String md5 = this.getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    /**
     * MD5 加盐加密，不可逆
     */
    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    /**
     * 事务相关说明：
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短：不要穿插其他网络操作(RFC/HTTP 请求)，或者将其剥离到事务方法外
     * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
     */
    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException {
        if (md5 == null || !md5.equals(this.getMD5(seckillId)))
            throw new SeckillException("seckill data rewrite");
        /* 执行秒杀逻辑：①减库存；②记录购买行为 */
        Date nowTime = new Date();
        try {
            // 记录购买行为，唯一 (seckill, userPhone)
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                // 重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                // 减库存，热点商品竞争（尽量后执行）
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    // 没有更新到记录，秒杀结束
                    throw new SeckillCloseException("seckill closed");
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException | RepeatKillException ex) {
            throw ex;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error" + e.getMessage());
        }
    }

    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) throws SeckillException {
        if (md5 == null || !md5.equals(getMD5(seckillId)))
            return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        try {
            seckillDao.killByProcedure(map);
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, sk);
            } else {
                return new SeckillExecution(seckillId, Objects.requireNonNull(SeckillStatEnum.stateOf(result)));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, Objects.requireNonNull(SeckillStatEnum.INNER_ERROR));
        }
    }
}
