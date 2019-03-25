/*     */ package org.springframework.boot.autoconfigure.jackson;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonCreator.Mode;
/*     */ import com.fasterxml.jackson.databind.Module;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.module.SimpleModule;
/*     */ import com.fasterxml.jackson.datatype.joda.cfg.JacksonJodaDateFormat;
/*     */ import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;
/*     */ import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
/*     */ import java.lang.reflect.Field;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.joda.time.DateTime;
/*     */ import org.joda.time.format.DateTimeFormat;
/*     */ import org.joda.time.format.DateTimeFormatter;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnJava.JavaVersion;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.boot.jackson.JsonComponentModule;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.Primary;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ @Configuration
/*     */ @ConditionalOnClass({ObjectMapper.class})
/*     */ public class JacksonAutoConfiguration
/*     */ {
/*     */   @Bean
/*     */   public JsonComponentModule jsonComponentModule()
/*     */   {
/*  83 */     return new JsonComponentModule();
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass({ObjectMapper.class, Jackson2ObjectMapperBuilder.class})
/*     */   static class JacksonObjectMapperConfiguration
/*     */   {
/*     */     @Bean
/*     */     @Primary
/*     */     @ConditionalOnMissingBean({ObjectMapper.class})
/*     */     public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
/*  94 */       return builder.createXmlMapper(false).build();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({Jackson2ObjectMapperBuilder.class, DateTime.class, DateTimeSerializer.class, JacksonJodaDateFormat.class})
/*     */   static class JodaDateTimeJacksonConfiguration
/*     */   {
/* 105 */     private static final Log logger = LogFactory.getLog(JodaDateTimeJacksonConfiguration.class);
/*     */     private final JacksonProperties jacksonProperties;
/*     */     
/*     */     JodaDateTimeJacksonConfiguration(JacksonProperties jacksonProperties)
/*     */     {
/* 110 */       this.jacksonProperties = jacksonProperties;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     public SimpleModule jodaDateTimeSerializationModule() {
/* 115 */       SimpleModule module = new SimpleModule();
/* 116 */       JacksonJodaDateFormat jacksonJodaFormat = getJacksonJodaDateFormat();
/* 117 */       if (jacksonJodaFormat != null) {
/* 118 */         module.addSerializer(DateTime.class, new DateTimeSerializer(jacksonJodaFormat));
/*     */       }
/*     */       
/* 121 */       return module;
/*     */     }
/*     */     
/*     */     private JacksonJodaDateFormat getJacksonJodaDateFormat() {
/* 125 */       if (this.jacksonProperties.getJodaDateTimeFormat() != null) {
/* 126 */         return new JacksonJodaDateFormat(
/* 127 */           DateTimeFormat.forPattern(this.jacksonProperties.getJodaDateTimeFormat())
/* 128 */           .withZoneUTC());
/*     */       }
/* 130 */       if (this.jacksonProperties.getDateFormat() != null) {
/*     */         try {
/* 132 */           return new JacksonJodaDateFormat(
/* 133 */             DateTimeFormat.forPattern(this.jacksonProperties.getDateFormat())
/* 134 */             .withZoneUTC());
/*     */         }
/*     */         catch (IllegalArgumentException ex) {
/* 137 */           if (logger.isWarnEnabled()) {
/* 138 */             logger.warn("spring.jackson.date-format could not be used to configure formatting of Joda's DateTime. You may want to configure spring.jackson.joda-date-time-format as well.");
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 145 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnJava(ConditionalOnJava.JavaVersion.EIGHT)
/*     */   @ConditionalOnClass({ParameterNamesModule.class})
/*     */   static class ParameterNamesModuleConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({ParameterNamesModule.class})
/*     */     public ParameterNamesModule parameterNamesModule()
/*     */     {
/* 158 */       return new ParameterNamesModule(JsonCreator.Mode.DEFAULT);
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass({ObjectMapper.class, Jackson2ObjectMapperBuilder.class})
/*     */   static class JacksonObjectMapperBuilderConfiguration
/*     */   {
/*     */     private final ApplicationContext applicationContext;
/*     */     
/*     */     JacksonObjectMapperBuilderConfiguration(ApplicationContext applicationContext)
/*     */     {
/* 170 */       this.applicationContext = applicationContext;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({Jackson2ObjectMapperBuilder.class})
/*     */     public Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder(List<Jackson2ObjectMapperBuilderCustomizer> customizers)
/*     */     {
/* 177 */       Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
/* 178 */       builder.applicationContext(this.applicationContext);
/* 179 */       customize(builder, customizers);
/* 180 */       return builder;
/*     */     }
/*     */     
/*     */     private void customize(Jackson2ObjectMapperBuilder builder, List<Jackson2ObjectMapperBuilderCustomizer> customizers)
/*     */     {
/* 185 */       for (Jackson2ObjectMapperBuilderCustomizer customizer : customizers) {
/* 186 */         customizer.customize(builder);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({ObjectMapper.class, Jackson2ObjectMapperBuilder.class})
/*     */   @EnableConfigurationProperties({JacksonProperties.class})
/*     */   static class Jackson2ObjectMapperBuilderCustomizerConfiguration
/*     */   {
/*     */     @Bean
/*     */     public StandardJackson2ObjectMapperBuilderCustomizer standardJacksonObjectMapperBuilderCustomizer(ApplicationContext applicationContext, JacksonProperties jacksonProperties)
/*     */     {
/* 201 */       return new StandardJackson2ObjectMapperBuilderCustomizer(applicationContext, jacksonProperties);
/*     */     }
/*     */     
/*     */ 
/*     */     private static final class StandardJackson2ObjectMapperBuilderCustomizer
/*     */       implements Jackson2ObjectMapperBuilderCustomizer, Ordered
/*     */     {
/*     */       private final ApplicationContext applicationContext;
/*     */       
/*     */       private final JacksonProperties jacksonProperties;
/*     */       
/*     */ 
/*     */       StandardJackson2ObjectMapperBuilderCustomizer(ApplicationContext applicationContext, JacksonProperties jacksonProperties)
/*     */       {
/* 215 */         this.applicationContext = applicationContext;
/* 216 */         this.jacksonProperties = jacksonProperties;
/*     */       }
/*     */       
/*     */       public int getOrder()
/*     */       {
/* 221 */         return 0;
/*     */       }
/*     */       
/*     */ 
/*     */       public void customize(Jackson2ObjectMapperBuilder builder)
/*     */       {
/* 227 */         if (this.jacksonProperties.getDefaultPropertyInclusion() != null) {
/* 228 */           builder.serializationInclusion(this.jacksonProperties
/* 229 */             .getDefaultPropertyInclusion());
/*     */         }
/* 231 */         if (this.jacksonProperties.getTimeZone() != null) {
/* 232 */           builder.timeZone(this.jacksonProperties.getTimeZone());
/*     */         }
/* 234 */         configureFeatures(builder, this.jacksonProperties.getDeserialization());
/* 235 */         configureFeatures(builder, this.jacksonProperties.getSerialization());
/* 236 */         configureFeatures(builder, this.jacksonProperties.getMapper());
/* 237 */         configureFeatures(builder, this.jacksonProperties.getParser());
/* 238 */         configureFeatures(builder, this.jacksonProperties.getGenerator());
/* 239 */         configureDateFormat(builder);
/* 240 */         configurePropertyNamingStrategy(builder);
/* 241 */         configureModules(builder);
/* 242 */         configureLocale(builder);
/*     */       }
/*     */       
/*     */       private void configureFeatures(Jackson2ObjectMapperBuilder builder, Map<?, Boolean> features)
/*     */       {
/* 247 */         for (Map.Entry<?, Boolean> entry : features.entrySet()) {
/* 248 */           if ((entry.getValue() != null) && (((Boolean)entry.getValue()).booleanValue())) {
/* 249 */             builder.featuresToEnable(new Object[] { entry.getKey() });
/*     */           }
/*     */           else {
/* 252 */             builder.featuresToDisable(new Object[] { entry.getKey() });
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */       private void configureDateFormat(Jackson2ObjectMapperBuilder builder)
/*     */       {
/* 260 */         String dateFormat = this.jacksonProperties.getDateFormat();
/* 261 */         if (dateFormat != null) {
/*     */           try {
/* 263 */             Class<?> dateFormatClass = ClassUtils.forName(dateFormat, null);
/* 264 */             builder.dateFormat(
/* 265 */               (DateFormat)BeanUtils.instantiateClass(dateFormatClass));
/*     */           }
/*     */           catch (ClassNotFoundException ex) {
/* 268 */             SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 273 */             TimeZone timeZone = this.jacksonProperties.getTimeZone();
/* 274 */             if (timeZone == null)
/*     */             {
/* 276 */               timeZone = new ObjectMapper().getSerializationConfig().getTimeZone();
/*     */             }
/* 278 */             simpleDateFormat.setTimeZone(timeZone);
/* 279 */             builder.dateFormat(simpleDateFormat);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       private void configurePropertyNamingStrategy(Jackson2ObjectMapperBuilder builder)
/*     */       {
/* 290 */         String strategy = this.jacksonProperties.getPropertyNamingStrategy();
/* 291 */         if (strategy != null) {
/*     */           try {
/* 293 */             configurePropertyNamingStrategyClass(builder, 
/* 294 */               ClassUtils.forName(strategy, null));
/*     */           }
/*     */           catch (ClassNotFoundException ex) {
/* 297 */             configurePropertyNamingStrategyField(builder, strategy);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */       private void configurePropertyNamingStrategyClass(Jackson2ObjectMapperBuilder builder, Class<?> propertyNamingStrategyClass)
/*     */       {
/* 305 */         builder.propertyNamingStrategy(
/* 306 */           (PropertyNamingStrategy)BeanUtils.instantiateClass(propertyNamingStrategyClass));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       private void configurePropertyNamingStrategyField(Jackson2ObjectMapperBuilder builder, String fieldName)
/*     */       {
/* 313 */         Field field = ReflectionUtils.findField(PropertyNamingStrategy.class, fieldName, PropertyNamingStrategy.class);
/*     */         
/* 315 */         Assert.notNull(field, "Constant named '" + fieldName + "' not found on " + PropertyNamingStrategy.class
/* 316 */           .getName());
/*     */         try {
/* 318 */           builder.propertyNamingStrategy(
/* 319 */             (PropertyNamingStrategy)field.get(null));
/*     */         }
/*     */         catch (Exception ex) {
/* 322 */           throw new IllegalStateException(ex);
/*     */         }
/*     */       }
/*     */       
/*     */       private void configureModules(Jackson2ObjectMapperBuilder builder) {
/* 327 */         Collection<Module> moduleBeans = getBeans(this.applicationContext, Module.class);
/*     */         
/* 329 */         builder.modulesToInstall(
/* 330 */           (Module[])moduleBeans.toArray(new Module[moduleBeans.size()]));
/*     */       }
/*     */       
/*     */       private void configureLocale(Jackson2ObjectMapperBuilder builder) {
/* 334 */         Locale locale = this.jacksonProperties.getLocale();
/* 335 */         if (locale != null) {
/* 336 */           builder.locale(locale);
/*     */         }
/*     */       }
/*     */       
/*     */       private static <T> Collection<T> getBeans(ListableBeanFactory beanFactory, Class<T> type)
/*     */       {
/* 342 */         return 
/* 343 */           BeanFactoryUtils.beansOfTypeIncludingAncestors(beanFactory, type).values();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jackson\JacksonAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */