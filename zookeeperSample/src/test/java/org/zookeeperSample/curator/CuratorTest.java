package org.zookeeperSample.curator;

import junit.framework.TestCase;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by yaoxm on 2016/3/13 0013.
 */
public class CuratorTest {

    CuratorFramework zkClient;
    PathChildrenCache pathChildrenCache;
    final String PATH = "/a/b/c";

    @Before
    public void init() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        zkClient = CuratorFrameworkFactory.newClient(
                "sss:2181", retryPolicy);
        zkClient.start();
    }

    @Test
    public void testCreateData() throws Exception {
        zkClient.create().forPath("/a", "a".getBytes());
        zkClient.create().forPath("/a/b", "a".getBytes());
        zkClient.create().forPath("/a/b/c", "a".getBytes());
    }

    @Test
    public void testCreateEphemeralData() throws Exception {
        zkClient.create().withMode(CreateMode.EPHEMERAL).forPath("/a/b/c/c2", "a".getBytes());
        Thread.sleep(10000);
    }

    @Test
    public void testDeleteData() throws Exception {
        zkClient.delete().guaranteed().forPath("/javatest");
    }

    @Test
    public void TestListener() throws Exception {
        pathChildrenCache = new PathChildrenCache(zkClient, PATH, true);
        pathChildrenCache.start();

        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()){
                    case CHILD_ADDED:
                        String node = ZKPaths.getNodeFromPath(event.getData().getPath());
                        System.out.println(node);
                        break;
                    case CHILD_REMOVED:
                        break;
                    case CHILD_UPDATED:
                        break;
                    case CONNECTION_LOST:
                        break;
                }
            }
        });

        Thread.sleep(100000);
    }

    @After
    public void destroy() {
        if (null != zkClient) {
            CloseableUtils.closeQuietly(zkClient);
        }
    }
}
