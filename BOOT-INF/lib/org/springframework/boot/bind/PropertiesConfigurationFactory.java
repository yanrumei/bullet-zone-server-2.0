/*     */ package org.springframework.boot.bind;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceAware;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.env.PropertySources;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.validation.DataBinder;
/*     */ import org.springframework.validation.ObjectError;
/*     */ import org.springframework.validation.Validator;
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
/*     */ public class PropertiesConfigurationFactory<T>
/*     */   implements FactoryBean<T>, MessageSourceAware, InitializingBean
/*     */ {
/*  57 */   private static final char[] EXACT_DELIMITERS = { '_', '.', '[' };
/*     */   
/*  59 */   private static final char[] TARGET_NAME_DELIMITERS = { '_', '.' };
/*     */   
/*     */ 
/*  62 */   private static final Log logger = LogFactory.getLog(PropertiesConfigurationFactory.class);
/*     */   
/*  64 */   private boolean ignoreUnknownFields = true;
/*     */   
/*     */   private boolean ignoreInvalidFields;
/*     */   
/*  68 */   private boolean exceptionIfInvalid = true;
/*     */   
/*     */   private PropertySources propertySources;
/*     */   
/*     */   private final T target;
/*     */   
/*     */   private Validator validator;
/*     */   
/*     */   private MessageSource messageSource;
/*     */   
/*  78 */   private boolean hasBeenBound = false;
/*     */   
/*  80 */   private boolean ignoreNestedProperties = false;
/*     */   
/*     */   private String targetName;
/*     */   
/*     */   private ConversionService conversionService;
/*     */   
/*  86 */   private boolean resolvePlaceholders = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertiesConfigurationFactory(T target)
/*     */   {
/*  94 */     Assert.notNull(target, "target must not be null");
/*  95 */     this.target = target;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertiesConfigurationFactory(Class<?> type)
/*     */   {
/* 105 */     Assert.notNull(type, "type must not be null");
/* 106 */     this.target = BeanUtils.instantiate(type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIgnoreNestedProperties(boolean ignoreNestedProperties)
/*     */   {
/* 116 */     this.ignoreNestedProperties = ignoreNestedProperties;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIgnoreUnknownFields(boolean ignoreUnknownFields)
/*     */   {
/* 128 */     this.ignoreUnknownFields = ignoreUnknownFields;
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
/*     */   public void setIgnoreInvalidFields(boolean ignoreInvalidFields)
/*     */   {
/* 141 */     this.ignoreInvalidFields = ignoreInvalidFields;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTargetName(String targetName)
/*     */   {
/* 149 */     this.targetName = targetName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMessageSource(MessageSource messageSource)
/*     */   {
/* 158 */     this.messageSource = messageSource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPropertySources(PropertySources propertySources)
/*     */   {
/* 166 */     this.propertySources = propertySources;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConversionService(ConversionService conversionService)
/*     */   {
/* 174 */     this.conversionService = conversionService;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValidator(Validator validator)
/*     */   {
/* 182 */     this.validator = validator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setExceptionIfInvalid(boolean exceptionIfInvalid)
/*     */   {
/* 194 */     this.exceptionIfInvalid = exceptionIfInvalid;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResolvePlaceholders(boolean resolvePlaceholders)
/*     */   {
/* 203 */     this.resolvePlaceholders = resolvePlaceholders;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet() throws Exception
/*     */   {
/* 208 */     bindPropertiesToTarget();
/*     */   }
/*     */   
/*     */   public Class<?> getObjectType()
/*     */   {
/* 213 */     if (this.target == null) {
/* 214 */       return Object.class;
/*     */     }
/* 216 */     return this.target.getClass();
/*     */   }
/*     */   
/*     */   public boolean isSingleton()
/*     */   {
/* 221 */     return true;
/*     */   }
/*     */   
/*     */   public T getObject() throws Exception
/*     */   {
/* 226 */     if (!this.hasBeenBound) {
/* 227 */       bindPropertiesToTarget();
/*     */     }
/* 229 */     return (T)this.target;
/*     */   }
/*     */   
/*     */   public void bindPropertiesToTarget() throws BindException {
/* 233 */     Assert.state(this.propertySources != null, "PropertySources should not be null");
/*     */     try {
/* 235 */       if (logger.isTraceEnabled()) {
/* 236 */         logger.trace("Property Sources: " + this.propertySources);
/*     */       }
/*     */       
/* 239 */       this.hasBeenBound = true;
/* 240 */       doBindPropertiesToTarget();
/*     */     }
/*     */     catch (BindException ex) {
/* 243 */       if (this.exceptionIfInvalid) {
/* 244 */         throw ex;
/*     */       }
/*     */       
/* 247 */       logger.error("Failed to load Properties validation bean. Your Properties may be invalid.", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private void doBindPropertiesToTarget() throws BindException
/*     */   {
/* 253 */     RelaxedDataBinder dataBinder = this.targetName != null ? new RelaxedDataBinder(this.target, this.targetName) : new RelaxedDataBinder(this.target);
/*     */     
/*     */ 
/* 256 */     if ((this.validator != null) && 
/* 257 */       (this.validator.supports(dataBinder.getTarget().getClass()))) {
/* 258 */       dataBinder.setValidator(this.validator);
/*     */     }
/* 260 */     if (this.conversionService != null) {
/* 261 */       dataBinder.setConversionService(this.conversionService);
/*     */     }
/* 263 */     dataBinder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
/* 264 */     dataBinder.setIgnoreNestedProperties(this.ignoreNestedProperties);
/* 265 */     dataBinder.setIgnoreInvalidFields(this.ignoreInvalidFields);
/* 266 */     dataBinder.setIgnoreUnknownFields(this.ignoreUnknownFields);
/* 267 */     customizeBinder(dataBinder);
/* 268 */     Iterable<String> relaxedTargetNames = getRelaxedTargetNames();
/* 269 */     Set<String> names = getNames(relaxedTargetNames);
/* 270 */     PropertyValues propertyValues = getPropertySourcesPropertyValues(names, relaxedTargetNames);
/*     */     
/* 272 */     dataBinder.bind(propertyValues);
/* 273 */     if (this.validator != null) {
/* 274 */       dataBinder.validate();
/*     */     }
/* 276 */     checkForBindingErrors(dataBinder);
/*     */   }
/*     */   
/*     */   private Iterable<String> getRelaxedTargetNames() {
/* 280 */     return (this.target != null) && (StringUtils.hasLength(this.targetName)) ? new RelaxedNames(this.targetName) : null;
/*     */   }
/*     */   
/*     */   private Set<String> getNames(Iterable<String> prefixes)
/*     */   {
/* 285 */     Set<String> names = new LinkedHashSet();
/* 286 */     if (this.target != null)
/*     */     {
/* 288 */       PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(this.target.getClass());
/* 289 */       RelaxedNames relaxedNames; String prefix; for (PropertyDescriptor descriptor : descriptors) {
/* 290 */         String name = descriptor.getName();
/* 291 */         if (!name.equals("class")) {
/* 292 */           relaxedNames = RelaxedNames.forCamelCase(name);
/* 293 */           if (prefixes == null) {
/* 294 */             for (String relaxedName : relaxedNames) {
/* 295 */               names.add(relaxedName);
/*     */             }
/*     */             
/*     */           } else {
/* 299 */             for (??? = prefixes.iterator(); ???.hasNext();) { prefix = (String)???.next();
/* 300 */               for (String relaxedName : relaxedNames) {
/* 301 */                 names.add(prefix + "." + relaxedName);
/* 302 */                 names.add(prefix + "_" + relaxedName);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 309 */     return names;
/*     */   }
/*     */   
/*     */   private PropertyValues getPropertySourcesPropertyValues(Set<String> names, Iterable<String> relaxedTargetNames)
/*     */   {
/* 314 */     PropertyNamePatternsMatcher includes = getPropertyNamePatternsMatcher(names, relaxedTargetNames);
/*     */     
/* 316 */     return new PropertySourcesPropertyValues(this.propertySources, names, includes, this.resolvePlaceholders);
/*     */   }
/*     */   
/*     */ 
/*     */   private PropertyNamePatternsMatcher getPropertyNamePatternsMatcher(Set<String> names, Iterable<String> relaxedTargetNames)
/*     */   {
/* 322 */     if ((this.ignoreUnknownFields) && (!isMapTarget()))
/*     */     {
/*     */ 
/* 325 */       return new DefaultPropertyNamePatternsMatcher(EXACT_DELIMITERS, true, names);
/*     */     }
/* 327 */     if (relaxedTargetNames != null)
/*     */     {
/*     */ 
/*     */ 
/* 331 */       Set<String> relaxedNames = new HashSet();
/* 332 */       for (String relaxedTargetName : relaxedTargetNames) {
/* 333 */         relaxedNames.add(relaxedTargetName);
/*     */       }
/* 335 */       return new DefaultPropertyNamePatternsMatcher(TARGET_NAME_DELIMITERS, true, relaxedNames);
/*     */     }
/*     */     
/*     */ 
/* 339 */     return PropertyNamePatternsMatcher.ALL;
/*     */   }
/*     */   
/*     */   private boolean isMapTarget() {
/* 343 */     return (this.target != null) && (Map.class.isAssignableFrom(this.target.getClass()));
/*     */   }
/*     */   
/*     */   private void checkForBindingErrors(RelaxedDataBinder dataBinder) throws BindException
/*     */   {
/* 348 */     BindingResult errors = dataBinder.getBindingResult();
/* 349 */     if (errors.hasErrors()) {
/* 350 */       logger.error("Properties configuration failed validation");
/* 351 */       for (ObjectError error : errors.getAllErrors()) {
/* 352 */         logger.error(this.messageSource != null ? this.messageSource
/*     */         
/* 354 */           .getMessage(error, 
/* 355 */           Locale.getDefault()) + " (" + error + ")" : error);
/*     */       }
/*     */       
/*     */ 
/* 358 */       if (this.exceptionIfInvalid) {
/* 359 */         throw new BindException(errors);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void customizeBinder(DataBinder dataBinder) {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\PropertiesConfigurationFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */