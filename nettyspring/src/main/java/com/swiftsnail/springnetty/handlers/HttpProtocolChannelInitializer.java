package com.swiftsnail.springnetty.handlers;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yaoxm on 2016/2/29.
 */
@Component
@Qualifier("httpProtocolChannelInitializer")
public class HttpProtocolChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

//    @Setter
//    @Getter
//    @Autowired
//    private HttpRequestDecoder httpRequestDecoder;
//
//    @Setter
//    @Getter
//    @Autowired
//    private HttpResponseEncoder httpResponseEncoder;

//    @Setter
//    @Getter
//    @Autowired
//    private ServerHandler serverHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("http-decoder", new HttpRequestDecoder());
        pipeline.addLast("http-encoder", new HttpResponseEncoder());
        pipeline.addLast("serverHandler", new ServerHandler(executorService));
    }
}
