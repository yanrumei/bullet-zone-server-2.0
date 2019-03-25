/*    */ package org.hibernate.validator.cfg.defs;
/*    */ 
/*    */ import javax.validation.constraints.Min;
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
/*    */ public class MinDef
/*    */   extends ConstraintDef<MinDef, Min>
/*    */ {
/*    */   public MinDef()
/*    */   {
/* 19 */     super(Min.class);
/*    */   }
/*    */   
/*    */   public MinDef value(long min) {
/* 23 */     addParameter("value", Long.valueOf(min));
/* 24 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\defs\MinDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */