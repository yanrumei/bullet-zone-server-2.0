/*    */ package org.hibernate.validator.cfg.defs;
/*    */ 
/*    */ import org.hibernate.validator.cfg.ConstraintDef;
/*    */ import org.hibernate.validator.constraints.Length;
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
/*    */ public class LengthDef
/*    */   extends ConstraintDef<LengthDef, Length>
/*    */ {
/*    */   public LengthDef()
/*    */   {
/* 20 */     super(Length.class);
/*    */   }
/*    */   
/*    */   public LengthDef min(int min) {
/* 24 */     addParameter("min", Integer.valueOf(min));
/* 25 */     return this;
/*    */   }
/*    */   
/*    */   public LengthDef max(int max) {
/* 29 */     addParameter("max", Integer.valueOf(max));
/* 30 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\defs\LengthDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */