/*    */ package org.hibernate.validator.internal.engine.time;
/*    */ 
/*    */ import org.hibernate.validator.spi.time.TimeProvider;
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
/*    */ public class DefaultTimeProvider
/*    */   implements TimeProvider
/*    */ {
/* 18 */   private static final DefaultTimeProvider INSTANCE = new DefaultTimeProvider();
/*    */   
/*    */ 
/*    */ 
/*    */   public static final DefaultTimeProvider getInstance()
/*    */   {
/* 24 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   public long getCurrentTime()
/*    */   {
/* 29 */     return System.currentTimeMillis();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\time\DefaultTimeProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */