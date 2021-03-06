/*    */ package org.springframework.util;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ public class SimpleIdGenerator
/*    */   implements IdGenerator
/*    */ {
/* 30 */   private final AtomicLong mostSigBits = new AtomicLong(0L);
/*    */   
/* 32 */   private final AtomicLong leastSigBits = new AtomicLong(0L);
/*    */   
/*    */ 
/*    */   public UUID generateId()
/*    */   {
/* 37 */     long leastSigBits = this.leastSigBits.incrementAndGet();
/* 38 */     if (leastSigBits == 0L) {
/* 39 */       this.mostSigBits.incrementAndGet();
/*    */     }
/* 41 */     return new UUID(this.mostSigBits.get(), leastSigBits);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\SimpleIdGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */