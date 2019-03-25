/*    */ package org.hibernate.validator.internal.util;
/*    */ 
/*    */ import org.hibernate.validator.internal.util.logging.Log;
/*    */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*    */ import org.hibernate.validator.internal.util.logging.Messages;
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
/*    */ public final class Contracts
/*    */ {
/* 21 */   private static final Log log = ;
/*    */   
/*    */ 
/*    */ 
/*    */   public static void assertNotNull(Object o)
/*    */   {
/* 27 */     assertNotNull(o, Messages.MESSAGES.mustNotBeNull());
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
/*    */   public static void assertNotNull(Object o, String message)
/*    */   {
/* 40 */     if (o == null) {
/* 41 */       throw log.getIllegalArgumentException(message);
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
/*    */ 
/*    */   public static void assertValueNotNull(Object o, String name)
/*    */   {
/* 56 */     if (o == null) {
/* 57 */       throw log.getIllegalArgumentException(Messages.MESSAGES.mustNotBeNull(name));
/*    */     }
/*    */   }
/*    */   
/*    */   public static void assertTrue(boolean condition, String message) {
/* 62 */     if (!condition) {
/* 63 */       throw log.getIllegalArgumentException(message);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void assertNotEmpty(String s, String message) {
/* 68 */     if (s.length() == 0) {
/* 69 */       throw log.getIllegalArgumentException(message);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\Contracts.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */