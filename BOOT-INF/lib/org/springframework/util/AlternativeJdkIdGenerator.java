/*    */ package org.springframework.util;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import java.security.SecureRandom;
/*    */ import java.util.Random;
/*    */ import java.util.UUID;
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
/*    */ 
/*    */ public class AlternativeJdkIdGenerator
/*    */   implements IdGenerator
/*    */ {
/*    */   private final Random random;
/*    */   
/*    */   public AlternativeJdkIdGenerator()
/*    */   {
/* 40 */     SecureRandom secureRandom = new SecureRandom();
/* 41 */     byte[] seed = new byte[8];
/* 42 */     secureRandom.nextBytes(seed);
/* 43 */     this.random = new Random(new BigInteger(seed).longValue());
/*    */   }
/*    */   
/*    */ 
/*    */   public UUID generateId()
/*    */   {
/* 49 */     byte[] randomBytes = new byte[16];
/* 50 */     this.random.nextBytes(randomBytes);
/*    */     
/* 52 */     long mostSigBits = 0L;
/* 53 */     for (int i = 0; i < 8; i++) {
/* 54 */       mostSigBits = mostSigBits << 8 | randomBytes[i] & 0xFF;
/*    */     }
/*    */     
/* 57 */     long leastSigBits = 0L;
/* 58 */     for (int i = 8; i < 16; i++) {
/* 59 */       leastSigBits = leastSigBits << 8 | randomBytes[i] & 0xFF;
/*    */     }
/*    */     
/* 62 */     return new UUID(mostSigBits, leastSigBits);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\AlternativeJdkIdGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */