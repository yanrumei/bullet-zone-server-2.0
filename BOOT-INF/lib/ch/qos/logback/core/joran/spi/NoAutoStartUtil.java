/*    */ package ch.qos.logback.core.joran.spi;
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
/*    */ public class NoAutoStartUtil
/*    */ {
/*    */   public static boolean notMarkedWithNoAutoStart(Object o)
/*    */   {
/* 26 */     if (o == null) {
/* 27 */       return false;
/*    */     }
/* 29 */     Class<?> clazz = o.getClass();
/* 30 */     NoAutoStart a = (NoAutoStart)clazz.getAnnotation(NoAutoStart.class);
/* 31 */     return a == null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\joran\spi\NoAutoStartUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */