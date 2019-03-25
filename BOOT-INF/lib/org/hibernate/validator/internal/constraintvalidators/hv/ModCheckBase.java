/*     */ package org.hibernate.validator.internal.constraintvalidators.hv;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.validation.ConstraintValidatorContext;
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
/*     */ public abstract class ModCheckBase
/*     */ {
/*  28 */   private static final Log log = ;
/*     */   
/*  30 */   private static final Pattern NUMBERS_ONLY_REGEXP = Pattern.compile("[^0-9]");
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int DEC_RADIX = 10;
/*     */   
/*     */ 
/*     */   private int startIndex;
/*     */   
/*     */ 
/*     */   private int endIndex;
/*     */   
/*     */ 
/*     */   private int checkDigitIndex;
/*     */   
/*     */ 
/*     */   private boolean ignoreNonDigitCharacters;
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isValid(CharSequence value, ConstraintValidatorContext context)
/*     */   {
/*  52 */     if (value == null) {
/*  53 */       return true;
/*     */     }
/*     */     
/*  56 */     String valueAsString = value.toString();
/*     */     
/*     */     try
/*     */     {
/*  60 */       String digitsAsString = extractVerificationString(valueAsString);
/*  61 */       checkDigit = extractCheckDigit(valueAsString);
/*     */     } catch (IndexOutOfBoundsException e) {
/*     */       char checkDigit;
/*  64 */       return false; }
/*     */     char checkDigit;
/*  66 */     String digitsAsString = stripNonDigitsIfRequired(digitsAsString);
/*     */     
/*     */     try
/*     */     {
/*  70 */       digits = extractDigits(digitsAsString);
/*     */     } catch (NumberFormatException e) {
/*     */       List<Integer> digits;
/*  73 */       return false;
/*     */     }
/*     */     List<Integer> digits;
/*  76 */     return isCheckDigitValid(digits, checkDigit);
/*     */   }
/*     */   
/*     */   public abstract boolean isCheckDigitValid(List<Integer> paramList, char paramChar);
/*     */   
/*     */   protected void initialize(int startIndex, int endIndex, int checkDigitIndex, boolean ignoreNonDigitCharacters) {
/*  82 */     this.startIndex = startIndex;
/*  83 */     this.endIndex = endIndex;
/*  84 */     this.checkDigitIndex = checkDigitIndex;
/*  85 */     this.ignoreNonDigitCharacters = ignoreNonDigitCharacters;
/*     */     
/*  87 */     validateOptions();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int extractDigit(char value)
/*     */     throws NumberFormatException
/*     */   {
/* 100 */     if (Character.isDigit(value)) {
/* 101 */       return Character.digit(value, 10);
/*     */     }
/*     */     
/* 104 */     throw log.getCharacterIsNotADigitException(value);
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
/*     */   private List<Integer> extractDigits(String value)
/*     */     throws NumberFormatException
/*     */   {
/* 118 */     List<Integer> digits = new ArrayList(value.length());
/* 119 */     char[] chars = value.toCharArray();
/* 120 */     for (char c : chars) {
/* 121 */       digits.add(Integer.valueOf(extractDigit(c)));
/*     */     }
/* 123 */     return digits;
/*     */   }
/*     */   
/*     */   private boolean validateOptions() {
/* 127 */     if (this.startIndex < 0) {
/* 128 */       throw log.getStartIndexCannotBeNegativeException(this.startIndex);
/*     */     }
/*     */     
/* 131 */     if (this.endIndex < 0) {
/* 132 */       throw log.getEndIndexCannotBeNegativeException(this.endIndex);
/*     */     }
/*     */     
/* 135 */     if (this.startIndex > this.endIndex) {
/* 136 */       throw log.getInvalidRangeException(this.startIndex, this.endIndex);
/*     */     }
/*     */     
/* 139 */     if ((this.checkDigitIndex > 0) && (this.startIndex <= this.checkDigitIndex) && (this.endIndex > this.checkDigitIndex)) {
/* 140 */       throw log.getInvalidCheckDigitException(this.startIndex, this.endIndex);
/*     */     }
/*     */     
/* 143 */     return true;
/*     */   }
/*     */   
/*     */   private String stripNonDigitsIfRequired(String value) {
/* 147 */     if (this.ignoreNonDigitCharacters) {
/* 148 */       return NUMBERS_ONLY_REGEXP.matcher(value).replaceAll("");
/*     */     }
/*     */     
/* 151 */     return value;
/*     */   }
/*     */   
/*     */   private String extractVerificationString(String value)
/*     */     throws IndexOutOfBoundsException
/*     */   {
/* 157 */     if (this.endIndex == Integer.MAX_VALUE) {
/* 158 */       return value.substring(0, value.length() - 1);
/*     */     }
/* 160 */     if (this.checkDigitIndex == -1) {
/* 161 */       return value.substring(this.startIndex, this.endIndex);
/*     */     }
/*     */     
/* 164 */     return value.substring(this.startIndex, this.endIndex + 1);
/*     */   }
/*     */   
/*     */   private char extractCheckDigit(String value)
/*     */     throws IndexOutOfBoundsException
/*     */   {
/* 170 */     if (this.checkDigitIndex == -1) {
/* 171 */       if (this.endIndex == Integer.MAX_VALUE) {
/* 172 */         return value.charAt(value.length() - 1);
/*     */       }
/*     */       
/* 175 */       return value.charAt(this.endIndex);
/*     */     }
/*     */     
/*     */ 
/* 179 */     return value.charAt(this.checkDigitIndex);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\ModCheckBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */