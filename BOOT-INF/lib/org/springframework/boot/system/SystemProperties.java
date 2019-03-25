/*    */ package org.springframework.boot.system;
/*    */ 
/*    */ import java.io.PrintStream;
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
/*    */ final class SystemProperties
/*    */ {
/*    */   public static String get(String... properties)
/*    */   {
/* 30 */     for (String property : properties) {
/*    */       try {
/* 32 */         String override = System.getProperty(property);
/* 33 */         override = override != null ? override : System.getenv(property);
/* 34 */         if (override != null) {
/* 35 */           return override;
/*    */         }
/*    */       }
/*    */       catch (Throwable ex) {
/* 39 */         System.err.println("Could not resolve '" + property + "' as system property: " + ex);
/*    */       }
/*    */     }
/*    */     
/* 43 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\system\SystemProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */