/*    */ package org.hibernate.validator.cfg.defs;
/*    */ 
/*    */ import org.hibernate.validator.cfg.ConstraintDef;
/*    */ import org.hibernate.validator.constraints.EAN;
/*    */ import org.hibernate.validator.constraints.EAN.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EANDef
/*    */   extends ConstraintDef<EANDef, EAN>
/*    */ {
/*    */   public EANDef()
/*    */   {
/* 18 */     super(EAN.class);
/*    */   }
/*    */   
/*    */   public EANDef type(EAN.Type type) {
/* 22 */     addParameter("type", type);
/* 23 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\defs\EANDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */