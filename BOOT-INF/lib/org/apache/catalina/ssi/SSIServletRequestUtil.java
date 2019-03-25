/*    */ package org.apache.catalina.ssi;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.tomcat.util.http.RequestUtil;
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
/*    */ public class SSIServletRequestUtil
/*    */ {
/*    */   public static String getRelativePath(HttpServletRequest request)
/*    */   {
/* 36 */     if (request.getAttribute("javax.servlet.include.request_uri") != null)
/*    */     {
/* 38 */       String result = (String)request.getAttribute("javax.servlet.include.path_info");
/*    */       
/* 40 */       if (result == null) {
/* 41 */         result = (String)request.getAttribute("javax.servlet.include.servlet_path");
/*    */       }
/* 43 */       if ((result == null) || (result.equals(""))) result = "/";
/* 44 */       return result;
/*    */     }
/*    */     
/* 47 */     String result = request.getPathInfo();
/* 48 */     if (result == null) {
/* 49 */       result = request.getServletPath();
/*    */     }
/* 51 */     if ((result == null) || (result.equals(""))) {
/* 52 */       result = "/";
/*    */     }
/* 54 */     return RequestUtil.normalize(result);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSIServletRequestUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */