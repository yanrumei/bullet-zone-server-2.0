/*    */ package org.springframework.web.multipart.support;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.Part;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.web.multipart.MultipartException;
/*    */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*    */ import org.springframework.web.multipart.MultipartResolver;
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
/*    */ public class StandardServletMultipartResolver
/*    */   implements MultipartResolver
/*    */ {
/* 52 */   private boolean resolveLazily = false;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setResolveLazily(boolean resolveLazily)
/*    */   {
/* 64 */     this.resolveLazily = resolveLazily;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isMultipart(HttpServletRequest request)
/*    */   {
/* 71 */     if (!"post".equalsIgnoreCase(request.getMethod())) {
/* 72 */       return false;
/*    */     }
/* 74 */     String contentType = request.getContentType();
/* 75 */     return StringUtils.startsWithIgnoreCase(contentType, "multipart/");
/*    */   }
/*    */   
/*    */   public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException
/*    */   {
/* 80 */     return new StandardMultipartHttpServletRequest(request, this.resolveLazily);
/*    */   }
/*    */   
/*    */ 
/*    */   public void cleanupMultipart(MultipartHttpServletRequest request)
/*    */   {
/*    */     try
/*    */     {
/* 88 */       for (Part part : request.getParts()) {
/* 89 */         if (request.getFile(part.getName()) != null) {
/* 90 */           part.delete();
/*    */         }
/*    */       }
/*    */     }
/*    */     catch (Throwable ex) {
/* 95 */       LogFactory.getLog(getClass()).warn("Failed to perform cleanup of multipart items", ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\multipart\support\StandardServletMultipartResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */