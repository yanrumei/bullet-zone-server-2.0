/*     */ package org.hibernate.validator.internal.metadata.provider;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.AnnotatedParameterizedType;
/*     */ import java.lang.reflect.AnnotatedType;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Parameter;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import javax.validation.Valid;
/*     */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptions;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.IgnoreJava6Requirement;
/*     */ import org.hibernate.validator.internal.util.ReflectionHelper;
/*     */ import org.hibernate.validator.internal.util.TypeHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.logging.Messages;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @IgnoreJava6Requirement
/*     */ public class TypeAnnotationAwareMetaDataProvider
/*     */   extends AnnotationMetaDataProvider
/*     */ {
/*  52 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */   public TypeAnnotationAwareMetaDataProvider(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider, AnnotationProcessingOptions annotationProcessingOptions)
/*     */   {
/*  57 */     super(constraintHelper, parameterNameProvider, annotationProcessingOptions);
/*     */   }
/*     */   
/*     */   protected Set<MetaConstraint<?>> findTypeAnnotationConstraintsForMember(Member member)
/*     */   {
/*  62 */     AnnotatedType annotatedType = null;
/*     */     
/*  64 */     if ((member instanceof Field)) {
/*  65 */       annotatedType = ((Field)member).getAnnotatedType();
/*     */     }
/*     */     
/*  68 */     if ((member instanceof Method)) {
/*  69 */       annotatedType = ((Method)member).getAnnotatedReturnType();
/*     */     }
/*     */     
/*  72 */     return findTypeArgumentsConstraints(member, annotatedType, ((AccessibleObject)member)
/*     */     
/*     */ 
/*  75 */       .isAnnotationPresent(Valid.class));
/*     */   }
/*     */   
/*     */ 
/*     */   protected Set<MetaConstraint<?>> findTypeAnnotationConstraintsForExecutableParameter(Member member, int i)
/*     */   {
/*  81 */     Parameter parameter = ((java.lang.reflect.Executable)member).getParameters()[i];
/*     */     try {
/*  83 */       return findTypeArgumentsConstraints(member, parameter
/*     */       
/*  85 */         .getAnnotatedType(), parameter
/*  86 */         .isAnnotationPresent(Valid.class));
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException ex)
/*     */     {
/*  90 */       log.warn(Messages.MESSAGES.constraintOnConstructorOfNonStaticInnerClass(), ex); }
/*  91 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */   private Set<MetaConstraint<?>> findTypeArgumentsConstraints(Member member, AnnotatedType annotatedType, boolean isCascaded)
/*     */   {
/*  96 */     Optional<AnnotatedType> typeParameter = getTypeParameter(annotatedType);
/*  97 */     if (!typeParameter.isPresent()) {
/*  98 */       return Collections.emptySet();
/*     */     }
/*     */     
/* 101 */     List<ConstraintDescriptorImpl<?>> constraintDescriptors = findTypeUseConstraints(member, (AnnotatedType)typeParameter.get());
/* 102 */     if (constraintDescriptors.isEmpty()) {
/* 103 */       return Collections.emptySet();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 111 */     Type validatedType = annotatedType.getType();
/* 112 */     if ((ReflectionHelper.isIterable(annotatedType.getType())) || (ReflectionHelper.isMap(annotatedType.getType()))) {
/* 113 */       if (!isCascaded) {
/* 114 */         throw log.getTypeAnnotationConstraintOnIterableRequiresUseOfValidAnnotationException(member
/* 115 */           .getDeclaringClass().getName(), member
/* 116 */           .getName());
/*     */       }
/*     */       
/* 119 */       validatedType = ((AnnotatedType)typeParameter.get()).getType();
/*     */     }
/*     */     
/* 122 */     return convertToTypeArgumentMetaConstraints(constraintDescriptors, member, validatedType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<ConstraintDescriptorImpl<?>> findTypeUseConstraints(Member member, AnnotatedType typeArgument)
/*     */   {
/* 133 */     List<ConstraintDescriptorImpl<?>> metaData = CollectionHelper.newArrayList();
/*     */     
/* 135 */     for (Annotation annotation : typeArgument.getAnnotations()) {
/* 136 */       metaData.addAll(findConstraintAnnotations(member, annotation, ElementType.TYPE_USE));
/*     */     }
/*     */     
/* 139 */     return metaData;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Set<MetaConstraint<?>> convertToTypeArgumentMetaConstraints(List<ConstraintDescriptorImpl<?>> constraintDescriptors, Member member, Type type)
/*     */   {
/* 146 */     Set<MetaConstraint<?>> constraints = CollectionHelper.newHashSet(constraintDescriptors.size());
/* 147 */     for (ConstraintDescriptorImpl<?> constraintDescription : constraintDescriptors) {
/* 148 */       MetaConstraint<?> metaConstraint = createTypeArgumentMetaConstraint(member, constraintDescription, type);
/* 149 */       constraints.add(metaConstraint);
/*     */     }
/* 151 */     return constraints;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private <A extends Annotation> MetaConstraint<?> createTypeArgumentMetaConstraint(Member member, ConstraintDescriptorImpl<A> descriptor, Type type)
/*     */   {
/* 158 */     return new MetaConstraint(descriptor, ConstraintLocation.forTypeArgument(member, type));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Optional<AnnotatedType> getTypeParameter(AnnotatedType annotatedType)
/*     */   {
/* 167 */     if (annotatedType == null) {
/* 168 */       return Optional.empty();
/*     */     }
/*     */     
/* 171 */     if (!TypeHelper.isAssignable(AnnotatedParameterizedType.class, annotatedType.getClass())) {
/* 172 */       return Optional.empty();
/*     */     }
/*     */     
/* 175 */     AnnotatedType[] annotatedArguments = ((AnnotatedParameterizedType)annotatedType).getAnnotatedActualTypeArguments();
/*     */     
/*     */ 
/* 178 */     if (annotatedArguments.length == 1) {
/* 179 */       return Optional.of(annotatedArguments[0]);
/*     */     }
/*     */     
/*     */ 
/* 183 */     if (annotatedArguments.length > 1)
/*     */     {
/*     */ 
/* 186 */       if (ReflectionHelper.isMap(annotatedType.getType())) {
/* 187 */         return Optional.of(annotatedArguments[1]);
/*     */       }
/*     */       
/*     */ 
/* 191 */       log.parameterizedTypeWithMoreThanOneTypeArgumentIsNotSupported(annotatedType.getType().getTypeName());
/*     */     }
/*     */     
/* 194 */     return Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\provider\TypeAnnotationAwareMetaDataProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */