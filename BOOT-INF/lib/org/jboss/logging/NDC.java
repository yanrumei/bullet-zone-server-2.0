/*    */ package org.jboss.logging;
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
/*    */ public final class NDC
/*    */ {
/*    */   public static void clear()
/*    */   {
/* 30 */     LoggerProviders.PROVIDER.clearNdc();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String get()
/*    */   {
/* 39 */     return LoggerProviders.PROVIDER.getNdc();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static int getDepth()
/*    */   {
/* 48 */     return LoggerProviders.PROVIDER.getNdcDepth();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String pop()
/*    */   {
/* 57 */     return LoggerProviders.PROVIDER.popNdc();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String peek()
/*    */   {
/* 66 */     return LoggerProviders.PROVIDER.peekNdc();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void push(String message)
/*    */   {
/* 75 */     LoggerProviders.PROVIDER.pushNdc(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void setMaxDepth(int maxDepth)
/*    */   {
/* 84 */     LoggerProviders.PROVIDER.setNdcMaxDepth(maxDepth);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\NDC.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */