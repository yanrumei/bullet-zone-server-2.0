/*    */ package org.hibernate.validator.internal.engine.messageinterpolation.el;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import javax.el.ArrayELResolver;
/*    */ import javax.el.BeanELResolver;
/*    */ import javax.el.CompositeELResolver;
/*    */ import javax.el.ELContext;
/*    */ import javax.el.ELResolver;
/*    */ import javax.el.ExpressionFactory;
/*    */ import javax.el.ListELResolver;
/*    */ import javax.el.MapELResolver;
/*    */ import javax.el.ResourceBundleELResolver;
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
/*    */ 
/*    */ public class SimpleELContext
/*    */   extends ELContext
/*    */ {
/* 27 */   private static final ELResolver DEFAULT_RESOLVER = new CompositeELResolver() {};
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private final MapBasedFunctionMapper functions;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private final VariableMapper variableMapper;
/*    */   
/*    */ 
/*    */ 
/*    */   private final ELResolver resolver;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public SimpleELContext(ExpressionFactory expressionFactory)
/*    */   {
/* 48 */     putContext(ExpressionFactory.class, expressionFactory);
/*    */     
/* 50 */     this.functions = new MapBasedFunctionMapper();
/* 51 */     this.variableMapper = new MapBasedVariableMapper();
/* 52 */     this.resolver = DEFAULT_RESOLVER;
/*    */   }
/*    */   
/*    */   public ELResolver getELResolver()
/*    */   {
/* 57 */     return this.resolver;
/*    */   }
/*    */   
/*    */   public MapBasedFunctionMapper getFunctionMapper()
/*    */   {
/* 62 */     return this.functions;
/*    */   }
/*    */   
/*    */   public VariableMapper getVariableMapper()
/*    */   {
/* 67 */     return this.variableMapper;
/*    */   }
/*    */   
/*    */   public ValueExpression setVariable(String name, ValueExpression expression) {
/* 71 */     return this.variableMapper.setVariable(name, expression);
/*    */   }
/*    */   
/*    */   public void setFunction(String prefix, String localName, Method method) {
/* 75 */     this.functions.setFunction(prefix, localName, method);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\el\SimpleELContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */