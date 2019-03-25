/*    */ package org.hibernate.validator.internal.metadata.aggregated.rule;
/*    */ 
/*    */ import java.util.Map;
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
/*    */ public class ParallelMethodsMustNotDefineGroupConversionForCascadedReturnValue
/*    */   extends MethodConfigurationRule
/*    */ {
/*    */   public void apply(ConstrainedExecutable method, ConstrainedExecutable otherMethod)
/*    */   {
/* 21 */     boolean isCascaded = (method.isCascading()) || (otherMethod.isCascading());
/*    */     
/* 23 */     boolean hasGroupConversions = (!method.getGroupConversions().isEmpty()) || (!otherMethod.getGroupConversions().isEmpty());
/*    */     
/* 25 */     if ((isDefinedOnParallelType(method, otherMethod)) && (isCascaded) && (hasGroupConversions)) {
/* 26 */       throw log.getMethodsFromParallelTypesMustNotDefineGroupConversionsForCascadedReturnValueException(method
/* 27 */         .getLocation().getMember(), otherMethod
/* 28 */         .getLocation().getMember());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\rule\ParallelMethodsMustNotDefineGroupConversionForCascadedReturnValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */