/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.cors.CorsConfiguration;
/*     */ import org.springframework.web.cors.CorsConfigurationSource;
/*     */ import org.springframework.web.servlet.DispatcherServlet;
/*     */ import org.springframework.web.servlet.HandlerExecutionChain;
/*     */ import org.springframework.web.servlet.HandlerInterceptor;
/*     */ import org.springframework.web.servlet.HandlerMapping;
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
/*     */ public class HandlerMappingIntrospector
/*     */   implements CorsConfigurationSource, ApplicationContextAware, InitializingBean
/*     */ {
/*     */   private ApplicationContext applicationContext;
/*     */   private List<HandlerMapping> handlerMappings;
/*     */   
/*     */   public HandlerMappingIntrospector() {}
/*     */   
/*     */   @Deprecated
/*     */   public HandlerMappingIntrospector(ApplicationContext context)
/*     */   {
/*  82 */     this.handlerMappings = initHandlerMappings(context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<HandlerMapping> getHandlerMappings()
/*     */   {
/*  90 */     return this.handlerMappings != null ? this.handlerMappings : Collections.emptyList();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setApplicationContext(ApplicationContext applicationContext)
/*     */   {
/*  96 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet()
/*     */   {
/* 101 */     if (this.handlerMappings == null) {
/* 102 */       Assert.notNull(this.applicationContext, "No ApplicationContext");
/* 103 */       this.handlerMappings = initHandlerMappings(this.applicationContext);
/*     */     }
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
/*     */   public MatchableHandlerMapping getMatchableHandlerMapping(HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 119 */     Assert.notNull(this.handlerMappings, "Handler mappings not initialized");
/* 120 */     HttpServletRequest wrapper = new RequestAttributeChangeIgnoringWrapper(request);
/* 121 */     for (HandlerMapping handlerMapping : this.handlerMappings) {
/* 122 */       Object handler = handlerMapping.getHandler(wrapper);
/* 123 */       if (handler != null)
/*     */       {
/*     */ 
/* 126 */         if ((handlerMapping instanceof MatchableHandlerMapping)) {
/* 127 */           return (MatchableHandlerMapping)handlerMapping;
/*     */         }
/* 129 */         throw new IllegalStateException("HandlerMapping is not a MatchableHandlerMapping");
/*     */       } }
/* 131 */     return null;
/*     */   }
/*     */   
/*     */   public CorsConfiguration getCorsConfiguration(HttpServletRequest request)
/*     */   {
/* 136 */     Assert.notNull(this.handlerMappings, "Handler mappings not initialized");
/* 137 */     HttpServletRequest wrapper = new RequestAttributeChangeIgnoringWrapper(request);
/* 138 */     for (HandlerMapping handlerMapping : this.handlerMappings) {
/* 139 */       HandlerExecutionChain handler = null;
/*     */       try {
/* 141 */         handler = handlerMapping.getHandler(wrapper);
/*     */       }
/*     */       catch (Exception localException) {}
/*     */       
/*     */ 
/* 146 */       if (handler != null)
/*     */       {
/*     */ 
/* 149 */         if (handler.getInterceptors() != null) {
/* 150 */           for (HandlerInterceptor interceptor : handler.getInterceptors()) {
/* 151 */             if ((interceptor instanceof CorsConfigurationSource)) {
/* 152 */               return ((CorsConfigurationSource)interceptor).getCorsConfiguration(wrapper);
/*     */             }
/*     */           }
/*     */         }
/* 156 */         if ((handler.getHandler() instanceof CorsConfigurationSource))
/* 157 */           return ((CorsConfigurationSource)handler.getHandler()).getCorsConfiguration(wrapper);
/*     */       }
/*     */     }
/* 160 */     return null;
/*     */   }
/*     */   
/*     */   private static List<HandlerMapping> initHandlerMappings(ApplicationContext applicationContext)
/*     */   {
/* 165 */     Map<String, HandlerMapping> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, HandlerMapping.class, true, false);
/*     */     
/* 167 */     if (!beans.isEmpty()) {
/* 168 */       List<HandlerMapping> mappings = new ArrayList(beans.values());
/* 169 */       AnnotationAwareOrderComparator.sort(mappings);
/* 170 */       return Collections.unmodifiableList(mappings);
/*     */     }
/* 172 */     return Collections.unmodifiableList(initFallback(applicationContext));
/*     */   }
/*     */   
/*     */   private static List<HandlerMapping> initFallback(ApplicationContext applicationContext)
/*     */   {
/* 177 */     String path = "DispatcherServlet.properties";
/*     */     try {
/* 179 */       Resource resource = new ClassPathResource(path, DispatcherServlet.class);
/* 180 */       props = PropertiesLoaderUtils.loadProperties(resource);
/*     */     } catch (IOException ex) {
/*     */       Properties props;
/* 183 */       throw new IllegalStateException("Could not load '" + path + "': " + ex.getMessage());
/*     */     }
/*     */     Properties props;
/* 186 */     String value = props.getProperty(HandlerMapping.class.getName());
/* 187 */     String[] names = StringUtils.commaDelimitedListToStringArray(value);
/* 188 */     List<HandlerMapping> result = new ArrayList(names.length);
/* 189 */     for (String name : names) {
/*     */       try {
/* 191 */         Class<?> clazz = ClassUtils.forName(name, DispatcherServlet.class.getClassLoader());
/* 192 */         Object mapping = applicationContext.getAutowireCapableBeanFactory().createBean(clazz);
/* 193 */         result.add((HandlerMapping)mapping);
/*     */       }
/*     */       catch (ClassNotFoundException ex) {
/* 196 */         throw new IllegalStateException("Could not find default HandlerMapping [" + name + "]");
/*     */       }
/*     */     }
/* 199 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class RequestAttributeChangeIgnoringWrapper
/*     */     extends HttpServletRequestWrapper
/*     */   {
/*     */     public RequestAttributeChangeIgnoringWrapper(HttpServletRequest request)
/*     */     {
/* 209 */       super();
/*     */     }
/*     */     
/*     */     public void setAttribute(String name, Object value) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\HandlerMappingIntrospector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */