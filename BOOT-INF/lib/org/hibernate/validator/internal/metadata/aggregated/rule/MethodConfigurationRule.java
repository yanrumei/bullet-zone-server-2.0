/*    */ package org.hibernate.validator.internal.metadata.aggregated.rule;
/*    */ 
/*    */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*    */ import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
/*    */ import org.hibernate.validator.internal.util.logging.Log;
/*    */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*    */ public abstract class MethodConfigurationRule
/*    */ {
/* 20 */   protected static final Log log = ;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract void apply(ConstrainedExecutable paramConstrainedExecutable1, ConstrainedExecutable paramConstrainedExecutable2);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean isStrictSubType(Class<?> clazz, Class<?> otherClazz)
/*    */   {
/* 41 */     return (clazz.isAssignableFrom(otherClazz)) && (!clazz.equals(otherClazz));
/*    */   }
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
/*    */   protected boolean isDefinedOnSubType(ConstrainedExecutable executable, ConstrainedExecutable otherExecutable)
/*    */   {
/* 55 */     Class<?> clazz = executable.getLocation().getDeclaringClass();
/* 56 */     Class<?> otherClazz = otherExecutable.getLocation().getDeclaringClass();
/*    */     
/* 58 */     return isStrictSubType(clazz, otherClazz);
/*    */   }
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
/*    */   protected boolean isDefinedOnParallelType(ConstrainedExecutable executable, ConstrainedExecutable otherExecutable)
/*    */   {
/* 72 */     Class<?> clazz = executable.getLocation().getDeclaringClass();
/* 73 */     Class<?> otherClazz = otherExecutable.getLocation().getDeclaringClass();
/*    */     
/* 75 */     return (!clazz.isAssignableFrom(otherClazz)) && (!otherClazz.isAssignableFrom(clazz));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\rule\MethodConfigurationRule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */