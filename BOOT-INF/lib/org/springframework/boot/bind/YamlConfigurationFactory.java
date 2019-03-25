/*     */ package org.springframework.boot.bind;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceAware;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StreamUtils;
/*     */ import org.springframework.validation.BeanPropertyBindingResult;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.validation.ObjectError;
/*     */ import org.springframework.validation.Validator;
/*     */ import org.yaml.snakeyaml.Yaml;
/*     */ import org.yaml.snakeyaml.constructor.Constructor;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
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
/*     */ public class YamlConfigurationFactory<T>
/*     */   implements FactoryBean<T>, MessageSourceAware, InitializingBean
/*     */ {
/*  55 */   private static final Log logger = LogFactory.getLog(YamlConfigurationFactory.class);
/*     */   
/*     */   private final Class<?> type;
/*     */   
/*     */   private boolean exceptionIfInvalid;
/*     */   
/*     */   private String yaml;
/*     */   
/*     */   private Resource resource;
/*     */   
/*     */   private T configuration;
/*     */   
/*     */   private Validator validator;
/*     */   
/*     */   private MessageSource messageSource;
/*     */   
/*  71 */   private Map<Class<?>, Map<String, String>> propertyAliases = Collections.emptyMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public YamlConfigurationFactory(Class<?> type)
/*     */   {
/*  79 */     Assert.notNull(type, "type must not be null");
/*  80 */     this.type = type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMessageSource(MessageSource messageSource)
/*     */   {
/*  89 */     this.messageSource = messageSource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPropertyAliases(Map<Class<?>, Map<String, String>> propertyAliases)
/*     */   {
/*  97 */     this.propertyAliases = new HashMap(propertyAliases);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setYaml(String yaml)
/*     */   {
/* 106 */     this.yaml = yaml;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResource(Resource resource)
/*     */   {
/* 114 */     this.resource = resource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValidator(Validator validator)
/*     */   {
/* 122 */     this.validator = validator;
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
/* 134 */     this.exceptionIfInvalid = exceptionIfInvalid;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet()
/*     */     throws Exception
/*     */   {
/* 140 */     if (this.yaml == null) {
/* 141 */       Assert.state(this.resource != null, "Resource should not be null");
/* 142 */       this.yaml = StreamUtils.copyToString(this.resource.getInputStream(), 
/* 143 */         Charset.defaultCharset());
/*     */     }
/* 145 */     Assert.state(this.yaml != null, "Yaml document should not be null: either set it directly or set the resource to load it from");
/*     */     try
/*     */     {
/* 148 */       if (logger.isTraceEnabled()) {
/* 149 */         logger.trace(String.format("Yaml document is %n%s", new Object[] { this.yaml }));
/*     */       }
/* 151 */       Constructor constructor = new YamlJavaBeanPropertyConstructor(this.type, this.propertyAliases);
/*     */       
/* 153 */       this.configuration = new Yaml(constructor).load(this.yaml);
/* 154 */       if (this.validator != null) {
/* 155 */         validate();
/*     */       }
/*     */     }
/*     */     catch (YAMLException ex) {
/* 159 */       if (this.exceptionIfInvalid) {
/* 160 */         throw ex;
/*     */       }
/* 162 */       logger.error("Failed to load YAML validation bean. Your YAML file may be invalid.", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private void validate() throws BindException
/*     */   {
/* 168 */     BindingResult errors = new BeanPropertyBindingResult(this.configuration, "configuration");
/*     */     
/* 170 */     this.validator.validate(this.configuration, errors);
/* 171 */     if (errors.hasErrors()) {
/* 172 */       logger.error("YAML configuration failed validation");
/* 173 */       for (ObjectError error : errors.getAllErrors()) {
/* 174 */         logger.error(getErrorMessage(error));
/*     */       }
/* 176 */       if (this.exceptionIfInvalid) {
/* 177 */         BindException summary = new BindException(errors);
/* 178 */         throw summary;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private Object getErrorMessage(ObjectError error) {
/* 184 */     if (this.messageSource != null) {
/* 185 */       Locale locale = Locale.getDefault();
/* 186 */       return this.messageSource.getMessage(error, locale) + " (" + error + ")";
/*     */     }
/* 188 */     return error;
/*     */   }
/*     */   
/*     */   public Class<?> getObjectType()
/*     */   {
/* 193 */     if (this.configuration == null) {
/* 194 */       return Object.class;
/*     */     }
/* 196 */     return this.configuration.getClass();
/*     */   }
/*     */   
/*     */   public boolean isSingleton()
/*     */   {
/* 201 */     return true;
/*     */   }
/*     */   
/*     */   public T getObject() throws Exception
/*     */   {
/* 206 */     if (this.configuration == null) {
/* 207 */       afterPropertiesSet();
/*     */     }
/* 209 */     return (T)this.configuration;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\YamlConfigurationFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */