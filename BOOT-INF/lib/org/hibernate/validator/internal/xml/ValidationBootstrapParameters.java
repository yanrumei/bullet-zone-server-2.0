/*     */ package org.hibernate.validator.internal.xml;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.validation.BootstrapConfiguration;
/*     */ import javax.validation.ConstraintValidatorFactory;
/*     */ import javax.validation.MessageInterpolator;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import javax.validation.TraversableResolver;
/*     */ import javax.validation.ValidationException;
/*     */ import javax.validation.spi.ValidationProvider;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.LoadClass;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
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
/*     */ public class ValidationBootstrapParameters
/*     */ {
/*  36 */   private static final Log log = ;
/*     */   
/*     */   private ConstraintValidatorFactory constraintValidatorFactory;
/*     */   private MessageInterpolator messageInterpolator;
/*     */   private TraversableResolver traversableResolver;
/*     */   private ParameterNameProvider parameterNameProvider;
/*     */   private ValidationProvider<?> provider;
/*  43 */   private Class<? extends ValidationProvider<?>> providerClass = null;
/*  44 */   private final Map<String, String> configProperties = CollectionHelper.newHashMap();
/*  45 */   private final Set<InputStream> mappings = CollectionHelper.newHashSet();
/*     */   
/*     */   public ValidationBootstrapParameters() {}
/*     */   
/*     */   public ValidationBootstrapParameters(BootstrapConfiguration bootstrapConfiguration, ClassLoader externalClassLoader)
/*     */   {
/*  51 */     setProviderClass(bootstrapConfiguration.getDefaultProviderClassName(), externalClassLoader);
/*  52 */     setMessageInterpolator(bootstrapConfiguration.getMessageInterpolatorClassName(), externalClassLoader);
/*  53 */     setTraversableResolver(bootstrapConfiguration.getTraversableResolverClassName(), externalClassLoader);
/*  54 */     setConstraintFactory(bootstrapConfiguration.getConstraintValidatorFactoryClassName(), externalClassLoader);
/*  55 */     setParameterNameProvider(bootstrapConfiguration.getParameterNameProviderClassName(), externalClassLoader);
/*  56 */     setMappingStreams(bootstrapConfiguration.getConstraintMappingResourcePaths(), externalClassLoader);
/*  57 */     setConfigProperties(bootstrapConfiguration.getProperties());
/*     */   }
/*     */   
/*     */   public final ConstraintValidatorFactory getConstraintValidatorFactory() {
/*  61 */     return this.constraintValidatorFactory;
/*     */   }
/*     */   
/*     */   public final void setConstraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory) {
/*  65 */     this.constraintValidatorFactory = constraintValidatorFactory;
/*     */   }
/*     */   
/*     */   public final MessageInterpolator getMessageInterpolator() {
/*  69 */     return this.messageInterpolator;
/*     */   }
/*     */   
/*     */   public final void setMessageInterpolator(MessageInterpolator messageInterpolator) {
/*  73 */     this.messageInterpolator = messageInterpolator;
/*     */   }
/*     */   
/*     */   public final ValidationProvider<?> getProvider() {
/*  77 */     return this.provider;
/*     */   }
/*     */   
/*     */   public final void setProvider(ValidationProvider<?> provider) {
/*  81 */     this.provider = provider;
/*     */   }
/*     */   
/*     */   public final Class<? extends ValidationProvider<?>> getProviderClass() {
/*  85 */     return this.providerClass;
/*     */   }
/*     */   
/*     */   public final void setProviderClass(Class<? extends ValidationProvider<?>> providerClass) {
/*  89 */     this.providerClass = providerClass;
/*     */   }
/*     */   
/*     */   public final TraversableResolver getTraversableResolver() {
/*  93 */     return this.traversableResolver;
/*     */   }
/*     */   
/*     */   public final void setTraversableResolver(TraversableResolver traversableResolver) {
/*  97 */     this.traversableResolver = traversableResolver;
/*     */   }
/*     */   
/*     */   public final void addConfigProperty(String key, String value) {
/* 101 */     this.configProperties.put(key, value);
/*     */   }
/*     */   
/*     */   public final void addMapping(InputStream in) {
/* 105 */     this.mappings.add(in);
/*     */   }
/*     */   
/*     */   public final void addAllMappings(Set<InputStream> mappings) {
/* 109 */     this.mappings.addAll(mappings);
/*     */   }
/*     */   
/*     */   public final Set<InputStream> getMappings() {
/* 113 */     return Collections.unmodifiableSet(this.mappings);
/*     */   }
/*     */   
/*     */   public final Map<String, String> getConfigProperties() {
/* 117 */     return Collections.unmodifiableMap(this.configProperties);
/*     */   }
/*     */   
/*     */   public ParameterNameProvider getParameterNameProvider() {
/* 121 */     return this.parameterNameProvider;
/*     */   }
/*     */   
/*     */   public void setParameterNameProvider(ParameterNameProvider parameterNameProvider) {
/* 125 */     this.parameterNameProvider = parameterNameProvider;
/*     */   }
/*     */   
/*     */   private void setProviderClass(String providerFqcn, ClassLoader externalClassLoader)
/*     */   {
/* 130 */     if (providerFqcn != null) {
/*     */       try {
/* 132 */         this.providerClass = ((Class)run(
/* 133 */           LoadClass.action(providerFqcn, externalClassLoader)));
/*     */         
/* 135 */         log.usingValidationProvider(providerFqcn);
/*     */       }
/*     */       catch (Exception e) {
/* 138 */         throw log.getUnableToInstantiateValidationProviderClassException(providerFqcn, e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void setMessageInterpolator(String messageInterpolatorFqcn, ClassLoader externalClassLoader) {
/* 144 */     if (messageInterpolatorFqcn != null) {
/*     */       try
/*     */       {
/* 147 */         Class<MessageInterpolator> messageInterpolatorClass = (Class)run(
/* 148 */           LoadClass.action(messageInterpolatorFqcn, externalClassLoader));
/*     */         
/* 150 */         this.messageInterpolator = ((MessageInterpolator)run(NewInstance.action(messageInterpolatorClass, "message interpolator")));
/* 151 */         log.usingMessageInterpolator(messageInterpolatorFqcn);
/*     */       }
/*     */       catch (ValidationException e) {
/* 154 */         throw log.getUnableToInstantiateMessageInterpolatorClassException(messageInterpolatorFqcn, e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void setTraversableResolver(String traversableResolverFqcn, ClassLoader externalClassLoader) {
/* 160 */     if (traversableResolverFqcn != null) {
/*     */       try
/*     */       {
/* 163 */         Class<TraversableResolver> clazz = (Class)run(
/* 164 */           LoadClass.action(traversableResolverFqcn, externalClassLoader));
/*     */         
/* 166 */         this.traversableResolver = ((TraversableResolver)run(NewInstance.action(clazz, "traversable resolver")));
/* 167 */         log.usingTraversableResolver(traversableResolverFqcn);
/*     */       }
/*     */       catch (ValidationException e) {
/* 170 */         throw log.getUnableToInstantiateTraversableResolverClassException(traversableResolverFqcn, e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void setConstraintFactory(String constraintFactoryFqcn, ClassLoader externalClassLoader) {
/* 176 */     if (constraintFactoryFqcn != null) {
/*     */       try
/*     */       {
/* 179 */         Class<ConstraintValidatorFactory> clazz = (Class)run(
/* 180 */           LoadClass.action(constraintFactoryFqcn, externalClassLoader));
/*     */         
/* 182 */         this.constraintValidatorFactory = ((ConstraintValidatorFactory)run(NewInstance.action(clazz, "constraint factory class")));
/* 183 */         log.usingConstraintFactory(constraintFactoryFqcn);
/*     */       }
/*     */       catch (ValidationException e) {
/* 186 */         throw log.getUnableToInstantiateConstraintFactoryClassException(constraintFactoryFqcn, e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void setParameterNameProvider(String parameterNameProviderFqcn, ClassLoader externalClassLoader) {
/* 192 */     if (parameterNameProviderFqcn != null) {
/*     */       try
/*     */       {
/* 195 */         Class<ParameterNameProvider> clazz = (Class)run(
/* 196 */           LoadClass.action(parameterNameProviderFqcn, externalClassLoader));
/*     */         
/* 198 */         this.parameterNameProvider = ((ParameterNameProvider)run(NewInstance.action(clazz, "parameter name provider class")));
/* 199 */         log.usingParameterNameProvider(parameterNameProviderFqcn);
/*     */       }
/*     */       catch (ValidationException e) {
/* 202 */         throw log.getUnableToInstantiateParameterNameProviderClassException(parameterNameProviderFqcn, e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void setMappingStreams(Set<String> mappingFileNames, ClassLoader externalClassLoader) {
/* 208 */     for (String mappingFileName : mappingFileNames) {
/* 209 */       log.debugf("Trying to open input stream for %s.", mappingFileName);
/*     */       
/* 211 */       InputStream in = ResourceLoaderHelper.getResettableInputStreamForPath(mappingFileName, externalClassLoader);
/* 212 */       if (in == null) {
/* 213 */         throw log.getUnableToOpenInputStreamForMappingFileException(mappingFileName);
/*     */       }
/* 215 */       this.mappings.add(in);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setConfigProperties(Map<String, String> properties) {
/* 220 */     for (Map.Entry<String, String> entry : properties.entrySet()) {
/* 221 */       this.configProperties.put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 232 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ValidationBootstrapParameters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */