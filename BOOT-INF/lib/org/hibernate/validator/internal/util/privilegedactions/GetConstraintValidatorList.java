/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.ServiceConfigurationError;
/*    */ import java.util.ServiceLoader;
/*    */ import javax.validation.ConstraintValidator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GetConstraintValidatorList
/*    */   implements PrivilegedAction<List<ConstraintValidator<?, ?>>>
/*    */ {
/*    */   public static List<ConstraintValidator<?, ?>> getConstraintValidatorList()
/*    */   {
/* 24 */     GetConstraintValidatorList action = new GetConstraintValidatorList();
/* 25 */     if (System.getSecurityManager() != null) {
/* 26 */       return (List)AccessController.doPrivileged(action);
/*    */     }
/*    */     
/* 29 */     return action.run();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public List<ConstraintValidator<?, ?>> run()
/*    */   {
/* 36 */     ClassLoader classloader = Thread.currentThread().getContextClassLoader();
/* 37 */     List<ConstraintValidator<?, ?>> constraintValidatorList = loadConstraintValidators(classloader);
/*    */     
/*    */ 
/* 40 */     if (constraintValidatorList.isEmpty()) {
/* 41 */       classloader = GetConstraintValidatorList.class.getClassLoader();
/* 42 */       constraintValidatorList = loadConstraintValidators(classloader);
/*    */     }
/*    */     
/* 45 */     return constraintValidatorList;
/*    */   }
/*    */   
/*    */   private List<ConstraintValidator<?, ?>> loadConstraintValidators(ClassLoader classloader) {
/* 49 */     ServiceLoader<ConstraintValidator> loader = ServiceLoader.load(ConstraintValidator.class, classloader);
/* 50 */     Iterator<ConstraintValidator> constraintValidatorIterator = loader.iterator();
/* 51 */     List<ConstraintValidator<?, ?>> constraintValidators = new ArrayList();
/* 52 */     while (constraintValidatorIterator.hasNext()) {
/*    */       try {
/* 54 */         constraintValidators.add(constraintValidatorIterator.next());
/*    */       }
/*    */       catch (ServiceConfigurationError localServiceConfigurationError) {}
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 62 */     return constraintValidators;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\GetConstraintValidatorList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */