package com.swiftsnail.probe.server;

import com.swiftsnail.avro.probe.service.MService;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificResponder;

import java.net.InetSocketAddress;

/**
 * Created by yaoxm on 2016/2/20 0020.
 */
public class AvroServer {

    public static void main(String[] args) {
        Server nettyServer = new NettyServer(new SpecificResponder(MService.class,
                new MServiceImpl()), new InetSocketAddress(8888));

        System.out.println("server started!");
    }
}
