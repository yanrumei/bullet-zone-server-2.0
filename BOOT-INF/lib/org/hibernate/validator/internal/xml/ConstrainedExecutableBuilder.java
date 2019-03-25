/*     */ package org.hibernate.validator.internal.xml;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import javax.validation.ValidationException;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*     */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl.ConstraintType;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedParameter;
/*     */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.StringHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredConstructor;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethod;
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
/*     */ class ConstrainedExecutableBuilder
/*     */ {
/*  46 */   private static final Log log = ;
/*     */   
/*     */   private final ClassLoadingHelper classLoadingHelper;
/*     */   
/*     */   private final MetaConstraintBuilder metaConstraintBuilder;
/*     */   private final GroupConversionBuilder groupConversionBuilder;
/*     */   private final ConstrainedParameterBuilder constrainedParameterBuilder;
/*     */   private final AnnotationProcessingOptionsImpl annotationProcessingOptions;
/*     */   
/*     */   ConstrainedExecutableBuilder(ClassLoadingHelper classLoadingHelper, ParameterNameProvider parameterNameProvider, MetaConstraintBuilder metaConstraintBuilder, GroupConversionBuilder groupConversionBuilder, AnnotationProcessingOptionsImpl annotationProcessingOptions)
/*     */   {
/*  57 */     this.classLoadingHelper = classLoadingHelper;
/*  58 */     this.metaConstraintBuilder = metaConstraintBuilder;
/*  59 */     this.groupConversionBuilder = groupConversionBuilder;
/*  60 */     this.constrainedParameterBuilder = new ConstrainedParameterBuilder(metaConstraintBuilder, parameterNameProvider, groupConversionBuilder, annotationProcessingOptions);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */     this.annotationProcessingOptions = annotationProcessingOptions;
/*     */   }
/*     */   
/*     */ 
/*     */   Set<ConstrainedExecutable> buildMethodConstrainedExecutable(List<MethodType> methods, Class<?> beanClass, String defaultPackage)
/*     */   {
/*  72 */     Set<ConstrainedExecutable> constrainedExecutables = CollectionHelper.newHashSet();
/*  73 */     List<Method> alreadyProcessedMethods = CollectionHelper.newArrayList();
/*  74 */     for (MethodType methodType : methods)
/*     */     {
/*  76 */       List<Class<?>> parameterTypes = createParameterTypes(methodType
/*  77 */         .getParameter(), beanClass, defaultPackage);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  82 */       String methodName = methodType.getName();
/*     */       
/*  84 */       Method method = (Method)run(
/*  85 */         GetDeclaredMethod.action(beanClass, methodName, 
/*     */         
/*     */ 
/*  88 */         (Class[])parameterTypes.toArray(new Class[parameterTypes.size()])));
/*     */       
/*     */ 
/*     */ 
/*  92 */       if (method == null) {
/*  93 */         throw log.getBeanDoesNotContainMethodException(beanClass
/*  94 */           .getName(), methodName, parameterTypes);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 100 */       if (alreadyProcessedMethods.contains(method)) {
/* 101 */         throw log.getMethodIsDefinedTwiceInMappingXmlForBeanException(method.toString(), beanClass.getName());
/*     */       }
/*     */       
/* 104 */       alreadyProcessedMethods.add(method);
/*     */       
/*     */ 
/* 107 */       ExecutableElement methodExecutableElement = ExecutableElement.forMethod(method);
/*     */       
/*     */ 
/* 110 */       if (methodType.getIgnoreAnnotations() != null) {
/* 111 */         this.annotationProcessingOptions.ignoreConstraintAnnotationsOnMember(method, methodType
/*     */         
/* 113 */           .getIgnoreAnnotations());
/*     */       }
/*     */       
/*     */ 
/* 117 */       ConstrainedExecutable constrainedExecutable = parseExecutableType(defaultPackage, methodType
/*     */       
/* 119 */         .getParameter(), methodType
/* 120 */         .getCrossParameter(), methodType
/* 121 */         .getReturnValue(), methodExecutableElement);
/*     */       
/*     */ 
/*     */ 
/* 125 */       constrainedExecutables.add(constrainedExecutable);
/*     */     }
/* 127 */     return constrainedExecutables;
/*     */   }
/*     */   
/*     */ 
/*     */   Set<ConstrainedExecutable> buildConstructorConstrainedExecutable(List<ConstructorType> constructors, Class<?> beanClass, String defaultPackage)
/*     */   {
/* 133 */     Set<ConstrainedExecutable> constrainedExecutables = CollectionHelper.newHashSet();
/* 134 */     List<Constructor<?>> alreadyProcessedConstructors = CollectionHelper.newArrayList();
/* 135 */     for (ConstructorType constructorType : constructors)
/*     */     {
/* 137 */       List<Class<?>> constructorParameterTypes = createParameterTypes(constructorType
/* 138 */         .getParameter(), beanClass, defaultPackage);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 143 */       Constructor<?> constructor = (Constructor)run(
/* 144 */         GetDeclaredConstructor.action(beanClass, 
/*     */         
/* 146 */         (Class[])constructorParameterTypes.toArray(new Class[constructorParameterTypes.size()])));
/*     */       
/*     */ 
/*     */ 
/* 150 */       if (constructor == null) {
/* 151 */         throw log.getBeanDoesNotContainConstructorException(beanClass
/* 152 */           .getName(), 
/* 153 */           StringHelper.join(constructorParameterTypes, ", "));
/*     */       }
/*     */       
/* 156 */       if (alreadyProcessedConstructors.contains(constructor)) {
/* 157 */         throw log.getConstructorIsDefinedTwiceInMappingXmlForBeanException(constructor
/* 158 */           .toString(), beanClass
/* 159 */           .getName());
/*     */       }
/*     */       
/*     */ 
/* 163 */       alreadyProcessedConstructors.add(constructor);
/*     */       
/*     */ 
/* 166 */       ExecutableElement constructorExecutableElement = ExecutableElement.forConstructor(constructor);
/*     */       
/*     */ 
/* 169 */       if (constructorType.getIgnoreAnnotations() != null) {
/* 170 */         this.annotationProcessingOptions.ignoreConstraintAnnotationsOnMember(constructor, constructorType
/*     */         
/* 172 */           .getIgnoreAnnotations());
/*     */       }
/*     */       
/*     */ 
/* 176 */       ConstrainedExecutable constrainedExecutable = parseExecutableType(defaultPackage, constructorType
/*     */       
/* 178 */         .getParameter(), constructorType
/* 179 */         .getCrossParameter(), constructorType
/* 180 */         .getReturnValue(), constructorExecutableElement);
/*     */       
/*     */ 
/* 183 */       constrainedExecutables.add(constrainedExecutable);
/*     */     }
/* 185 */     return constrainedExecutables;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ConstrainedExecutable parseExecutableType(String defaultPackage, List<ParameterType> parameterTypeList, CrossParameterType crossParameterType, ReturnValueType returnValueType, ExecutableElement executableElement)
/*     */   {
/* 193 */     List<ConstrainedParameter> parameterMetaData = this.constrainedParameterBuilder.buildConstrainedParameters(parameterTypeList, executableElement, defaultPackage);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 199 */     Set<MetaConstraint<?>> crossParameterConstraints = parseCrossParameterConstraints(defaultPackage, crossParameterType, executableElement);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 206 */     Set<MetaConstraint<?>> returnValueConstraints = CollectionHelper.newHashSet();
/* 207 */     Map<Class<?>, Class<?>> groupConversions = CollectionHelper.newHashMap();
/* 208 */     boolean isCascaded = parseReturnValueType(returnValueType, executableElement, returnValueConstraints, groupConversions, defaultPackage);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 218 */     return new ConstrainedExecutable(ConfigurationSource.XML, 
/*     */     
/* 220 */       ConstraintLocation.forReturnValue(executableElement), parameterMetaData, crossParameterConstraints, returnValueConstraints, 
/*     */       
/*     */ 
/*     */ 
/* 224 */       Collections.emptySet(), groupConversions, isCascaded, UnwrapMode.AUTOMATIC);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Set<MetaConstraint<?>> parseCrossParameterConstraints(String defaultPackage, CrossParameterType crossParameterType, ExecutableElement executableElement)
/*     */   {
/* 235 */     Set<MetaConstraint<?>> crossParameterConstraints = CollectionHelper.newHashSet();
/* 236 */     if (crossParameterType == null) {
/* 237 */       return crossParameterConstraints;
/*     */     }
/*     */     
/* 240 */     ConstraintLocation constraintLocation = ConstraintLocation.forCrossParameter(executableElement);
/*     */     
/* 242 */     for (ConstraintType constraintType : crossParameterType.getConstraint()) {
/* 243 */       MetaConstraint<?> metaConstraint = this.metaConstraintBuilder.buildMetaConstraint(constraintLocation, constraintType, executableElement
/*     */       
/*     */ 
/* 246 */         .getElementType(), defaultPackage, ConstraintDescriptorImpl.ConstraintType.CROSS_PARAMETER);
/*     */       
/*     */ 
/*     */ 
/* 250 */       crossParameterConstraints.add(metaConstraint);
/*     */     }
/*     */     
/*     */ 
/* 254 */     if (crossParameterType.getIgnoreAnnotations() != null) {
/* 255 */       this.annotationProcessingOptions.ignoreConstraintAnnotationsForCrossParameterConstraint(executableElement
/* 256 */         .getMember(), crossParameterType
/* 257 */         .getIgnoreAnnotations());
/*     */     }
/*     */     
/*     */ 
/* 261 */     return crossParameterConstraints;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean parseReturnValueType(ReturnValueType returnValueType, ExecutableElement executableElement, Set<MetaConstraint<?>> returnValueConstraints, Map<Class<?>, Class<?>> groupConversions, String defaultPackage)
/*     */   {
/* 269 */     if (returnValueType == null) {
/* 270 */       return false;
/*     */     }
/*     */     
/* 273 */     ConstraintLocation constraintLocation = ConstraintLocation.forReturnValue(executableElement);
/* 274 */     for (ConstraintType constraint : returnValueType.getConstraint()) {
/* 275 */       MetaConstraint<?> metaConstraint = this.metaConstraintBuilder.buildMetaConstraint(constraintLocation, constraint, executableElement
/*     */       
/*     */ 
/* 278 */         .getElementType(), defaultPackage, ConstraintDescriptorImpl.ConstraintType.GENERIC);
/*     */       
/*     */ 
/*     */ 
/* 282 */       returnValueConstraints.add(metaConstraint);
/*     */     }
/* 284 */     groupConversions.putAll(this.groupConversionBuilder
/* 285 */       .buildGroupConversionMap(returnValueType
/* 286 */       .getConvertGroup(), defaultPackage));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 292 */     if (returnValueType.getIgnoreAnnotations() != null) {
/* 293 */       this.annotationProcessingOptions.ignoreConstraintAnnotationsForReturnValue(executableElement
/* 294 */         .getMember(), returnValueType
/* 295 */         .getIgnoreAnnotations());
/*     */     }
/*     */     
/*     */ 
/* 299 */     return returnValueType.getValid() != null;
/*     */   }
/*     */   
/*     */ 
/*     */   private List<Class<?>> createParameterTypes(List<ParameterType> parameterList, Class<?> beanClass, String defaultPackage)
/*     */   {
/* 305 */     List<Class<?>> parameterTypes = CollectionHelper.newArrayList();
/* 306 */     for (ParameterType parameterType : parameterList) {
/* 307 */       String type = null;
/*     */       try {
/* 309 */         type = parameterType.getType();
/* 310 */         Class<?> parameterClass = this.classLoadingHelper.loadClass(type, defaultPackage);
/* 311 */         parameterTypes.add(parameterClass);
/*     */       }
/*     */       catch (ValidationException e) {
/* 314 */         throw log.getInvalidParameterTypeException(type, beanClass.getName());
/*     */       }
/*     */     }
/*     */     
/* 318 */     return parameterTypes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 328 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ConstrainedExecutableBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */