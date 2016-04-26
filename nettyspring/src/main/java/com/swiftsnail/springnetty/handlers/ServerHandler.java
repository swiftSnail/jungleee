package com.swiftsnail.springnetty.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * Created by yaoxm on 2016/2/29.
 */
//@Component
//@Qualifier("serverHandler")
//@ChannelHandler.Sharable
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private ExecutorService executorService;

    public ServerHandler(@NonNull ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {
//        log.info(httpObject.toString());
        log.info(Thread.currentThread().getName() + "get request");
        //mock obese work
        doJob(ctx);

    }

    private void writeResponse(ChannelHandlerContext ctx, String content) throws UnsupportedEncodingException {
        if (ctx.channel().isWritable()) {
            ByteBuf contentBB = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
            FullHttpResponse httpResponse = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    contentBB);

            if (null != httpResponse.content()) {
                httpResponse.headers().set(HttpHeaders.Names.ACCEPT_CHARSET, "UTF-8");
                httpResponse.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
                httpResponse.headers().set(HttpHeaders.Names.CONTENT_LENGTH, httpResponse.content().readableBytes());
                httpResponse.headers().set("long", "1000L");
                httpResponse.headers().set("b", "english hehe");
                httpResponse.headers().set("c", URLEncoder.encode("我是中文", "UTF-8"));
            } else {
                httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            }

            ctx.write(httpResponse).addListener(ChannelFutureListener.CLOSE);
            ctx.flush();
        }
    }

    private void doJob(ChannelHandlerContext ctx) throws ExecutionException, InterruptedException {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5);
                    writeResponse(ctx, "我是中文");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
