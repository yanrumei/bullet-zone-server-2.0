/*     */ package org.hibernate.validator.internal.constraintvalidators.hv;
/*     */ 
/*     */ import java.net.IDN;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.validation.ConstraintValidator;
/*     */ import javax.validation.ConstraintValidatorContext;
/*     */ import org.hibernate.validator.constraints.Email;
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
/*     */ public class EmailValidator
/*     */   implements ConstraintValidator<Email, CharSequence>
/*     */ {
/*     */   private static final String LOCAL_PART_ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-￿-]";
/*     */   private static final String DOMAIN_LABEL = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
/*     */   private static final String DOMAIN = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*";
/*     */   private static final String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";
/*     */   private static final int MAX_LOCAL_PART_LENGTH = 64;
/*     */   private static final int MAX_DOMAIN_PART_LENGTH = 255;
/*  50 */   private static final Pattern LOCAL_PART_PATTERN = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-￿-]+(\\.[a-z0-9!#$%&'*+/=?^_`{|}~-￿-]+)*", 2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private static final Pattern DOMAIN_PATTERN = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]", 2);
/*     */   
/*     */ 
/*     */ 
/*     */   public void initialize(Email annotation) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isValid(CharSequence value, ConstraintValidatorContext context)
/*     */   {
/*  67 */     if ((value == null) || (value.length() == 0)) {
/*  68 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */     String[] emailParts = value.toString().split("@", 3);
/*  76 */     if (emailParts.length != 2) {
/*  77 */       return false;
/*     */     }
/*     */     
/*  80 */     if (!matchLocalPart(emailParts[0])) {
/*  81 */       return false;
/*     */     }
/*     */     
/*  84 */     return matchDomain(emailParts[1]);
/*     */   }
/*     */   
/*     */   private boolean matchLocalPart(String localPart) {
/*  88 */     if (localPart.length() > 64) {
/*  89 */       return false;
/*     */     }
/*  91 */     Matcher matcher = LOCAL_PART_PATTERN.matcher(localPart);
/*  92 */     return matcher.matches();
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean matchDomain(String domain)
/*     */   {
/*  98 */     if (domain.endsWith(".")) {
/*  99 */       return false;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 104 */       asciiString = IDN.toASCII(domain);
/*     */     } catch (IllegalArgumentException e) {
/*     */       String asciiString;
/* 107 */       return false;
/*     */     }
/*     */     String asciiString;
/* 110 */     if (asciiString.length() > 255) {
/* 111 */       return false;
/*     */     }
/*     */     
/* 114 */     Matcher matcher = DOMAIN_PATTERN.matcher(asciiString);
/* 115 */     return matcher.matches();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\EmailValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */