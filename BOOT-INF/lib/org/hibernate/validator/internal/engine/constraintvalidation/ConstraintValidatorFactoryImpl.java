/*    */ package org.hibernate.validator.internal.engine.constraintvalidation;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorFactory;
/*    */ import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
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
/*    */ public class ConstraintValidatorFactoryImpl
/*    */   implements ConstraintValidatorFactory
/*    */ {
/*    */   public final <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key)
/*    */   {
/* 28 */     return (ConstraintValidator)run(NewInstance.action(key, "ConstraintValidator"));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void releaseInstance(ConstraintValidator<?, ?> instance) {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private <T> T run(PrivilegedAction<T> action)
/*    */   {
/* 43 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\constraintvalidation\ConstraintValidatorFactoryImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */