package com.huawei.zs.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @ClassName ServiceTest
 * @Description 准备开启多线程调用转换方法
 * @Author 82428
 * @Date 2019/10/21 20:05
 * @Version 1.0
 */
public class ServiceTest5 {
    public static void main(String[] args) {
        System.out.println("主线程执行。。。");
        long oldTime = System.currentTimeMillis();
        final List<String> excelFileIds = new ArrayList<String>();
        excelFileIds.add("11111111111");
        excelFileIds.add("22222222222");
        excelFileIds.add("33333333333");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Future<Map<String, String>>> results = new ArrayList<Future<Map<String, String>>>();
        final CountDownLatch latch = new CountDownLatch(3);
        for (final String excelFileId : excelFileIds) {
            results.add(executorService.submit(new Callable<Map<String, String>>() {
                public Map<String, String> call() throws Exception {
                    System.out.println(Thread.currentThread().getName() + "子线程执行...");
                    MainMethod mainMethod = new MainMethod();
                    Map<String, String> result = mainMethod.getResult(excelFileId);
                    latch.countDown();
                    return result;
                }
            }));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Future<Map<String, String>> result : results) {
            if(result.isDone()){
                try {
                    System.out.println(result.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        long newTime = System.currentTimeMillis();
        System.out.println("共执行" + (newTime - oldTime) + "毫秒");
        executorService.shutdown();
    }
}
