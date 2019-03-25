/*    */ package org.hibernate.validator.internal.metadata.raw;
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
/*    */ public enum ConfigurationSource
/*    */ {
/* 19 */   ANNOTATION(0), 
/*    */   
/*    */ 
/*    */ 
/* 23 */   XML(1), 
/*    */   
/*    */ 
/*    */ 
/* 27 */   API(2);
/*    */   
/*    */   private int priority;
/*    */   
/*    */   private ConfigurationSource(int priority) {
/* 32 */     this.priority = priority;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getPriority()
/*    */   {
/* 43 */     return this.priority;
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
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ConfigurationSource max(ConfigurationSource a, ConfigurationSource b)
/*    */   {
/* 59 */     return a.getPriority() >= b.getPriority() ? a : b;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\raw\ConfigurationSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */