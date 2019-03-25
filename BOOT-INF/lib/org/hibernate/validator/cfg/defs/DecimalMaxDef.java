/*    */ package org.hibernate.validator.cfg.defs;
/*    */ 
/*    */ import javax.validation.constraints.DecimalMax;
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
/*    */ public class DecimalMaxDef
/*    */   extends ConstraintDef<DecimalMaxDef, DecimalMax>
/*    */ {
/*    */   public DecimalMaxDef()
/*    */   {
/* 20 */     super(DecimalMax.class);
/*    */   }
/*    */   
/*    */   public DecimalMaxDef value(String max) {
/* 24 */     addParameter("value", max);
/* 25 */     return this;
/*    */   }
/*    */   
/*    */   public DecimalMaxDef inclusive(boolean inclusive) {
/* 29 */     addParameter("inclusive", Boolean.valueOf(inclusive));
/* 30 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\defs\DecimalMaxDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */