/*    */ package org.hibernate.validator.cfg.defs;
/*    */ 
/*    */ import javax.validation.constraints.Size;
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
/*    */ public class SizeDef
/*    */   extends ConstraintDef<SizeDef, Size>
/*    */ {
/*    */   public SizeDef()
/*    */   {
/* 20 */     super(Size.class);
/*    */   }
/*    */   
/*    */   public SizeDef min(int min) {
/* 24 */     addParameter("min", Integer.valueOf(min));
/* 25 */     return this;
/*    */   }
/*    */   
/*    */   public SizeDef max(int max) {
/* 29 */     addParameter("max", Integer.valueOf(max));
/* 30 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\defs\SizeDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */