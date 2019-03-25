/*    */ package org.hibernate.validator.internal.metadata.aggregated.rule;
/*    */ 
/*    */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*    */ import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
/*    */ import org.hibernate.validator.internal.util.logging.Log;
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
/*    */ public class ParallelMethodsMustNotDefineParameterConstraints
/*    */   extends MethodConfigurationRule
/*    */ {
/*    */   public void apply(ConstrainedExecutable method, ConstrainedExecutable otherMethod)
/*    */   {
/* 21 */     if ((isDefinedOnParallelType(method, otherMethod)) && (
/* 22 */       (method.hasParameterConstraints()) || (otherMethod.hasParameterConstraints()))) {
/* 23 */       throw log.getParameterConstraintsDefinedInMethodsFromParallelTypesException(method
/* 24 */         .getLocation().getMember(), otherMethod
/* 25 */         .getLocation().getMember());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\rule\ParallelMethodsMustNotDefineParameterConstraints.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */