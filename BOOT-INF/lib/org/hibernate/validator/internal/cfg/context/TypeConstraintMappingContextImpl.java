/*     */ package org.hibernate.validator.internal.cfg.context;
/*     */ 
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import org.hibernate.validator.cfg.ConstraintDef;
/*     */ import org.hibernate.validator.cfg.context.ConstructorConstraintMappingContext;
/*     */ import org.hibernate.validator.cfg.context.MethodConstraintMappingContext;
/*     */ import org.hibernate.validator.cfg.context.PropertyConstraintMappingContext;
/*     */ import org.hibernate.validator.cfg.context.TypeConstraintMappingContext;
/*     */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl.ConstraintType;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedType;
/*     */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.Contracts;
/*     */ import org.hibernate.validator.internal.util.ReflectionHelper;
/*     */ import org.hibernate.validator.internal.util.StringHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.logging.Messages;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredConstructor;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredField;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethod;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
/*     */ import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
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
/*     */ public final class TypeConstraintMappingContextImpl<C>
/*     */   extends ConstraintMappingContextImplBase
/*     */   implements TypeConstraintMappingContext<C>
/*     */ {
/*  61 */   private static final Log log = ;
/*     */   
/*     */   private final Class<C> beanClass;
/*     */   
/*  65 */   private final Set<ExecutableConstraintMappingContextImpl> executableContexts = CollectionHelper.newHashSet();
/*  66 */   private final Set<PropertyConstraintMappingContextImpl> propertyContexts = CollectionHelper.newHashSet();
/*  67 */   private final Set<Member> configuredMembers = CollectionHelper.newHashSet();
/*     */   private List<Class<?>> defaultGroupSequence;
/*     */   private Class<? extends DefaultGroupSequenceProvider<? super C>> defaultGroupSequenceProviderClass;
/*     */   
/*     */   TypeConstraintMappingContextImpl(DefaultConstraintMapping mapping, Class<C> beanClass)
/*     */   {
/*  73 */     super(mapping);
/*  74 */     this.beanClass = beanClass;
/*  75 */     mapping.getAnnotationProcessingOptions().ignoreAnnotationConstraintForClass(beanClass, Boolean.FALSE);
/*     */   }
/*     */   
/*     */   public TypeConstraintMappingContext<C> constraint(ConstraintDef<?, ?> definition)
/*     */   {
/*  80 */     addConstraint(ConfiguredConstraint.forType(definition, this.beanClass));
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public TypeConstraintMappingContext<C> ignoreAnnotations()
/*     */   {
/*  86 */     return ignoreAnnotations(true);
/*     */   }
/*     */   
/*     */   public TypeConstraintMappingContext<C> ignoreAnnotations(boolean ignoreAnnotations)
/*     */   {
/*  91 */     this.mapping.getAnnotationProcessingOptions().ignoreClassLevelConstraintAnnotations(this.beanClass, ignoreAnnotations);
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public TypeConstraintMappingContext<C> ignoreAllAnnotations()
/*     */   {
/*  97 */     this.mapping.getAnnotationProcessingOptions().ignoreAnnotationConstraintForClass(this.beanClass, Boolean.TRUE);
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public TypeConstraintMappingContext<C> defaultGroupSequence(Class<?>... defaultGroupSequence)
/*     */   {
/* 103 */     this.defaultGroupSequence = Arrays.asList(defaultGroupSequence);
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public TypeConstraintMappingContext<C> defaultGroupSequenceProviderClass(Class<? extends DefaultGroupSequenceProvider<? super C>> defaultGroupSequenceProviderClass)
/*     */   {
/* 109 */     this.defaultGroupSequenceProviderClass = defaultGroupSequenceProviderClass;
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public PropertyConstraintMappingContext property(String property, ElementType elementType)
/*     */   {
/* 115 */     Contracts.assertNotNull(property, "The property name must not be null.");
/* 116 */     Contracts.assertNotNull(elementType, "The element type must not be null.");
/* 117 */     Contracts.assertNotEmpty(property, Messages.MESSAGES.propertyNameMustNotBeEmpty());
/*     */     
/* 119 */     Member member = getMember(this.beanClass, property, elementType);
/*     */     
/*     */ 
/*     */ 
/* 123 */     if ((member == null) || (member.getDeclaringClass() != this.beanClass)) {
/* 124 */       throw log.getUnableToFindPropertyWithAccessException(this.beanClass, property, elementType);
/*     */     }
/*     */     
/* 127 */     if (this.configuredMembers.contains(member)) {
/* 128 */       throw log.getPropertyHasAlreadyBeConfiguredViaProgrammaticApiException(this.beanClass.getName(), property);
/*     */     }
/*     */     
/* 131 */     PropertyConstraintMappingContextImpl context = new PropertyConstraintMappingContextImpl(this, member);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 136 */     this.configuredMembers.add(member);
/* 137 */     this.propertyContexts.add(context);
/* 138 */     return context;
/*     */   }
/*     */   
/*     */   public MethodConstraintMappingContext method(String name, Class<?>... parameterTypes)
/*     */   {
/* 143 */     Contracts.assertNotNull(name, Messages.MESSAGES.methodNameMustNotBeNull());
/*     */     
/* 145 */     Method method = (Method)run(GetDeclaredMethod.action(this.beanClass, name, parameterTypes));
/*     */     
/* 147 */     if ((method == null) || (method.getDeclaringClass() != this.beanClass)) {
/* 148 */       throw log.getUnableToFindMethodException(this.beanClass, 
/*     */       
/* 150 */         ExecutableElement.getExecutableAsString(name, parameterTypes));
/*     */     }
/*     */     
/*     */ 
/* 154 */     if (this.configuredMembers.contains(method)) {
/* 155 */       throw log.getMethodHasAlreadyBeConfiguredViaProgrammaticApiException(this.beanClass
/* 156 */         .getName(), 
/* 157 */         ExecutableElement.getExecutableAsString(name, parameterTypes));
/*     */     }
/*     */     
/*     */ 
/* 161 */     MethodConstraintMappingContextImpl context = new MethodConstraintMappingContextImpl(this, method);
/* 162 */     this.configuredMembers.add(method);
/* 163 */     this.executableContexts.add(context);
/*     */     
/* 165 */     return context;
/*     */   }
/*     */   
/*     */   public ConstructorConstraintMappingContext constructor(Class<?>... parameterTypes)
/*     */   {
/* 170 */     Constructor<C> constructor = (Constructor)run(GetDeclaredConstructor.action(this.beanClass, parameterTypes));
/*     */     
/* 172 */     if ((constructor == null) || (constructor.getDeclaringClass() != this.beanClass)) {
/* 173 */       throw log.getBeanDoesNotContainConstructorException(this.beanClass
/* 174 */         .getName(), 
/* 175 */         StringHelper.join(parameterTypes, ", "));
/*     */     }
/*     */     
/*     */ 
/* 179 */     if (this.configuredMembers.contains(constructor)) {
/* 180 */       throw log.getConstructorHasAlreadyBeConfiguredViaProgrammaticApiException(this.beanClass
/* 181 */         .getName(), 
/* 182 */         ExecutableElement.getExecutableAsString(this.beanClass.getSimpleName(), parameterTypes));
/*     */     }
/*     */     
/*     */ 
/* 186 */     ConstructorConstraintMappingContextImpl context = new ConstructorConstraintMappingContextImpl(this, constructor);
/*     */     
/*     */ 
/*     */ 
/* 190 */     this.configuredMembers.add(constructor);
/* 191 */     this.executableContexts.add(context);
/*     */     
/* 193 */     return context;
/*     */   }
/*     */   
/*     */   BeanConfiguration<C> build(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider) {
/* 197 */     return new BeanConfiguration(ConfigurationSource.API, this.beanClass, 
/*     */     
/*     */ 
/* 200 */       buildConstraintElements(constraintHelper, parameterNameProvider), this.defaultGroupSequence, 
/*     */       
/* 202 */       getDefaultGroupSequenceProvider());
/*     */   }
/*     */   
/*     */   private Set<ConstrainedElement> buildConstraintElements(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider)
/*     */   {
/* 207 */     Set<ConstrainedElement> elements = CollectionHelper.newHashSet();
/*     */     
/*     */ 
/* 210 */     elements.add(new ConstrainedType(ConfigurationSource.API, 
/*     */     
/*     */ 
/* 213 */       ConstraintLocation.forClass(this.beanClass), 
/* 214 */       getConstraints(constraintHelper)));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 219 */     for (ExecutableConstraintMappingContextImpl executableContext : this.executableContexts) {
/* 220 */       elements.add(executableContext.build(constraintHelper, parameterNameProvider));
/*     */     }
/*     */     
/*     */ 
/* 224 */     for (PropertyConstraintMappingContextImpl propertyContext : this.propertyContexts) {
/* 225 */       elements.add(propertyContext.build(constraintHelper));
/*     */     }
/*     */     
/* 228 */     return elements;
/*     */   }
/*     */   
/*     */   private DefaultGroupSequenceProvider<? super C> getDefaultGroupSequenceProvider() {
/* 232 */     return this.defaultGroupSequenceProviderClass != null ? (DefaultGroupSequenceProvider)run(
/* 233 */       NewInstance.action(this.defaultGroupSequenceProviderClass, "default group sequence provider")) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Class<?> getBeanClass()
/*     */   {
/* 241 */     return this.beanClass;
/*     */   }
/*     */   
/*     */   protected ConstraintDescriptorImpl.ConstraintType getConstraintType()
/*     */   {
/* 246 */     return ConstraintDescriptorImpl.ConstraintType.GENERIC;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Member getMember(Class<?> clazz, String property, ElementType elementType)
/*     */   {
/* 259 */     Contracts.assertNotNull(clazz, Messages.MESSAGES.classCannotBeNull());
/*     */     
/* 261 */     if ((property == null) || (property.length() == 0)) {
/* 262 */       throw log.getPropertyNameCannotBeNullOrEmptyException();
/*     */     }
/*     */     
/* 265 */     if ((!ElementType.FIELD.equals(elementType)) && (!ElementType.METHOD.equals(elementType))) {
/* 266 */       throw log.getElementTypeHasToBeFieldOrMethodException();
/*     */     }
/*     */     
/* 269 */     Member member = null;
/* 270 */     if (ElementType.FIELD.equals(elementType)) {
/* 271 */       member = (Member)run(GetDeclaredField.action(clazz, property));
/*     */     }
/*     */     else {
/* 274 */       String methodName = property.substring(0, 1).toUpperCase() + property.substring(1);
/* 275 */       for (String prefix : ReflectionHelper.PROPERTY_ACCESSOR_PREFIXES) {
/* 276 */         member = (Member)run(GetMethod.action(clazz, prefix + methodName));
/* 277 */         if (member != null) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 282 */     return member;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 292 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\cfg\context\TypeConstraintMappingContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */