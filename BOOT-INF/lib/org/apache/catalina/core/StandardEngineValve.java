/*    */ package org.apache.catalina.core;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.ServletException;
/*    */ import org.apache.catalina.Host;
/*    */ import org.apache.catalina.Pipeline;
/*    */ import org.apache.catalina.Valve;
/*    */ import org.apache.catalina.connector.Request;
/*    */ import org.apache.catalina.connector.Response;
/*    */ import org.apache.catalina.valves.ValveBase;
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
/*    */ 
/*    */ final class StandardEngineValve
/*    */   extends ValveBase
/*    */ {
/*    */   public StandardEngineValve()
/*    */   {
/* 43 */     super(true);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 53 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
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
/*    */   public final void invoke(Request request, Response response)
/*    */     throws IOException, ServletException
/*    */   {
/* 74 */     Host host = request.getHost();
/* 75 */     if (host == null)
/*    */     {
/* 77 */       response.sendError(400, sm
/* 78 */         .getString("standardEngine.noHost", new Object[] {request
/* 79 */         .getServerName() }));
/* 80 */       return;
/*    */     }
/* 82 */     if (request.isAsyncSupported()) {
/* 83 */       request.setAsyncSupported(host.getPipeline().isAsyncSupported());
/*    */     }
/*    */     
/*    */ 
/* 87 */     host.getPipeline().getFirst().invoke(request, response);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\StandardEngineValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */