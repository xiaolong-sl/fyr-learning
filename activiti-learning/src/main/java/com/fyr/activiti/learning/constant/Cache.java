package com.fyr.activiti.learning.constant;

import java.io.Serializable;

public class Cache implements Serializable {

    /**
     * 缓存对象
     */
    private Object value;
    /**
     * 缓存过期时间
     */
    private Long ttl;

    Cache(Object value, Long ttlTime) {
        this.value = value;
        this.ttl = ttlTime;
    }

    Object getValue() {
        return value;
    }

    Long getTtl() {
        return ttl;
    }

    @Override
    public String toString() {
        return "LocalCache{" +
                "value=" + value +
                ", ttl=" + ttl +
                '}';
    }
}
