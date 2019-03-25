/*    */ package org.hibernate.validator.cfg.defs;
/*    */ 
/*    */ import org.hibernate.validator.cfg.ConstraintDef;
/*    */ import org.hibernate.validator.constraints.Mod10Check;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Mod10CheckDef
/*    */   extends ConstraintDef<Mod10CheckDef, Mod10Check>
/*    */ {
/*    */   public Mod10CheckDef()
/*    */   {
/* 19 */     super(Mod10Check.class);
/*    */   }
/*    */   
/*    */   public Mod10CheckDef multiplier(int multiplier) {
/* 23 */     addParameter("multiplier", Integer.valueOf(multiplier));
/* 24 */     return this;
/*    */   }
/*    */   
/*    */   public Mod10CheckDef weight(int weight) {
/* 28 */     addParameter("weight", Integer.valueOf(weight));
/* 29 */     return this;
/*    */   }
/*    */   
/*    */   public Mod10CheckDef startIndex(int startIndex) {
/* 33 */     addParameter("startIndex", Integer.valueOf(startIndex));
/* 34 */     return this;
/*    */   }
/*    */   
/*    */   public Mod10CheckDef endIndex(int endIndex) {
/* 38 */     addParameter("endIndex", Integer.valueOf(endIndex));
/* 39 */     return this;
/*    */   }
/*    */   
/*    */   public Mod10CheckDef checkDigitIndex(int checkDigitIndex) {
/* 43 */     addParameter("checkDigitIndex", Integer.valueOf(checkDigitIndex));
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   public Mod10CheckDef ignoreNonDigitCharacters(boolean ignoreNonDigitCharacters) {
/* 48 */     addParameter("ignoreNonDigitCharacters", Boolean.valueOf(ignoreNonDigitCharacters));
/* 49 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\defs\Mod10CheckDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */