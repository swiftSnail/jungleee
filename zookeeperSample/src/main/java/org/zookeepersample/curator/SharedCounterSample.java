/**
 * @(#)SharedCounterSample.java, 2015年10月23日. Copyright 2015 ss, Inc. All
 *                               rights reserved. ss
 *                               PROPRIETARY/CONFIDENTIAL. Use is subject to
 *                               license terms.
 */
package org.zookeepersample.curator;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.framework.recipes.shared.SharedCountListener;
import org.apache.curator.framework.recipes.shared.SharedCountReader;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.google.common.collect.Lists;

/**
 * @author yaoxm
 */
public class SharedCounterSample {
    private static final String PATH = "/examples/counter";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                "myservice..myservice.com:2188", new ExponentialBackoffRetry(
                        1000, 3));
        client.start();

        SharedCount sharedCount = new SharedCount(client, PATH, 0);
        sharedCount.addListener(new SharedCountListener() {

            @Override
            public void stateChanged(CuratorFramework client,
                    ConnectionState newState) {
                System.out.println(newState.toString());
            }

            @Override
            public void countHasChanged(SharedCountReader sharedCount,
                    int newCount) throws Exception {
                System.out.println(newCount);
            }
        });
        sharedCount.start();

        ExecutorService service = Executors.newFixedThreadPool(10);
        List<SharedCount> list = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            final SharedCount count = new SharedCount(client, PATH, 0);

            count.addListener(new SharedCountListener() {

                @Override
                public void stateChanged(CuratorFramework client,
                        ConnectionState newState) {
                    System.out.println(newState.toString());
                }

                @Override
                public void countHasChanged(SharedCountReader sharedCount,
                        int newCount) throws Exception {
                    System.out.println(newCount);
                }
            });

            list.add(count);

            Callable<Void> task = new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    count.start();

                    //Thread.sleep(500);
                    while (!count.trySetCount(count.getVersionedValue(),
                            count.getCount() + 1)) {
                        Thread.sleep(100);
                    }

                    return null;
                }
            };
            service.submit(task);
        }

        service.shutdown();
        service.awaitTermination(10, TimeUnit.MINUTES);

        for (SharedCount count: list) {
            count.close();
        }
        sharedCount.close();
    }

}
