/**
 * @(#)LockingSample.java, 2015年10月22日. Copyright 2015 ss, Inc. All rights
 *                         reserved. ss PROPRIETARY/CONFIDENTIAL. Use is
 *                         subject to license terms.
 */
package org.zookeepersample.curator;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * @author yaoxm
 */
public class LockingSample {

    private static final int QTY = 5;

    private static final int REPETITIONS = QTY * 1;

    private static final String PATH = "/examples/locks";

    public static void main(String[] args) {
        final LimitedResource limitedResource = new LimitedResource();

        ExecutorService service = Executors.newFixedThreadPool(QTY);

        try {
            for (int i = 0; i < QTY; i++) {
                final int index = i;
                Callable<Void> task = new Callable<Void>() {

                    @Override
                    public Void call() throws Exception {
                        CuratorFramework client = CuratorFrameworkFactory
                                .newClient("myservice..myservice.com:2181",
                                        new ExponentialBackoffRetry(1000, 3));
                        try {
                            client.start();
                            
                            ZKLock lock = new ZKLock(client, PATH, limitedResource, "client " + index);
                            System.out.println("client " + index);
                            for (int j = 0; j < REPETITIONS; j++) {
                                lock.doWork(10, TimeUnit.SECONDS);
                            }
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            CloseableUtils.closeQuietly(client);
                        }
                        return null;
                    }
                };

                service.submit(task);
            }

            service.shutdown();
            service.awaitTermination(10, TimeUnit.MINUTES);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}

class LimitedResource {
    private final AtomicBoolean inUse = new AtomicBoolean(false);

    public void use() {
        if (!inUse.compareAndSet(false, true)) {
            throw new IllegalStateException("one user at a time!");
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            inUse.set(false);
        }
    }
}

class ZKLock {
    private final InterProcessMutex lock;

    private final LimitedResource limitedResource;

    private final String clientName;

    public ZKLock(CuratorFramework client, String lockPath,
            LimitedResource limitedResource, String clientName) {
        this.limitedResource = limitedResource;
        this.clientName = clientName;
        lock = new InterProcessMutex(client, lockPath);
    }

    public void doWork(long time, TimeUnit unit) throws Exception {
        if (!lock.acquire(time, unit)) {
            //throw new IllegalStateException(clientName
            //        + " could not acquire the lock");
            return;
        }

        try {
            System.out.println(clientName + " has the lock");
            limitedResource.use();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            System.out.println(clientName + " releasing the lock");
            lock.release(); // always release the lock in a finally block
        }
    }
}
