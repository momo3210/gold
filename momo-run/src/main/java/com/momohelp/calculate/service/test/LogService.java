package com.momohelp.calculate.service.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogService {
 
    private final BlockingQueue<String> queue;
    private final LogThread logThread = new LogThread();
    private final PrintWriter printWriter;
 
    private boolean isShutdown = false;
    private int reservations;
 
    public LogService(BlockingQueue<String> queue, PrintWriter printWriter) {
        this.queue = queue;
        this.printWriter = printWriter;
    }
 
    public void start() {
        logThread.start();
    }
 
    public void stop() {
        synchronized (this) {
            isShutdown = true;
        }
        //Interrupts this thread.
        logThread.interrupt();
    }
 
    public void log(String msg) throws InterruptedException {
        synchronized (this) {
            if (isShutdown) {
                throw new IllegalStateException("log service stop!!");
            }
            ++reservations;
        }
        queue.put(msg);
    }
 
 
    /**
     * 该线程类是做打印日志用的
     */
    private class LogThread extends Thread {
        public void run() {
            try {
                while (true) {
                    synchronized (LogService.this) {
                        if (isShutdown && reservations == 0) {
                            break;
                        }
                    }
 
                    try {
                        String msg = queue.take();
                        synchronized (LogService.this) {
                            --reservations;
                        }
                        printWriter.println(msg);
                        printWriter.flush();
                    } catch (InterruptedException e) {
                        //retry
                    	Thread.currentThread().interrupt();
                    }
                }
            } finally {
                printWriter.close();
            }
        }
    }
 
 
    /**
     * 测试日期服务
     * 该日志服务使用生产者和消费者模式
     * 并且可以可靠的取消操作
     *
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String args[]) throws FileNotFoundException {
 
        //使用阻塞队列
        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
 
 
        //把日志写入指定文件
        File file = new File("C:\\test.log");
        PrintWriter p = new PrintWriter(file);
 
        final LogService logService = new LogService(queue, p);
 
        //开启日志服务
        logService.start();
 
        ExecutorService service = Executors.newCachedThreadPool();
 
        //使用多线程调用日志服务
        for (int n = 0; n < 100; n++) {
            final int finalN = n;
            service.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        logService.log("thread-" + finalN + " use log");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
 
        //logService.stop();//使用stop方法停止日志服务
    }
}