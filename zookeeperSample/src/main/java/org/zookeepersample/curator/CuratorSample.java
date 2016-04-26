/**
 * @(#)CuratorSample.java, 2015年10月22日. Copyright 2015 ss, Inc. All rights
 *                         reserved. ss PROPRIETARY/CONFIDENTIAL. Use is
 *                         subject to license terms.
 */
package org.zookeepersample.curator;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;

/**
 * @author yaoxm
 */
public class CuratorSample {

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(
                "sss:2181", retryPolicy);
        zkClient.start();

        // zkClient.create().forPath("/javatest","中文".getBytes());
        zkClient.setData().forPath("/javatest", "中文".getBytes());
        byte[] chinese = zkClient.getData().forPath("/javatest");
        System.out.println(new String(chinese));

        zkClient.create().withProtection()
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath("/javatest/seq", "hehe".getBytes());

        List<String> data = zkClient.getChildren().forPath("/javatest");
        for (String aa: data) {
            System.out.println(aa);
        }

        zkClient.getCuratorListenable().addListener(new CuratorListener() {
            
            @Override
            public void eventReceived(CuratorFramework client, CuratorEvent event)
                    throws Exception {
                System.out.println("fire" + event.getName());
            }
        });
        zkClient.setData().inBackground().forPath("/javatest/seq", "heheagain".getBytes());
        
        zkClient.delete().inBackground(new BackgroundCallback() {
            
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event)
                    throws Exception {
                System.out.println("delete " + event.getPath());                
            }
        }).forPath("/javatest/seq");
        
        //zkClient.delete().guaranteed().forPath("/javatest/seq");
        //zkClient.create().withMode(CreateMode.PERSISTENT).forPath("/javatest/nodeforwatcher");
        zkClient.getChildren().usingWatcher(new CuratorWatcher() {
            
            @Override
            public void process(WatchedEvent event) throws Exception {
                System.out.println(event.getPath());                
            }
        }).forPath("/javatest");
        
        //zkClient.close();
    }
}
