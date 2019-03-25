/*    */ package org.hibernate.validator.cfg.defs;
/*    */ 
/*    */ import org.hibernate.validator.cfg.ConstraintDef;
/*    */ import org.hibernate.validator.constraints.Range;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RangeDef
/*    */   extends ConstraintDef<RangeDef, Range>
/*    */ {
/*    */   public RangeDef()
/*    */   {
/* 18 */     super(Range.class);
/*    */   }
/*    */   
/*    */   public RangeDef min(long min) {
/* 22 */     addParameter("min", Long.valueOf(min));
/* 23 */     return this;
/*    */   }
/*    */   
/*    */   public RangeDef max(long max) {
/* 27 */     addParameter("max", Long.valueOf(max));
/* 28 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\defs\RangeDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */