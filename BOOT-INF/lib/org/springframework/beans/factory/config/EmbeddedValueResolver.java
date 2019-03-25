/*    */ package org.springframework.beans.factory.config;
/*    */ 
/*    */ import org.springframework.util.StringValueResolver;
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
/*    */ public class EmbeddedValueResolver
/*    */   implements StringValueResolver
/*    */ {
/*    */   private final BeanExpressionContext exprContext;
/*    */   private final BeanExpressionResolver exprResolver;
/*    */   
/*    */   public EmbeddedValueResolver(ConfigurableBeanFactory beanFactory)
/*    */   {
/* 44 */     this.exprContext = new BeanExpressionContext(beanFactory, null);
/* 45 */     this.exprResolver = beanFactory.getBeanExpressionResolver();
/*    */   }
/*    */   
/*    */ 
/*    */   public String resolveStringValue(String strVal)
/*    */   {
/* 51 */     String value = this.exprContext.getBeanFactory().resolveEmbeddedValue(strVal);
/* 52 */     if ((this.exprResolver != null) && (value != null)) {
/* 53 */       Object evaluated = this.exprResolver.evaluate(value, this.exprContext);
/* 54 */       value = evaluated != null ? evaluated.toString() : null;
/*    */     }
/* 56 */     return value;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\EmbeddedValueResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */