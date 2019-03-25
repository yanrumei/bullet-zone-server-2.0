/*    */ package org.springframework.remoting.caucho;
/*    */ 
/*    */ import com.sun.net.httpserver.Headers;
/*    */ import com.sun.net.httpserver.HttpExchange;
/*    */ import com.sun.net.httpserver.HttpHandler;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.springframework.lang.UsesSunHttpServer;
/*    */ import org.springframework.util.FileCopyUtils;
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
/*    */ @Deprecated
/*    */ @UsesSunHttpServer
/*    */ public class SimpleBurlapServiceExporter
/*    */   extends BurlapExporter
/*    */   implements HttpHandler
/*    */ {
/*    */   public void handle(HttpExchange exchange)
/*    */     throws IOException
/*    */   {
/* 60 */     if (!"POST".equals(exchange.getRequestMethod())) {
/* 61 */       exchange.getResponseHeaders().set("Allow", "POST");
/* 62 */       exchange.sendResponseHeaders(405, -1L);
/* 63 */       return;
/*    */     }
/*    */     
/* 66 */     ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
/*    */     try {
/* 68 */       invoke(exchange.getRequestBody(), output);
/*    */     }
/*    */     catch (Throwable ex) {
/* 71 */       exchange.sendResponseHeaders(500, -1L);
/* 72 */       this.logger.error("Burlap skeleton invocation failed", ex);
/*    */     }
/*    */     
/* 75 */     exchange.sendResponseHeaders(200, output.size());
/* 76 */     FileCopyUtils.copy(output.toByteArray(), exchange.getResponseBody());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\remoting\caucho\SimpleBurlapServiceExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */