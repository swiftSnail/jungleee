/**
 * @(#)TransactionExample.java, 2015年10月22日. Copyright 2015 ss, Inc. All
 *                              rights reserved. ss PROPRIETARY/CONFIDENTIAL.
 *                              Use is subject to license terms.
 */
package org.zookeepersample.curator;

import java.util.Collection;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author yaoxm
 */
public class TransactionExample {

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString("myservice..myservice.com:2181")
                .retryPolicy(retryPolicy).connectionTimeoutMs(1000)
                .sessionTimeoutMs(1000).namespace("yaya").build();
//        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(
//                "myservice..myservice.com:2181", retryPolicy);
        zkClient.start();

        Collection<CuratorTransactionResult> results = zkClient.inTransaction()
                .create().forPath("/a/path", "some data".getBytes()).and().commit();
   /*             .setData().forPath("/another/path", "other data".getBytes())
                .and().delete().forPath("/yet/another/path").and().commit();*/

        for (CuratorTransactionResult result: results) {
            System.out.println(result.getForPath() + " - " + result.getType());
        }
        
        zkClient.close();
    }
}
