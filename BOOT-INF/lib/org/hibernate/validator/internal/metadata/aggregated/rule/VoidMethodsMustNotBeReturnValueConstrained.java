/*    */ package org.hibernate.validator.internal.metadata.aggregated.rule;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*    */ import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
/*    */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
/*    */ import org.hibernate.validator.internal.util.logging.Log;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VoidMethodsMustNotBeReturnValueConstrained
/*    */   extends MethodConfigurationRule
/*    */ {
/*    */   public void apply(ConstrainedExecutable method, ConstrainedExecutable otherMethod)
/*    */   {
/* 21 */     if ((method.getExecutable().getReturnType() == Void.TYPE) && (
/* 22 */       (!method.getConstraints().isEmpty()) || (method.isCascading()))) {
/* 23 */       throw log.getVoidMethodsMustNotBeConstrainedException(method.getLocation().getMember());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\rule\VoidMethodsMustNotBeReturnValueConstrained.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */