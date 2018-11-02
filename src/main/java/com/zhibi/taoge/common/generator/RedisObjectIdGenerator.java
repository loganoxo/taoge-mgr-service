package com.zhibi.taoge.common.generator;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Redis实现的分布式ID生成器 Created by RUANHANG on 2016/1/6.
 */
public class RedisObjectIdGenerator implements IObjectIdGenerator, IdentifierGenerator {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private RedisTemplate template;

    private Map<String, Queue<Object>> idLocalCache = Maps.newConcurrentMap();

    private int batchsize = 20;

    public void setTemplate(RedisTemplate template) {
        this.template = template;
    }

    public RedisTemplate getTemplate() {
        return template;
    }

    @Override
    public long generateId(final String key) {
        synchronized (this) {
            Queue<Object> queue = getIdQueue(key);
            Object poll = queue.poll();
            if (poll != null) {
                return (long) poll;
            } else {
//				logger.info("gen ids!!!");
                batchGeneraterIds(key, queue);
                return (long) queue.poll();
            }
        }
    }

    private Queue<Object> getIdQueue(String key) {
        Queue<Object> queue = idLocalCache.get(key);
        if (queue == null) {
//			logger.info("new queue");
            queue = new ConcurrentLinkedQueue<>();
            idLocalCache.put(key, queue);
        }
        return queue;
    }

    private void batchGeneraterIds(final String key, Queue<Object> queue) {

        List<Long> list = template.executePipelined((RedisCallback<Object>) connection -> {
            for (int i = 0; i < batchsize; i++) {
                connection.incr(key.getBytes());
            }
            return null;
        });
        if (list == null) {
            throw new RuntimeException("redis id generator error!list return null!!!!");
        }

        logger.info("ids:" + JSONObject.toJSONString(list));

        queue.addAll(list);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return generateId(object.getClass().getSimpleName());
    }
}
