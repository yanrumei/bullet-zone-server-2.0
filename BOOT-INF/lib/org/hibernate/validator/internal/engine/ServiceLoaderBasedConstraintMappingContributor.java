/*    */ package org.hibernate.validator.internal.engine;
/*    */ 
/*    */ import com.fasterxml.classmate.ResolvedType;
/*    */ import com.fasterxml.classmate.TypeResolver;
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.Type;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import org.hibernate.validator.cfg.ConstraintMapping;
/*    */ import org.hibernate.validator.cfg.context.ConstraintDefinitionContext;
/*    */ import org.hibernate.validator.internal.util.CollectionHelper;
/*    */ import org.hibernate.validator.internal.util.TypeResolutionHelper;
/*    */ import org.hibernate.validator.internal.util.privilegedactions.GetConstraintValidatorList;
/*    */ import org.hibernate.validator.spi.cfg.ConstraintMappingContributor;
/*    */ import org.hibernate.validator.spi.cfg.ConstraintMappingContributor.ConstraintMappingBuilder;
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
/*    */ public class ServiceLoaderBasedConstraintMappingContributor
/*    */   implements ConstraintMappingContributor
/*    */ {
/*    */   private final TypeResolutionHelper typeResolutionHelper;
/*    */   
/*    */   public ServiceLoaderBasedConstraintMappingContributor(TypeResolutionHelper typeResolutionHelper)
/*    */   {
/* 40 */     this.typeResolutionHelper = typeResolutionHelper;
/*    */   }
/*    */   
/*    */   public void createConstraintMappings(ConstraintMappingContributor.ConstraintMappingBuilder builder)
/*    */   {
/* 45 */     Map<Class<?>, List<Class<?>>> customValidators = CollectionHelper.newHashMap();
/*    */     
/*    */ 
/* 48 */     GetConstraintValidatorList constraintValidatorListAction = new GetConstraintValidatorList();
/* 49 */     List<ConstraintValidator<?, ?>> discoveredConstraintValidators = (List)run(constraintValidatorListAction);
/*    */     
/* 51 */     for (Iterator localIterator = discoveredConstraintValidators.iterator(); localIterator.hasNext();) { constraintValidator = (ConstraintValidator)localIterator.next();
/* 52 */       Class<?> constraintValidatorClass = constraintValidator.getClass();
/* 53 */       Class<?> annotationType = determineAnnotationType(constraintValidatorClass);
/*    */       
/* 55 */       List<Class<?>> validators = (List)customValidators.get(annotationType);
/* 56 */       if ((annotationType != null) && (validators == null)) {
/* 57 */         validators = new ArrayList();
/* 58 */         customValidators.put(annotationType, validators);
/*    */       }
/* 60 */       validators.add(constraintValidatorClass);
/*    */     }
/*    */     ConstraintValidator<?, ?> constraintValidator;
/* 63 */     ConstraintMapping constraintMapping = builder.addConstraintMapping();
/* 64 */     for (Map.Entry<Class<?>, List<Class<?>>> entry : customValidators.entrySet()) {
/* 65 */       registerConstraintDefinition(constraintMapping, (Class)entry.getKey(), (List)entry.getValue());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private <A extends Annotation> void registerConstraintDefinition(ConstraintMapping constraintMapping, Class<?> constraintType, List<Class<?>> validatorTypes)
/*    */   {
/* 75 */     ConstraintDefinitionContext<A> context = constraintMapping.constraintDefinition(constraintType).includeExistingValidators(true);
/*    */     
/* 77 */     for (Class<?> validatorType : validatorTypes) {
/* 78 */       context.validatedBy(validatorType);
/*    */     }
/*    */   }
/*    */   
/*    */   private Class<?> determineAnnotationType(Class<?> constraintValidatorClass)
/*    */   {
/* 84 */     ResolvedType resolvedType = this.typeResolutionHelper.getTypeResolver().resolve(constraintValidatorClass, new Type[0]);
/*    */     
/* 86 */     return ((ResolvedType)resolvedType.typeParametersFor(ConstraintValidator.class).get(0)).getErasedType();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private <T> T run(PrivilegedAction<T> action)
/*    */   {
/* 96 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\ServiceLoaderBasedConstraintMappingContributor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */