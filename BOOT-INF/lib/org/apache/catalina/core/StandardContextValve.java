/*    */ package org.apache.catalina.core;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.ServletException;
/*    */ import org.apache.catalina.Container;
/*    */ import org.apache.catalina.Pipeline;
/*    */ import org.apache.catalina.Valve;
/*    */ import org.apache.catalina.Wrapper;
/*    */ import org.apache.catalina.connector.Request;
/*    */ import org.apache.catalina.connector.Response;
/*    */ import org.apache.catalina.valves.ValveBase;
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.tomcat.util.buf.MessageBytes;
/*    */ import org.apache.tomcat.util.res.StringManager;
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
/*    */ final class StandardContextValve
/*    */   extends ValveBase
/*    */ {
/* 43 */   private static final StringManager sm = StringManager.getManager(StandardContextValve.class);
/*    */   
/*    */   public StandardContextValve() {
/* 46 */     super(true);
/*    */   }
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
/*    */   public final void invoke(Request request, Response response)
/*    */     throws IOException, ServletException
/*    */   {
/* 66 */     MessageBytes requestPathMB = request.getRequestPathMB();
/* 67 */     if ((requestPathMB.startsWithIgnoreCase("/META-INF/", 0)) || 
/* 68 */       (requestPathMB.equalsIgnoreCase("/META-INF")) || 
/* 69 */       (requestPathMB.startsWithIgnoreCase("/WEB-INF/", 0)) || 
/* 70 */       (requestPathMB.equalsIgnoreCase("/WEB-INF"))) {
/* 71 */       response.sendError(404);
/* 72 */       return;
/*    */     }
/*    */     
/*    */ 
/* 76 */     Wrapper wrapper = request.getWrapper();
/* 77 */     if ((wrapper == null) || (wrapper.isUnavailable())) {
/* 78 */       response.sendError(404);
/* 79 */       return;
/*    */     }
/*    */     
/*    */     try
/*    */     {
/* 84 */       response.sendAcknowledgement();
/*    */     } catch (IOException ioe) {
/* 86 */       this.container.getLogger().error(sm.getString("standardContextValve.acknowledgeException"), ioe);
/*    */       
/* 88 */       request.setAttribute("javax.servlet.error.exception", ioe);
/* 89 */       response.sendError(500);
/* 90 */       return;
/*    */     }
/*    */     
/* 93 */     if (request.isAsyncSupported()) {
/* 94 */       request.setAsyncSupported(wrapper.getPipeline().isAsyncSupported());
/*    */     }
/* 96 */     wrapper.getPipeline().getFirst().invoke(request, response);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\StandardContextValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */