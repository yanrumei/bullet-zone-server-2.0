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
/*    */ @UsesSunHttpServer
/*    */ public class SimpleHessianServiceExporter
/*    */   extends HessianExporter
/*    */   implements HttpHandler
/*    */ {
/*    */   public void handle(HttpExchange exchange)
/*    */     throws IOException
/*    */   {
/* 56 */     if (!"POST".equals(exchange.getRequestMethod())) {
/* 57 */       exchange.getResponseHeaders().set("Allow", "POST");
/* 58 */       exchange.sendResponseHeaders(405, -1L);
/* 59 */       return;
/*    */     }
/*    */     
/* 62 */     ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
/*    */     try {
/* 64 */       invoke(exchange.getRequestBody(), output);
/*    */     }
/*    */     catch (Throwable ex) {
/* 67 */       exchange.sendResponseHeaders(500, -1L);
/* 68 */       this.logger.error("Hessian skeleton invocation failed", ex);
/* 69 */       return;
/*    */     }
/*    */     
/* 72 */     exchange.getResponseHeaders().set("Content-Type", "application/x-hessian");
/* 73 */     exchange.sendResponseHeaders(200, output.size());
/* 74 */     FileCopyUtils.copy(output.toByteArray(), exchange.getResponseBody());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\remoting\caucho\SimpleHessianServiceExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */