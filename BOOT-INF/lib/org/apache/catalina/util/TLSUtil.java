/*    */ package org.apache.catalina.util;
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
/*    */ public class TLSUtil
/*    */ {
/*    */   public static boolean isTLSRequestAttribute(String name)
/*    */   {
/* 36 */     return ("javax.servlet.request.X509Certificate".equals(name)) || 
/* 37 */       ("javax.servlet.request.cipher_suite".equals(name)) || 
/* 38 */       ("javax.servlet.request.key_size".equals(name)) || 
/* 39 */       ("javax.servlet.request.ssl_session_id".equals(name)) || 
/* 40 */       ("javax.servlet.request.ssl_session_mgr".equals(name)) || 
/* 41 */       ("org.apache.tomcat.util.net.secure_protocol_version".equals(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\TLSUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */