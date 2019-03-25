/*    */ package org.apache.catalina.util;
/*    */ 
/*    */ import javax.servlet.ServletContext;
/*    */ import javax.servlet.SessionCookieConfig;
/*    */ import org.apache.catalina.Context;
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
/*    */ public class SessionConfig
/*    */ {
/*    */   private static final String DEFAULT_SESSION_COOKIE_NAME = "JSESSIONID";
/*    */   private static final String DEFAULT_SESSION_PARAMETER_NAME = "jsessionid";
/*    */   
/*    */   public static String getSessionCookieName(Context context)
/*    */   {
/* 36 */     String result = getConfiguredSessionCookieName(context);
/*    */     
/* 38 */     if (result == null) {
/* 39 */       result = "JSESSIONID";
/*    */     }
/*    */     
/* 42 */     return result;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String getSessionUriParamName(Context context)
/*    */   {
/* 53 */     String result = getConfiguredSessionCookieName(context);
/*    */     
/* 55 */     if (result == null) {
/* 56 */       result = "jsessionid";
/*    */     }
/*    */     
/* 59 */     return result;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private static String getConfiguredSessionCookieName(Context context)
/*    */   {
/* 69 */     if (context != null) {
/* 70 */       String cookieName = context.getSessionCookieName();
/* 71 */       if ((cookieName != null) && (cookieName.length() > 0)) {
/* 72 */         return cookieName;
/*    */       }
/*    */       
/*    */ 
/* 76 */       SessionCookieConfig scc = context.getServletContext().getSessionCookieConfig();
/* 77 */       cookieName = scc.getName();
/* 78 */       if ((cookieName != null) && (cookieName.length() > 0)) {
/* 79 */         return cookieName;
/*    */       }
/*    */     }
/*    */     
/* 83 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\SessionConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */