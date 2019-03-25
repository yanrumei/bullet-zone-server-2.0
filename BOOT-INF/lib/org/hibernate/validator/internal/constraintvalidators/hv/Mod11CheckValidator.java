/*     */ package org.hibernate.validator.internal.constraintvalidators.hv;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.validation.ConstraintValidator;
/*     */ import org.hibernate.validator.constraints.Mod11Check;
/*     */ import org.hibernate.validator.constraints.Mod11Check.ProcessingDirection;
/*     */ import org.hibernate.validator.internal.util.ModUtil;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*     */ public class Mod11CheckValidator
/*     */   extends ModCheckBase
/*     */   implements ConstraintValidator<Mod11Check, CharSequence>
/*     */ {
/*  31 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean reverseOrder;
/*     */   
/*     */ 
/*     */ 
/*     */   private char treatCheck10As;
/*     */   
/*     */ 
/*     */ 
/*     */   private char treatCheck11As;
/*     */   
/*     */ 
/*     */ 
/*     */   private int threshold;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialize(Mod11Check constraintAnnotation)
/*     */   {
/*  54 */     initialize(constraintAnnotation
/*  55 */       .startIndex(), constraintAnnotation
/*  56 */       .endIndex(), constraintAnnotation
/*  57 */       .checkDigitIndex(), constraintAnnotation
/*  58 */       .ignoreNonDigitCharacters(), constraintAnnotation
/*  59 */       .threshold(), constraintAnnotation
/*  60 */       .treatCheck10As(), constraintAnnotation
/*  61 */       .treatCheck11As(), constraintAnnotation
/*  62 */       .processingDirection());
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
/*     */   public void initialize(int startIndex, int endIndex, int checkDigitIndex, boolean ignoreNonDigitCharacters, int threshold, char treatCheck10As, char treatCheck11As, Mod11Check.ProcessingDirection direction)
/*     */   {
/*  75 */     super.initialize(startIndex, endIndex, checkDigitIndex, ignoreNonDigitCharacters);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */     this.threshold = threshold;
/*  82 */     this.reverseOrder = (direction == Mod11Check.ProcessingDirection.LEFT_TO_RIGHT);
/*     */     
/*  84 */     this.treatCheck10As = treatCheck10As;
/*  85 */     this.treatCheck11As = treatCheck11As;
/*     */     
/*  87 */     if (!Character.isLetterOrDigit(this.treatCheck10As)) {
/*  88 */       throw log.getTreatCheckAsIsNotADigitNorALetterException(this.treatCheck10As);
/*     */     }
/*     */     
/*  91 */     if (!Character.isLetterOrDigit(this.treatCheck11As)) {
/*  92 */       throw log.getTreatCheckAsIsNotADigitNorALetterException(this.treatCheck11As);
/*     */     }
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
/*     */   public boolean isCheckDigitValid(List<Integer> digits, char checkDigit)
/*     */   {
/* 106 */     if (this.reverseOrder) {
/* 107 */       Collections.reverse(digits);
/*     */     }
/*     */     
/* 110 */     int modResult = ModUtil.calculateMod11Check(digits, this.threshold);
/* 111 */     switch (modResult) {
/*     */     case 10: 
/* 113 */       return checkDigit == this.treatCheck10As;
/*     */     case 11: 
/* 115 */       return checkDigit == this.treatCheck11As;
/*     */     }
/* 117 */     return (Character.isDigit(checkDigit)) && (modResult == extractDigit(checkDigit));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\Mod11CheckValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */