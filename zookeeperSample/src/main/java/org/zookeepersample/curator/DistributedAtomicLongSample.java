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
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.google.common.collect.Lists;

/**
 * @author yaoxm
 */
public class DistributedAtomicLongSample {
    private static final String PATH = "/examples/longcounter";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                "myservice..myservice.com:2188", new ExponentialBackoffRetry(
                        1000, 3));
        client.start();

        ExecutorService service = Executors.newFixedThreadPool(10);
        List<DistributedAtomicLong> list = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            final DistributedAtomicLong count = new DistributedAtomicLong(
                    client, PATH, new ExponentialBackoffRetry(10, 10));

            list.add(count);

            Callable<Void> task = new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    AtomicValue<Long> v = count.increment();

                    if (v.succeeded()) {
                        System.out.println("Increment: from " + v.preValue()
                                + " to " + v.postValue());
                    }

                    return null;
                }
            };
            service.submit(task);
        }

        service.shutdown();
        service.awaitTermination(10, TimeUnit.MINUTES);
    }

}
