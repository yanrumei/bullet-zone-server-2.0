/*    */ package org.apache.tomcat.util.compat;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
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
/*    */ public class JrePlatform
/*    */ {
/*    */   private static final String OS_NAME_PROPERTY = "os.name";
/*    */   private static final String OS_NAME_WINDOWS_PREFIX = "Windows";
/*    */   public static final boolean IS_WINDOWS;
/*    */   
/*    */   static
/*    */   {
/*    */     String osName;
/*    */     String osName;
/* 41 */     if (System.getSecurityManager() == null) {
/* 42 */       osName = System.getProperty("os.name");
/*    */     } else {
/* 44 */       osName = (String)AccessController.doPrivileged(new PrivilegedAction()
/*    */       {
/*    */ 
/*    */         public String run()
/*    */         {
/* 49 */           return System.getProperty("os.name");
/*    */         }
/*    */       });
/*    */     }
/*    */     
/* 54 */     IS_WINDOWS = osName.startsWith("Windows");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\compat\JrePlatform.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */