/*     */ package org.hibernate.validator.internal.xml;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.validation.BootstrapConfiguration;
/*     */ import javax.validation.executable.ExecutableType;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
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
/*     */ public class BootstrapConfigurationImpl
/*     */   implements BootstrapConfiguration
/*     */ {
/*  30 */   private static final Set<ExecutableType> DEFAULT_VALIDATED_EXECUTABLE_TYPES = Collections.unmodifiableSet(
/*  31 */     EnumSet.of(ExecutableType.CONSTRUCTORS, ExecutableType.NON_GETTER_METHODS));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  38 */   private static final Set<ExecutableType> ALL_VALIDATED_EXECUTABLE_TYPES = Collections.unmodifiableSet(
/*  39 */     EnumSet.complementOf(
/*  40 */     EnumSet.of(ExecutableType.ALL, ExecutableType.NONE, ExecutableType.IMPLICIT)));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  48 */   private static final BootstrapConfiguration DEFAULT_BOOTSTRAP_CONFIGURATION = new BootstrapConfigurationImpl();
/*     */   private final String defaultProviderClassName;
/*     */   private final String constraintValidatorFactoryClassName;
/*     */   private final String messageInterpolatorClassName;
/*     */   private final String traversableResolverClassName;
/*     */   private final String parameterNameProviderClassName;
/*     */   private final Set<String> constraintMappingResourcePaths;
/*     */   private final Map<String, String> properties;
/*     */   private final Set<ExecutableType> validatedExecutableTypes;
/*     */   private final boolean isExecutableValidationEnabled;
/*     */   
/*     */   private BootstrapConfigurationImpl()
/*     */   {
/*  61 */     this.defaultProviderClassName = null;
/*  62 */     this.constraintValidatorFactoryClassName = null;
/*  63 */     this.messageInterpolatorClassName = null;
/*  64 */     this.traversableResolverClassName = null;
/*  65 */     this.parameterNameProviderClassName = null;
/*  66 */     this.validatedExecutableTypes = DEFAULT_VALIDATED_EXECUTABLE_TYPES;
/*  67 */     this.isExecutableValidationEnabled = true;
/*  68 */     this.constraintMappingResourcePaths = CollectionHelper.newHashSet();
/*  69 */     this.properties = CollectionHelper.newHashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BootstrapConfigurationImpl(String defaultProviderClassName, String constraintValidatorFactoryClassName, String messageInterpolatorClassName, String traversableResolverClassName, String parameterNameProviderClassName, EnumSet<ExecutableType> validatedExecutableTypes, boolean isExecutableValidationEnabled, Set<String> constraintMappingResourcePaths, Map<String, String> properties)
/*     */   {
/*  81 */     this.defaultProviderClassName = defaultProviderClassName;
/*  82 */     this.constraintValidatorFactoryClassName = constraintValidatorFactoryClassName;
/*  83 */     this.messageInterpolatorClassName = messageInterpolatorClassName;
/*  84 */     this.traversableResolverClassName = traversableResolverClassName;
/*  85 */     this.parameterNameProviderClassName = parameterNameProviderClassName;
/*  86 */     this.validatedExecutableTypes = prepareValidatedExecutableTypes(validatedExecutableTypes);
/*  87 */     this.isExecutableValidationEnabled = isExecutableValidationEnabled;
/*  88 */     this.constraintMappingResourcePaths = constraintMappingResourcePaths;
/*  89 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   public static BootstrapConfiguration getDefaultBootstrapConfiguration() {
/*  93 */     return DEFAULT_BOOTSTRAP_CONFIGURATION;
/*     */   }
/*     */   
/*     */   private Set<ExecutableType> prepareValidatedExecutableTypes(EnumSet<ExecutableType> validatedExecutableTypes) {
/*  97 */     if (validatedExecutableTypes == null) {
/*  98 */       return DEFAULT_VALIDATED_EXECUTABLE_TYPES;
/*     */     }
/* 100 */     if (validatedExecutableTypes.contains(ExecutableType.ALL)) {
/* 101 */       return ALL_VALIDATED_EXECUTABLE_TYPES;
/*     */     }
/* 103 */     if (validatedExecutableTypes.contains(ExecutableType.NONE)) {
/* 104 */       return EnumSet.noneOf(ExecutableType.class);
/*     */     }
/*     */     
/* 107 */     return validatedExecutableTypes;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDefaultProviderClassName()
/*     */   {
/* 113 */     return this.defaultProviderClassName;
/*     */   }
/*     */   
/*     */   public String getConstraintValidatorFactoryClassName()
/*     */   {
/* 118 */     return this.constraintValidatorFactoryClassName;
/*     */   }
/*     */   
/*     */   public String getMessageInterpolatorClassName()
/*     */   {
/* 123 */     return this.messageInterpolatorClassName;
/*     */   }
/*     */   
/*     */   public String getTraversableResolverClassName()
/*     */   {
/* 128 */     return this.traversableResolverClassName;
/*     */   }
/*     */   
/*     */   public String getParameterNameProviderClassName()
/*     */   {
/* 133 */     return this.parameterNameProviderClassName;
/*     */   }
/*     */   
/*     */   public Set<String> getConstraintMappingResourcePaths()
/*     */   {
/* 138 */     return CollectionHelper.newHashSet(this.constraintMappingResourcePaths);
/*     */   }
/*     */   
/*     */   public boolean isExecutableValidationEnabled()
/*     */   {
/* 143 */     return this.isExecutableValidationEnabled;
/*     */   }
/*     */   
/*     */   public Set<ExecutableType> getDefaultValidatedExecutableTypes()
/*     */   {
/* 148 */     return CollectionHelper.newHashSet(this.validatedExecutableTypes);
/*     */   }
/*     */   
/*     */   public Map<String, String> getProperties()
/*     */   {
/* 153 */     return CollectionHelper.newHashMap(this.properties);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 158 */     StringBuilder sb = new StringBuilder();
/* 159 */     sb.append("BootstrapConfigurationImpl");
/* 160 */     sb.append("{defaultProviderClassName='").append(this.defaultProviderClassName).append('\'');
/* 161 */     sb.append(", constraintValidatorFactoryClassName='")
/* 162 */       .append(this.constraintValidatorFactoryClassName)
/* 163 */       .append('\'');
/* 164 */     sb.append(", messageInterpolatorClassName='").append(this.messageInterpolatorClassName).append('\'');
/* 165 */     sb.append(", traversableResolverClassName='").append(this.traversableResolverClassName).append('\'');
/* 166 */     sb.append(", parameterNameProviderClassName='").append(this.parameterNameProviderClassName).append('\'');
/* 167 */     sb.append(", validatedExecutableTypes='").append(this.validatedExecutableTypes).append('\'');
/* 168 */     sb.append(", constraintMappingResourcePaths=").append(this.constraintMappingResourcePaths).append('\'');
/* 169 */     sb.append(", properties=").append(this.properties);
/* 170 */     sb.append('}');
/* 171 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\BootstrapConfigurationImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */