/*    */ package org.springframework.validation.beanvalidation;
/*    */ 
/*    */ import javax.validation.ValidationException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class OptionalValidatorFactoryBean
/*    */   extends LocalValidatorFactoryBean
/*    */ {
/*    */   public void afterPropertiesSet()
/*    */   {
/*    */     try
/*    */     {
/* 40 */       super.afterPropertiesSet();
/*    */     }
/*    */     catch (ValidationException ex) {
/* 43 */       LogFactory.getLog(getClass()).debug("Failed to set up a Bean Validation provider", ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\beanvalidation\OptionalValidatorFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */