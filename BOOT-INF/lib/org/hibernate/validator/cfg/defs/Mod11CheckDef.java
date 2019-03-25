/*    */ package org.hibernate.validator.cfg.defs;
/*    */ 
/*    */ import org.hibernate.validator.cfg.ConstraintDef;
/*    */ import org.hibernate.validator.constraints.Mod11Check;
/*    */ import org.hibernate.validator.constraints.Mod11Check.ProcessingDirection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Mod11CheckDef
/*    */   extends ConstraintDef<Mod11CheckDef, Mod11Check>
/*    */ {
/*    */   public Mod11CheckDef()
/*    */   {
/* 19 */     super(Mod11Check.class);
/*    */   }
/*    */   
/*    */   public Mod11CheckDef threshold(int threshold) {
/* 23 */     addParameter("threshold", Integer.valueOf(threshold));
/* 24 */     return this;
/*    */   }
/*    */   
/*    */   public Mod11CheckDef startIndex(int startIndex) {
/* 28 */     addParameter("startIndex", Integer.valueOf(startIndex));
/* 29 */     return this;
/*    */   }
/*    */   
/*    */   public Mod11CheckDef endIndex(int endIndex) {
/* 33 */     addParameter("endIndex", Integer.valueOf(endIndex));
/* 34 */     return this;
/*    */   }
/*    */   
/*    */   public Mod11CheckDef checkDigitIndex(int checkDigitIndex) {
/* 38 */     addParameter("checkDigitIndex", Integer.valueOf(checkDigitIndex));
/* 39 */     return this;
/*    */   }
/*    */   
/*    */   public Mod11CheckDef ignoreNonDigitCharacters(boolean ignoreNonDigitCharacters) {
/* 43 */     addParameter("ignoreNonDigitCharacters", Boolean.valueOf(ignoreNonDigitCharacters));
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   public Mod11CheckDef treatCheck10As(char treatCheck10As) {
/* 48 */     addParameter("treatCheck10As", Character.valueOf(treatCheck10As));
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public Mod11CheckDef treatCheck11As(char treatCheck11As) {
/* 53 */     addParameter("treatCheck11As", Character.valueOf(treatCheck11As));
/* 54 */     return this;
/*    */   }
/*    */   
/*    */   public Mod11CheckDef processingDirection(Mod11Check.ProcessingDirection processingDirection) {
/* 58 */     addParameter("processingDirection", processingDirection);
/* 59 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\defs\Mod11CheckDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */