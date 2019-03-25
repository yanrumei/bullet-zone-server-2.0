/*    */ package org.hibernate.validator.cfg;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.validation.Payload;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ConstraintDef<C extends ConstraintDef<C, A>, A extends Annotation>
/*    */ {
/*    */   protected final Class<A> constraintType;
/*    */   protected final Map<String, Object> parameters;
/*    */   
/*    */   protected ConstraintDef(Class<A> constraintType)
/*    */   {
/* 47 */     this.constraintType = constraintType;
/* 48 */     this.parameters = new HashMap();
/*    */   }
/*    */   
/*    */   protected ConstraintDef(ConstraintDef<?, A> original) {
/* 52 */     this.constraintType = original.constraintType;
/* 53 */     this.parameters = original.parameters;
/*    */   }
/*    */   
/*    */   private C getThis()
/*    */   {
/* 58 */     return this;
/*    */   }
/*    */   
/*    */   protected C addParameter(String key, Object value) {
/* 62 */     this.parameters.put(key, value);
/* 63 */     return getThis();
/*    */   }
/*    */   
/*    */   public C message(String message) {
/* 67 */     addParameter("message", message);
/* 68 */     return getThis();
/*    */   }
/*    */   
/*    */   public C groups(Class<?>... groups) {
/* 72 */     addParameter("groups", groups);
/* 73 */     return getThis();
/*    */   }
/*    */   
/*    */   public C payload(Class<? extends Payload>... payload) {
/* 77 */     addParameter("payload", payload);
/* 78 */     return getThis();
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 83 */     StringBuilder sb = new StringBuilder();
/* 84 */     sb.append(getClass().getName());
/* 85 */     sb.append(", constraintType=").append(this.constraintType);
/* 86 */     sb.append(", parameters=").append(this.parameters);
/* 87 */     sb.append('}');
/* 88 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\ConstraintDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */