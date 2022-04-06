package com.fyr.activiti.learning.constant;

/**
 * 每一分钟清理一次过期缓存
 */
public class CleanExpiredCacheThread implements Runnable {

    @Override
    public void run() {
        LocalCache.setCleanThreadRun();
        while (true) {
            System.out.println("clean thread run ");
            LocalCache.deleteTimeOut();
            try {
                Thread.sleep(LocalCache.ONE_MINUTE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
