/*    */ package org.apache.tomcat.util.compat;
/*    */ 
/*    */ import java.util.Locale;
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
/*    */ public class JreVendor
/*    */ {
/*    */   public static final boolean IS_ORACLE_JVM;
/*    */   public static final boolean IS_IBM_JVM;
/*    */   
/*    */   static
/*    */   {
/* 31 */     String vendor = System.getProperty("java.vendor", "");
/* 32 */     vendor = vendor.toLowerCase(Locale.ENGLISH);
/*    */     
/* 34 */     if ((vendor.startsWith("oracle")) || (vendor.startsWith("sun"))) {
/* 35 */       IS_ORACLE_JVM = true;
/* 36 */       IS_IBM_JVM = false;
/* 37 */     } else if (vendor.contains("ibm")) {
/* 38 */       IS_ORACLE_JVM = false;
/* 39 */       IS_IBM_JVM = true;
/*    */     } else {
/* 41 */       IS_ORACLE_JVM = false;
/* 42 */       IS_IBM_JVM = false;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\compat\JreVendor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */