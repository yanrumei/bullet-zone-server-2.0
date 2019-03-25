/*    */ package org.hibernate.validator.cfg.defs;
/*    */ 
/*    */ import javax.validation.constraints.DecimalMin;
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
/*    */ public class DecimalMinDef
/*    */   extends ConstraintDef<DecimalMinDef, DecimalMin>
/*    */ {
/*    */   public DecimalMinDef()
/*    */   {
/* 19 */     super(DecimalMin.class);
/*    */   }
/*    */   
/*    */   public DecimalMinDef value(String min) {
/* 23 */     addParameter("value", min);
/* 24 */     return this;
/*    */   }
/*    */   
/*    */   public DecimalMinDef inclusive(boolean inclusive) {
/* 28 */     addParameter("inclusive", Boolean.valueOf(inclusive));
/* 29 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\defs\DecimalMinDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */