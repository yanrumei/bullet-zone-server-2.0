/*    */ package org.jboss.logging;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ final class SerializedLogger
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 508779982439435831L;
/*    */   private final String name;
/*    */   
/*    */   SerializedLogger(String name)
/*    */   {
/* 30 */     this.name = name;
/*    */   }
/*    */   
/*    */   protected Object readResolve() {
/* 34 */     return Logger.getLogger(this.name);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\SerializedLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */