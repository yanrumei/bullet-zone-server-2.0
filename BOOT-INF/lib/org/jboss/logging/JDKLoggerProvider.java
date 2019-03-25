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
/*    */ final class JDKLoggerProvider
/*    */   extends AbstractMdcLoggerProvider
/*    */   implements LoggerProvider
/*    */ {
/*    */   public Logger getLogger(String name)
/*    */   {
/* 24 */     return new JDKLogger(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\JDKLoggerProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */