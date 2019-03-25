/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.core.env.ConfigurablePropertyResolver;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertiesPropertySource;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.core.env.PropertySources;
/*     */ import org.springframework.core.env.PropertySourcesPropertyResolver;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringValueResolver;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertySourcesPlaceholderConfigurer
/*     */   extends PlaceholderConfigurerSupport
/*     */   implements EnvironmentAware
/*     */ {
/*     */   public static final String LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME = "localProperties";
/*     */   public static final String ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME = "environmentProperties";
/*     */   private MutablePropertySources propertySources;
/*     */   private PropertySources appliedPropertySources;
/*     */   private Environment environment;
/*     */   
/*     */   public void setPropertySources(PropertySources propertySources)
/*     */   {
/*  92 */     this.propertySources = new MutablePropertySources(propertySources);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnvironment(Environment environment)
/*     */   {
/* 103 */     this.environment = environment;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*     */     throws BeansException
/*     */   {
/* 125 */     if (this.propertySources == null) {
/* 126 */       this.propertySources = new MutablePropertySources();
/* 127 */       if (this.environment != null) {
/* 128 */         this.propertySources.addLast(new PropertySource("environmentProperties", this.environment)
/*     */         {
/*     */           public String getProperty(String key)
/*     */           {
/* 132 */             return ((Environment)this.source).getProperty(key);
/*     */           }
/*     */         });
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 139 */         PropertySource<?> localPropertySource = new PropertiesPropertySource("localProperties", mergeProperties());
/* 140 */         if (this.localOverride) {
/* 141 */           this.propertySources.addFirst(localPropertySource);
/*     */         }
/*     */         else {
/* 144 */           this.propertySources.addLast(localPropertySource);
/*     */         }
/*     */       }
/*     */       catch (IOException ex) {
/* 148 */         throw new BeanInitializationException("Could not load properties", ex);
/*     */       }
/*     */     }
/*     */     
/* 152 */     processProperties(beanFactory, new PropertySourcesPropertyResolver(this.propertySources));
/* 153 */     this.appliedPropertySources = this.propertySources;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, final ConfigurablePropertyResolver propertyResolver)
/*     */     throws BeansException
/*     */   {
/* 163 */     propertyResolver.setPlaceholderPrefix(this.placeholderPrefix);
/* 164 */     propertyResolver.setPlaceholderSuffix(this.placeholderSuffix);
/* 165 */     propertyResolver.setValueSeparator(this.valueSeparator);
/*     */     
/* 167 */     StringValueResolver valueResolver = new StringValueResolver()
/*     */     {
/*     */ 
/*     */       public String resolveStringValue(String strVal)
/*     */       {
/* 172 */         String resolved = PropertySourcesPlaceholderConfigurer.this.ignoreUnresolvablePlaceholders ? propertyResolver.resolvePlaceholders(strVal) : propertyResolver.resolveRequiredPlaceholders(strVal);
/* 173 */         if (PropertySourcesPlaceholderConfigurer.this.trimValues) {
/* 174 */           resolved = resolved.trim();
/*     */         }
/* 176 */         return resolved.equals(PropertySourcesPlaceholderConfigurer.this.nullValue) ? null : resolved;
/*     */       }
/*     */       
/* 179 */     };
/* 180 */     doProcessProperties(beanFactoryToProcess, valueResolver);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
/*     */   {
/* 191 */     throw new UnsupportedOperationException("Call processProperties(ConfigurableListableBeanFactory, ConfigurablePropertyResolver) instead");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertySources getAppliedPropertySources()
/*     */     throws IllegalStateException
/*     */   {
/* 203 */     Assert.state(this.appliedPropertySources != null, "PropertySources have not get been applied");
/* 204 */     return this.appliedPropertySources;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\support\PropertySourcesPlaceholderConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */