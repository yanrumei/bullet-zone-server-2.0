/*     */ package org.hibernate.validator.internal.util;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ModUtil
/*     */ {
/*     */   public static int calculateLuhnMod10Check(List<Integer> digits)
/*     */   {
/*  28 */     int sum = 0;
/*  29 */     boolean even = true;
/*  30 */     for (int index = digits.size() - 1; index >= 0; index--) {
/*  31 */       int digit = ((Integer)digits.get(index)).intValue();
/*     */       
/*  33 */       if (even) {
/*  34 */         digit <<= 1;
/*     */       }
/*  36 */       if (digit > 9) {
/*  37 */         digit -= 9;
/*     */       }
/*  39 */       sum += digit;
/*  40 */       even = !even;
/*     */     }
/*  42 */     return (10 - sum % 10) % 10;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int calculateMod10Check(List<Integer> digits, int multiplier, int weight)
/*     */   {
/*  55 */     int sum = 0;
/*  56 */     boolean even = true;
/*  57 */     for (int index = digits.size() - 1; index >= 0; index--) {
/*  58 */       int digit = ((Integer)digits.get(index)).intValue();
/*     */       
/*  60 */       if (even) {
/*  61 */         digit *= multiplier;
/*     */       }
/*     */       else {
/*  64 */         digit *= weight;
/*     */       }
/*     */       
/*  67 */       sum += digit;
/*  68 */       even = !even;
/*     */     }
/*  70 */     return (10 - sum % 10) % 10;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int calculateMod11Check(List<Integer> digits, int threshold)
/*     */   {
/*  82 */     int sum = 0;
/*  83 */     int multiplier = 2;
/*     */     
/*  85 */     for (int index = digits.size() - 1; index >= 0; index--) {
/*  86 */       sum += ((Integer)digits.get(index)).intValue() * multiplier++;
/*  87 */       if (multiplier > threshold) {
/*  88 */         multiplier = 2;
/*     */       }
/*     */     }
/*  91 */     return 11 - sum % 11;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int calculateMod11Check(List<Integer> digits)
/*     */   {
/* 102 */     return calculateMod11Check(digits, Integer.MAX_VALUE);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\ModUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */