/*    */ package org.hibernate.validator.internal.engine.constraintvalidation;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import org.hibernate.validator.internal.engine.path.PathImpl;
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
/*    */ public class ConstraintViolationCreationContext
/*    */ {
/*    */   private final String message;
/*    */   private final PathImpl propertyPath;
/*    */   private final Map<String, Object> expressionVariables;
/*    */   private final Object dynamicPayload;
/*    */   
/*    */   public ConstraintViolationCreationContext(String message, PathImpl property)
/*    */   {
/* 26 */     this(message, property, Collections.emptyMap(), null);
/*    */   }
/*    */   
/*    */   public ConstraintViolationCreationContext(String message, PathImpl property, Map<String, Object> expressionVariables, Object dynamicPayload) {
/* 30 */     this.message = message;
/* 31 */     this.propertyPath = property;
/* 32 */     this.expressionVariables = expressionVariables;
/* 33 */     this.dynamicPayload = dynamicPayload;
/*    */   }
/*    */   
/*    */   public final String getMessage() {
/* 37 */     return this.message;
/*    */   }
/*    */   
/*    */   public final PathImpl getPath() {
/* 41 */     return this.propertyPath;
/*    */   }
/*    */   
/*    */   public Map<String, Object> getExpressionVariables() {
/* 45 */     return this.expressionVariables;
/*    */   }
/*    */   
/*    */   public Object getDynamicPayload() {
/* 49 */     return this.dynamicPayload;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 54 */     StringBuilder sb = new StringBuilder("ConstraintViolationCreationContext{");
/* 55 */     sb.append("message='").append(this.message).append('\'');
/* 56 */     sb.append(", propertyPath=").append(this.propertyPath);
/* 57 */     sb.append(", messageParameters=").append(this.expressionVariables);
/* 58 */     sb.append(", dynamicPayload=").append(this.dynamicPayload);
/* 59 */     sb.append('}');
/* 60 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\constraintvalidation\ConstraintViolationCreationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */