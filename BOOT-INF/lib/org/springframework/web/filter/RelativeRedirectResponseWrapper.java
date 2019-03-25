/*    */ package org.springframework.web.filter;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.ServletResponse;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import javax.servlet.http.HttpServletResponseWrapper;
/*    */ import org.springframework.http.HttpStatus;
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
/*    */ class RelativeRedirectResponseWrapper
/*    */   extends HttpServletResponseWrapper
/*    */ {
/*    */   private final HttpStatus redirectStatus;
/*    */   
/*    */   private RelativeRedirectResponseWrapper(HttpServletResponse response, HttpStatus redirectStatus)
/*    */   {
/* 40 */     super(response);
/* 41 */     Assert.notNull(redirectStatus, "'redirectStatus' is required");
/* 42 */     this.redirectStatus = redirectStatus;
/*    */   }
/*    */   
/*    */   public void sendRedirect(String location)
/*    */     throws IOException
/*    */   {
/* 48 */     setStatus(this.redirectStatus.value());
/* 49 */     setHeader("Location", location);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static HttpServletResponse wrapIfNecessary(HttpServletResponse response, HttpStatus redirectStatus)
/*    */   {
/* 56 */     return hasWrapper(response) ? response : new RelativeRedirectResponseWrapper(response, redirectStatus);
/*    */   }
/*    */   
/*    */   private static boolean hasWrapper(ServletResponse response) {
/* 60 */     if ((response instanceof RelativeRedirectResponseWrapper)) {
/* 61 */       return true;
/*    */     }
/* 63 */     while ((response instanceof HttpServletResponseWrapper)) {
/* 64 */       response = ((HttpServletResponseWrapper)response).getResponse();
/* 65 */       if ((response instanceof RelativeRedirectResponseWrapper)) {
/* 66 */         return true;
/*    */       }
/*    */     }
/* 69 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\filter\RelativeRedirectResponseWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */