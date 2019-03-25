/*    */ package org.hibernate.validator.cfg.defs;
/*    */ 
/*    */ import javax.validation.constraints.Digits;
/*    */ import org.hibernate.validator.cfg.ConstraintDef;
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
/*    */ public class DigitsDef
/*    */   extends ConstraintDef<DigitsDef, Digits>
/*    */ {
/*    */   public DigitsDef()
/*    */   {
/* 20 */     super(Digits.class);
/*    */   }
/*    */   
/*    */   public DigitsDef integer(int integer) {
/* 24 */     addParameter("integer", Integer.valueOf(integer));
/* 25 */     return this;
/*    */   }
/*    */   
/*    */   public DigitsDef fraction(int fraction) {
/* 29 */     addParameter("fraction", Integer.valueOf(fraction));
/* 30 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\defs\DigitsDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */