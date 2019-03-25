/*    */ package org.hibernate.validator.cfg.defs;
/*    */ 
/*    */ import javax.validation.constraints.Max;
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
/*    */ public class MaxDef
/*    */   extends ConstraintDef<MaxDef, Max>
/*    */ {
/*    */   public MaxDef()
/*    */   {
/* 19 */     super(Max.class);
/*    */   }
/*    */   
/*    */   public MaxDef value(long max) {
/* 23 */     addParameter("value", Long.valueOf(max));
/* 24 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\defs\MaxDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */