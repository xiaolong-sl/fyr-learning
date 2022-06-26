package com.fyr.learning.commons.utils;

import com.fyr.learning.commons.repository.ConfigurationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class LocalCache {

    private static Logger LOGGER = LoggerFactory.getLogger(LocalCache.class);

    @Autowired
    private ConfigurationRepository configurationRepository;

    /**
     * 缓存最大个数
     */
    private static final Integer CACHE_CAPACITY = 100;
    /**
     * 当前缓存个数
     */
    private static Integer CURRENT_SIZE = 0;
    /**
     * 时间一分钟
     */
    static Long ONE_MINUTE = 1 * 60 * 1000L;
    /**
     * 静态缓存对象
     */
    private static final Map<String, String> CACHE_STATIC_MAP = new ConcurrentHashMap<>();
    /**
     * 动态缓存对象
     */
    private static final Map<String, Cache> CACHE_DYNAMIC_MAP = new ConcurrentHashMap<>();
    /**
     * 这个记录了缓存使用的最后一次的记录，最近使用的在最前面
     */
    private static final List<String> CACHE_USE_LOG_LIST = new LinkedList<>();
    /**
     * 清理过期缓存是否在运行
     */
    private static Boolean CLEAN_THREAD_IS_RUN = false;

    /**
     * 清理线程的
     */
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void init() {
        configurationRepository.findAll().stream().filter(x -> x.getIsActivated().equalsIgnoreCase("Y")).collect(Collectors.toList()).forEach(e -> CACHE_STATIC_MAP.put(e.getKey().toUpperCase(), e.getValue()));
    }

    /**
     * 设置缓存
     */
    public static void setCache(String cacheKey, Object cacheValue, long cacheTime) {
        Long ttlTime = null;
        if (cacheTime <= 0L) {
            if (cacheTime == -1L) {
                ttlTime = -1L;
            } else {
                return;
            }
        }
        checkSize();
        saveCacheUseLog(cacheKey);
        CURRENT_SIZE = CURRENT_SIZE + 1;
        if (ttlTime == null) {
            ttlTime = System.currentTimeMillis() + cacheTime;
        }
        Cache Cache = new Cache(cacheValue, ttlTime);
        CACHE_DYNAMIC_MAP.put(cacheKey, Cache);
        LOGGER.info("have set key :" + cacheKey);
    }

    /**
     * 设置缓存
     */
    public static void setCache(String cacheKey, Object cacheValue) {
        setCache(cacheKey, cacheValue, -1L);
    }

    /**
     * 获取缓存
     */
    public static Object getCache(String cacheKey) {
        startCleanThread();
        if (checkCache(cacheKey)) {
            saveCacheUseLog(cacheKey);
            return CACHE_DYNAMIC_MAP.get(cacheKey).getValue();
        }
        return null;
    }

    public static boolean isExist(String cacheKey) {
        return checkCache(cacheKey);
    }

    /**
     * 删除所有缓存
     */
    public static void clear() {
        LOGGER.info("have clean all key !");
        CACHE_DYNAMIC_MAP.clear();
        CURRENT_SIZE = 0;
    }

    /**
     * 删除某个缓存
     */
    public static void deleteCache(String cacheKey) {
        Object cacheValue = CACHE_DYNAMIC_MAP.remove(cacheKey);
        if (cacheValue != null) {
            LOGGER.info("have delete key :" + cacheKey);
            CURRENT_SIZE = CURRENT_SIZE - 1;
        }
    }

    /**
     * 判断缓存在不在,过没过期
     */
    private static boolean checkCache(String cacheKey) {
        Cache Cache = CACHE_DYNAMIC_MAP.get(cacheKey);
        if (Cache == null) {
            return false;
        }
        if (Cache.getTtl() == -1L) {
            return true;
        }
        if (Cache.getTtl() < System.currentTimeMillis()) {
            deleteCache(cacheKey);
            return false;
        }
        return true;
    }

    /**
     * 删除最近最久未使用的缓存
     */
    private static void deleteLRU() {
        LOGGER.info("delete Least recently used run!");
        String cacheKey = CACHE_USE_LOG_LIST.remove(CACHE_USE_LOG_LIST.size() - 1);
        deleteCache(cacheKey);
    }

    /**
     * 删除过期的缓存
     */
    static void deleteTimeOut() {
        LOGGER.info("delete time out run!");
        List<String> deleteKeyList = new LinkedList<>();
        for (Map.Entry<String, Cache> entry : CACHE_DYNAMIC_MAP.entrySet()) {
            if (entry.getValue().getTtl() < System.currentTimeMillis() && entry.getValue().getTtl() != -1L) {
                deleteKeyList.add(entry.getKey());
            }
        }

        for (String deleteKey : deleteKeyList) {
            deleteCache(deleteKey);
        }
        LOGGER.info("delete cache count is :" + deleteKeyList.size());

    }

    /**
     * 检查大小
     * 当当前大小如果已经达到最大大小
     * 首先删除过期缓存，如果过期缓存删除过后还是达到最大缓存数目
     * 删除最久未使用缓存
     */
    private static void checkSize() {
        if (CURRENT_SIZE >= CACHE_CAPACITY) {
            deleteTimeOut();
        }
        if (CURRENT_SIZE >= CACHE_CAPACITY) {
            deleteLRU();
        }
    }

    /**
     * 保存缓存的使用记录
     */
    private static synchronized void saveCacheUseLog(String cacheKey) {
        CACHE_USE_LOG_LIST.remove(cacheKey);
        CACHE_USE_LOG_LIST.add(0, cacheKey);
    }

    /**
     * 设置清理线程的运行状态为正在运行
     */
    static void setCleanThreadRun() {
        CLEAN_THREAD_IS_RUN = true;
    }

    /**
     * 开启清理过期缓存的线程
     */
    private static void startCleanThread() {
        if (!CLEAN_THREAD_IS_RUN) {
            executor.submit(new CleanExpiredCacheThread());
        }
    }

    public static void showUtilsInfo() {
        System.out.println("clean time out cache is run :" + CLEAN_THREAD_IS_RUN);
        System.out.println("cache max count is :" + CACHE_CAPACITY);
        System.out.println("cache current count is :" + CURRENT_SIZE);
        System.out.println("cache object map is :" + CACHE_DYNAMIC_MAP.toString());
        System.out.println("cache use log list is :" + CACHE_USE_LOG_LIST.toString());

    }

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(1 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LocalCache.setCache("my_cache_key_" + i, i, 60 * 1000);
        }

        for (int i = 0; i < 100; i++) {
            if (i > 10) {
                LocalCache.getCache("test");
            }
            try {
                Thread.sleep(1 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LocalCache.showUtilsInfo();
        }
    }
}
