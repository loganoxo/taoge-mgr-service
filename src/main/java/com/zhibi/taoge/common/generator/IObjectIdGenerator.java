package com.zhibi.taoge.common.generator;

/**
 * 对象ID生成器 Created by RUANHANG on 2016/1/6.
 */
public interface IObjectIdGenerator {

    /**
     * 生成分布式ID
     *
     * @param key 业务key 用于区分不通业务
     * @return 返回生成的ID
     */
    long generateId(String key);
}
