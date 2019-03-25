/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import io.netty.buffer.ByteBufInputStream;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.http.FullHttpResponse;
/*    */ import io.netty.handler.codec.http.HttpResponseStatus;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Map.Entry;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Netty4ClientHttpResponse
/*    */   extends AbstractClientHttpResponse
/*    */ {
/*    */   private final ChannelHandlerContext context;
/*    */   private final FullHttpResponse nettyResponse;
/*    */   private final ByteBufInputStream body;
/*    */   private volatile HttpHeaders headers;
/*    */   
/*    */   public Netty4ClientHttpResponse(ChannelHandlerContext context, FullHttpResponse nettyResponse)
/*    */   {
/* 48 */     Assert.notNull(context, "ChannelHandlerContext must not be null");
/* 49 */     Assert.notNull(nettyResponse, "FullHttpResponse must not be null");
/* 50 */     this.context = context;
/* 51 */     this.nettyResponse = nettyResponse;
/* 52 */     this.body = new ByteBufInputStream(this.nettyResponse.content());
/* 53 */     this.nettyResponse.retain();
/*    */   }
/*    */   
/*    */ 
/*    */   public int getRawStatusCode()
/*    */     throws IOException
/*    */   {
/* 60 */     return this.nettyResponse.getStatus().code();
/*    */   }
/*    */   
/*    */   public String getStatusText()
/*    */     throws IOException
/*    */   {
/* 66 */     return this.nettyResponse.getStatus().reasonPhrase();
/*    */   }
/*    */   
/*    */   public HttpHeaders getHeaders()
/*    */   {
/* 71 */     if (this.headers == null) {
/* 72 */       HttpHeaders headers = new HttpHeaders();
/* 73 */       for (Map.Entry<String, String> entry : this.nettyResponse.headers()) {
/* 74 */         headers.add((String)entry.getKey(), (String)entry.getValue());
/*    */       }
/* 76 */       this.headers = headers;
/*    */     }
/* 78 */     return this.headers;
/*    */   }
/*    */   
/*    */   public InputStream getBody() throws IOException
/*    */   {
/* 83 */     return this.body;
/*    */   }
/*    */   
/*    */   public void close()
/*    */   {
/* 88 */     this.nettyResponse.release();
/* 89 */     this.context.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\Netty4ClientHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */