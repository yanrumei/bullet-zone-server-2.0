/*     */ package org.springframework.boot.autoconfigure.web;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.springframework.http.converter.FormHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.AbstractXmlHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.web.client.RestTemplate;
/*     */ import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
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
/*     */ public class HttpMessageConverters
/*     */   implements Iterable<HttpMessageConverter<?>>
/*     */ {
/*     */   private static final List<Class<?>> NON_REPLACING_CONVERTERS;
/*     */   private final List<HttpMessageConverter<?>> converters;
/*     */   
/*     */   static
/*     */   {
/*  62 */     List<Class<?>> nonReplacingConverters = new ArrayList();
/*  63 */     addClassIfExists(nonReplacingConverters, "org.springframework.hateoas.mvc.TypeConstrainedMappingJackson2HttpMessageConverter");
/*     */     
/*  65 */     NON_REPLACING_CONVERTERS = Collections.unmodifiableList(nonReplacingConverters);
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
/*     */   public HttpMessageConverters(HttpMessageConverter<?>... additionalConverters)
/*     */   {
/*  79 */     this(Arrays.asList(additionalConverters));
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
/*     */   public HttpMessageConverters(Collection<HttpMessageConverter<?>> additionalConverters)
/*     */   {
/*  92 */     this(true, additionalConverters);
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
/*     */   public HttpMessageConverters(boolean addDefaultConverters, Collection<HttpMessageConverter<?>> converters)
/*     */   {
/* 105 */     List<HttpMessageConverter<?>> combined = getCombinedConverters(converters, addDefaultConverters ? 
/* 106 */       getDefaultConverters() : 
/* 107 */       Collections.emptyList());
/* 108 */     combined = postProcessConverters(combined);
/* 109 */     this.converters = Collections.unmodifiableList(combined);
/*     */   }
/*     */   
/*     */ 
/*     */   private List<HttpMessageConverter<?>> getCombinedConverters(Collection<HttpMessageConverter<?>> converters, List<HttpMessageConverter<?>> defaultConverters)
/*     */   {
/* 115 */     List<HttpMessageConverter<?>> combined = new ArrayList();
/* 116 */     List<HttpMessageConverter<?>> processing = new ArrayList(converters);
/*     */     
/* 118 */     for (HttpMessageConverter<?> defaultConverter : defaultConverters) {
/* 119 */       Iterator<HttpMessageConverter<?>> iterator = processing.iterator();
/* 120 */       while (iterator.hasNext()) {
/* 121 */         HttpMessageConverter<?> candidate = (HttpMessageConverter)iterator.next();
/* 122 */         if (isReplacement(defaultConverter, candidate)) {
/* 123 */           combined.add(candidate);
/* 124 */           iterator.remove();
/*     */         }
/*     */       }
/* 127 */       combined.add(defaultConverter);
/* 128 */       if ((defaultConverter instanceof AllEncompassingFormHttpMessageConverter)) {
/* 129 */         configurePartConverters((AllEncompassingFormHttpMessageConverter)defaultConverter, converters);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 134 */     combined.addAll(0, processing);
/* 135 */     return combined;
/*     */   }
/*     */   
/*     */   private boolean isReplacement(HttpMessageConverter<?> defaultConverter, HttpMessageConverter<?> candidate)
/*     */   {
/* 140 */     for (Class<?> nonReplacingConverter : NON_REPLACING_CONVERTERS) {
/* 141 */       if (nonReplacingConverter.isInstance(candidate)) {
/* 142 */         return false;
/*     */       }
/*     */     }
/* 145 */     return ClassUtils.isAssignableValue(defaultConverter.getClass(), candidate);
/*     */   }
/*     */   
/*     */ 
/*     */   private void configurePartConverters(AllEncompassingFormHttpMessageConverter formConverter, Collection<HttpMessageConverter<?>> converters)
/*     */   {
/* 151 */     List<HttpMessageConverter<?>> partConverters = extractPartConverters(formConverter);
/*     */     
/* 153 */     List<HttpMessageConverter<?>> combinedConverters = getCombinedConverters(converters, partConverters);
/*     */     
/* 155 */     combinedConverters = postProcessPartConverters(combinedConverters);
/* 156 */     formConverter.setPartConverters(combinedConverters);
/*     */   }
/*     */   
/*     */ 
/*     */   private List<HttpMessageConverter<?>> extractPartConverters(FormHttpMessageConverter formConverter)
/*     */   {
/* 162 */     Field field = ReflectionUtils.findField(FormHttpMessageConverter.class, "partConverters");
/*     */     
/* 164 */     ReflectionUtils.makeAccessible(field);
/* 165 */     return (List)ReflectionUtils.getField(field, formConverter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<HttpMessageConverter<?>> postProcessConverters(List<HttpMessageConverter<?>> converters)
/*     */   {
/* 177 */     return converters;
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
/*     */   protected List<HttpMessageConverter<?>> postProcessPartConverters(List<HttpMessageConverter<?>> converters)
/*     */   {
/* 190 */     return converters;
/*     */   }
/*     */   
/*     */   private List<HttpMessageConverter<?>> getDefaultConverters() {
/* 194 */     List<HttpMessageConverter<?>> converters = new ArrayList();
/* 195 */     if (ClassUtils.isPresent("org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport", null))
/*     */     {
/* 197 */       converters.addAll(new WebMvcConfigurationSupport() {
/*     */         public List<HttpMessageConverter<?>> defaultMessageConverters() {
/* 199 */           return super.getMessageConverters();
/*     */         }
/*     */         
/*     */       }.defaultMessageConverters());
/*     */     } else {
/* 204 */       converters.addAll(new RestTemplate().getMessageConverters());
/*     */     }
/* 206 */     reorderXmlConvertersToEnd(converters);
/* 207 */     return converters;
/*     */   }
/*     */   
/*     */   private void reorderXmlConvertersToEnd(List<HttpMessageConverter<?>> converters) {
/* 211 */     List<HttpMessageConverter<?>> xml = new ArrayList();
/* 212 */     Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
/* 213 */     while (iterator.hasNext()) {
/* 214 */       HttpMessageConverter<?> converter = (HttpMessageConverter)iterator.next();
/* 215 */       if (((converter instanceof AbstractXmlHttpMessageConverter)) || ((converter instanceof MappingJackson2XmlHttpMessageConverter)))
/*     */       {
/* 217 */         xml.add(converter);
/* 218 */         iterator.remove();
/*     */       }
/*     */     }
/* 221 */     converters.addAll(xml);
/*     */   }
/*     */   
/*     */   public Iterator<HttpMessageConverter<?>> iterator()
/*     */   {
/* 226 */     return getConverters().iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<HttpMessageConverter<?>> getConverters()
/*     */   {
/* 235 */     return this.converters;
/*     */   }
/*     */   
/*     */   private static void addClassIfExists(List<Class<?>> list, String className) {
/*     */     try {
/* 240 */       list.add(Class.forName(className));
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {}catch (NoClassDefFoundError localNoClassDefFoundError) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\HttpMessageConverters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */