/*    */ package org.hibernate.validator.internal.engine.messageinterpolation.el;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.el.ValueExpression;
/*    */ import javax.el.VariableMapper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MapBasedVariableMapper
/*    */   extends VariableMapper
/*    */ {
/* 19 */   private Map<String, ValueExpression> map = Collections.emptyMap();
/*    */   
/*    */   public ValueExpression resolveVariable(String variable)
/*    */   {
/* 23 */     return (ValueExpression)this.map.get(variable);
/*    */   }
/*    */   
/*    */   public ValueExpression setVariable(String variable, ValueExpression expression)
/*    */   {
/* 28 */     if (this.map.isEmpty()) {
/* 29 */       this.map = new HashMap();
/*    */     }
/* 31 */     return (ValueExpression)this.map.put(variable, expression);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\el\MapBasedVariableMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */