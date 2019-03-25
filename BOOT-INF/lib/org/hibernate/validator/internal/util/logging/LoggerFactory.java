/*    */ package org.hibernate.validator.internal.util.logging;
/*    */ 
/*    */ import org.jboss.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LoggerFactory
/*    */ {
/*    */   public static Log make()
/*    */   {
/* 17 */     Throwable t = new Throwable();
/* 18 */     StackTraceElement directCaller = t.getStackTrace()[1];
/* 19 */     return (Log)Logger.getMessageLogger(Log.class, directCaller.getClassName());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\logging\LoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */