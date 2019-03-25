/*    */ package org.springframework.boot.context.embedded.jetty;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import javax.servlet.ServletContext;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletRequestWrapper;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.eclipse.jetty.http.HttpMethod;
/*    */ import org.eclipse.jetty.server.Request;
/*    */ import org.eclipse.jetty.server.handler.ErrorHandler;
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
/*    */ class JettyEmbeddedErrorHandler
/*    */   extends ErrorHandler
/*    */ {
/*    */   private static final Set<String> SUPPORTED_METHODS;
/*    */   private final ErrorHandler delegate;
/*    */   
/*    */   static
/*    */   {
/* 48 */     Set<String> supportedMethods = new HashSet();
/* 49 */     supportedMethods.add("GET");
/* 50 */     supportedMethods.add("HEAD");
/* 51 */     supportedMethods.add("POST");
/* 52 */     SUPPORTED_METHODS = Collections.unmodifiableSet(supportedMethods);
/*    */   }
/*    */   
/*    */ 
/*    */   JettyEmbeddedErrorHandler(ErrorHandler delegate)
/*    */   {
/* 58 */     this.delegate = delegate;
/*    */   }
/*    */   
/*    */   public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
/*    */     throws IOException
/*    */   {
/* 64 */     if (!isSupported(request.getMethod())) {
/* 65 */       request = new ErrorHttpServletRequest(request);
/*    */     }
/* 67 */     this.delegate.handle(target, baseRequest, request, response);
/*    */   }
/*    */   
/*    */   private boolean isSupported(String method) {
/* 71 */     for (String supportedMethod : SUPPORTED_METHODS) {
/* 72 */       if (supportedMethod.equalsIgnoreCase(method)) {
/* 73 */         return true;
/*    */       }
/*    */     }
/* 76 */     return false;
/*    */   }
/*    */   
/*    */   private static class ErrorHttpServletRequest extends HttpServletRequestWrapper
/*    */   {
/* 81 */     private boolean simulateGetMethod = true;
/*    */     
/*    */     ErrorHttpServletRequest(HttpServletRequest request) {
/* 84 */       super();
/*    */     }
/*    */     
/*    */     public String getMethod()
/*    */     {
/* 89 */       return this.simulateGetMethod ? HttpMethod.GET.toString() : 
/* 90 */         super.getMethod();
/*    */     }
/*    */     
/*    */     public ServletContext getServletContext()
/*    */     {
/* 95 */       this.simulateGetMethod = false;
/* 96 */       return super.getServletContext();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\jetty\JettyEmbeddedErrorHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */