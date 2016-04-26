package com.swiftsnail.springnetty.config;

import com.swiftsnail.springnetty.handlers.HttpProtocolChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yaoxm on 2016/2/29.
 */
@Configuration
@ComponentScan("com.swiftsnail.springnetty")
@PropertySource("classpath:netty-server.properties")
public class SpringConfig {

    @Value("${boss.thread.count}")
    private int bossCount;

    @Value("${worker.thread.count}")
    private int workerCount;

    @Value("${tcp.port}")
    private int tcpPort;

    @Value("${so.keepalive}")
    private boolean keepAlive;

    @Value("${so.backlog}")
    private int backlog;

    @Autowired
    @Qualifier("httpProtocolChannelInitializer")
    private HttpProtocolChannelInitializer protocolInitializer;

    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(protocolInitializer);
        Map<ChannelOption<?>, Object> channelOptions = channelOptions();
        Set<ChannelOption<?>> keySet = channelOptions.keySet();
        for (ChannelOption option : keySet) {
            b.option(option, channelOptions.get(option));
        }
        return b;
    }

    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossCount);
    }

    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerCount);
    }

    @Bean(name = "inetSocketAddress")
    public InetSocketAddress inetSocketAddress() {
        return new InetSocketAddress(tcpPort);
    }

    @Bean(name = "channelOptions")
    public Map<ChannelOption<?>, Object> channelOptions() {
        Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
        options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
        options.put(ChannelOption.SO_BACKLOG, backlog);
        return options;
    }

//    @Bean(name = "httpRequestDecoder")
//    public HttpRequestDecoder httpRequestDecoder() {
//        return new HttpRequestDecoder();
//    }
//
//    @Bean(name = "httpResponseEncoder")
//    public HttpResponseEncoder httpResponseEncoder() {
//        return new HttpResponseEncoder();
//    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
