/*    */ package org.springframework.web.bind.support;
/*    */ 
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorFactory;
/*    */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*    */ import org.springframework.web.context.ContextLoader;
/*    */ import org.springframework.web.context.WebApplicationContext;
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
/*    */ public class SpringWebConstraintValidatorFactory
/*    */   implements ConstraintValidatorFactory
/*    */ {
/*    */   public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key)
/*    */   {
/* 44 */     return (ConstraintValidator)getWebApplicationContext().getAutowireCapableBeanFactory().createBean(key);
/*    */   }
/*    */   
/*    */   public void releaseInstance(ConstraintValidator<?, ?> instance)
/*    */   {
/* 49 */     getWebApplicationContext().getAutowireCapableBeanFactory().destroyBean(instance);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected WebApplicationContext getWebApplicationContext()
/*    */   {
/* 61 */     WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
/* 62 */     if (wac == null) {
/* 63 */       throw new IllegalStateException("No WebApplicationContext registered for current thread - consider overriding SpringWebConstraintValidatorFactory.getWebApplicationContext()");
/*    */     }
/*    */     
/* 66 */     return wac;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\support\SpringWebConstraintValidatorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */