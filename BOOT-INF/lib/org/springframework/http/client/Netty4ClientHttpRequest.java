/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufOutputStream;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.SimpleChannelInboundHandler;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.SettableListenableFuture;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Netty4ClientHttpRequest
/*     */   extends AbstractAsyncClientHttpRequest
/*     */   implements ClientHttpRequest
/*     */ {
/*     */   private final Bootstrap bootstrap;
/*     */   private final URI uri;
/*     */   private final org.springframework.http.HttpMethod method;
/*     */   private final ByteBufOutputStream body;
/*     */   
/*     */   public Netty4ClientHttpRequest(Bootstrap bootstrap, URI uri, org.springframework.http.HttpMethod method)
/*     */   {
/*  66 */     this.bootstrap = bootstrap;
/*  67 */     this.uri = uri;
/*  68 */     this.method = method;
/*  69 */     this.body = new ByteBufOutputStream(Unpooled.buffer(1024));
/*     */   }
/*     */   
/*     */ 
/*     */   public org.springframework.http.HttpMethod getMethod()
/*     */   {
/*  75 */     return this.method;
/*     */   }
/*     */   
/*     */   public URI getURI()
/*     */   {
/*  80 */     return this.uri;
/*     */   }
/*     */   
/*     */   public ClientHttpResponse execute() throws IOException
/*     */   {
/*     */     try {
/*  86 */       return (ClientHttpResponse)executeAsync().get();
/*     */     }
/*     */     catch (InterruptedException ex) {
/*  89 */       throw new IOException(ex.getMessage(), ex);
/*     */     }
/*     */     catch (ExecutionException ex) {
/*  92 */       if ((ex.getCause() instanceof IOException)) {
/*  93 */         throw ((IOException)ex.getCause());
/*     */       }
/*     */       
/*  96 */       throw new IOException(ex.getMessage(), ex.getCause());
/*     */     }
/*     */   }
/*     */   
/*     */   protected OutputStream getBodyInternal(org.springframework.http.HttpHeaders headers)
/*     */     throws IOException
/*     */   {
/* 103 */     return this.body;
/*     */   }
/*     */   
/*     */   protected ListenableFuture<ClientHttpResponse> executeInternal(final org.springframework.http.HttpHeaders headers) throws IOException
/*     */   {
/* 108 */     final SettableListenableFuture<ClientHttpResponse> responseFuture = new SettableListenableFuture();
/*     */     
/*     */ 
/* 111 */     ChannelFutureListener connectionListener = new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture future) throws Exception {
/* 114 */         if (future.isSuccess()) {
/* 115 */           Channel channel = future.channel();
/* 116 */           channel.pipeline().addLast(new ChannelHandler[] { new Netty4ClientHttpRequest.RequestExecuteHandler(responseFuture) });
/* 117 */           FullHttpRequest nettyRequest = Netty4ClientHttpRequest.this.createFullHttpRequest(headers);
/* 118 */           channel.writeAndFlush(nettyRequest);
/*     */         }
/*     */         else {
/* 121 */           responseFuture.setException(future.cause());
/*     */         }
/*     */         
/*     */       }
/* 125 */     };
/* 126 */     this.bootstrap.connect(this.uri.getHost(), getPort(this.uri)).addListener(connectionListener);
/* 127 */     return responseFuture;
/*     */   }
/*     */   
/*     */   private FullHttpRequest createFullHttpRequest(org.springframework.http.HttpHeaders headers)
/*     */   {
/* 132 */     io.netty.handler.codec.http.HttpMethod nettyMethod = io.netty.handler.codec.http.HttpMethod.valueOf(this.method.name());
/*     */     
/* 134 */     String authority = this.uri.getRawAuthority();
/* 135 */     String path = this.uri.toString().substring(this.uri.toString().indexOf(authority) + authority.length());
/*     */     
/* 137 */     FullHttpRequest nettyRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, nettyMethod, path, this.body.buffer());
/*     */     
/* 139 */     nettyRequest.headers().set("Host", this.uri.getHost() + ":" + getPort(this.uri));
/* 140 */     nettyRequest.headers().set("Connection", "close");
/* 141 */     for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
/* 142 */       nettyRequest.headers().add((String)entry.getKey(), (Iterable)entry.getValue());
/*     */     }
/* 144 */     if ((!nettyRequest.headers().contains("Content-Length")) && (this.body.buffer().readableBytes() > 0)) {
/* 145 */       nettyRequest.headers().set("Content-Length", Integer.valueOf(this.body.buffer().readableBytes()));
/*     */     }
/*     */     
/* 148 */     return nettyRequest;
/*     */   }
/*     */   
/*     */   private static int getPort(URI uri) {
/* 152 */     int port = uri.getPort();
/* 153 */     if (port == -1) {
/* 154 */       if ("http".equalsIgnoreCase(uri.getScheme())) {
/* 155 */         port = 80;
/*     */       }
/* 157 */       else if ("https".equalsIgnoreCase(uri.getScheme())) {
/* 158 */         port = 443;
/*     */       }
/*     */     }
/* 161 */     return port;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class RequestExecuteHandler
/*     */     extends SimpleChannelInboundHandler<FullHttpResponse>
/*     */   {
/*     */     private final SettableListenableFuture<ClientHttpResponse> responseFuture;
/*     */     
/*     */ 
/*     */     public RequestExecuteHandler(SettableListenableFuture<ClientHttpResponse> responseFuture)
/*     */     {
/* 173 */       this.responseFuture = responseFuture;
/*     */     }
/*     */     
/*     */     protected void channelRead0(ChannelHandlerContext context, FullHttpResponse response) throws Exception
/*     */     {
/* 178 */       this.responseFuture.set(new Netty4ClientHttpResponse(context, response));
/*     */     }
/*     */     
/*     */     public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception
/*     */     {
/* 183 */       this.responseFuture.setException(cause);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\Netty4ClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */