/*    */ package org.apache.tomcat.util;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
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
/*    */ public class ExceptionUtils
/*    */ {
/*    */   public static void handleThrowable(Throwable t)
/*    */   {
/* 33 */     if ((t instanceof ThreadDeath)) {
/* 34 */       throw ((ThreadDeath)t);
/*    */     }
/* 36 */     if ((t instanceof StackOverflowError))
/*    */     {
/* 38 */       return;
/*    */     }
/* 40 */     if ((t instanceof VirtualMachineError)) {
/* 41 */       throw ((VirtualMachineError)t);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Throwable unwrapInvocationTargetException(Throwable t)
/*    */   {
/* 55 */     if (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 56 */       return t.getCause();
/*    */     }
/* 58 */     return t;
/*    */   }
/*    */   
/*    */   public static void preload() {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\ExceptionUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */