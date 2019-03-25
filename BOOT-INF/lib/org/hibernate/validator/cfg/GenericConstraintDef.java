/*    */ package org.hibernate.validator.cfg;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GenericConstraintDef<A extends Annotation>
/*    */   extends ConstraintDef<GenericConstraintDef<A>, A>
/*    */ {
/*    */   public GenericConstraintDef(Class<A> constraintType)
/*    */   {
/* 24 */     super(constraintType);
/*    */   }
/*    */   
/*    */   public GenericConstraintDef<A> param(String key, Object value) {
/* 28 */     addParameter(key, value);
/* 29 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\GenericConstraintDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */