/*     */ package org.springframework.boot.env;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.boot.SpringApplication;
/*     */ import org.springframework.boot.json.JsonParser;
/*     */ import org.springframework.boot.json.JsonParserFactory;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.MapPropertySource;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class SpringApplicationJsonEnvironmentPostProcessor
/*     */   implements EnvironmentPostProcessor, Ordered
/*     */ {
/*     */   private static final String SERVLET_ENVIRONMENT_CLASS = "org.springframework.web.context.support.StandardServletEnvironment";
/*     */   public static final int DEFAULT_ORDER = -2147483643;
/*  62 */   private static final Log logger = LogFactory.getLog(SpringApplicationJsonEnvironmentPostProcessor.class);
/*     */   
/*  64 */   private int order = -2147483643;
/*     */   
/*     */   public int getOrder()
/*     */   {
/*  68 */     return this.order;
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/*  72 */     this.order = order;
/*     */   }
/*     */   
/*     */ 
/*     */   public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application)
/*     */   {
/*  78 */     String json = environment.resolvePlaceholders("${spring.application.json:${SPRING_APPLICATION_JSON:}}");
/*     */     
/*  80 */     if (StringUtils.hasText(json)) {
/*  81 */       processJson(environment, json);
/*     */     }
/*     */   }
/*     */   
/*     */   private void processJson(ConfigurableEnvironment environment, String json) {
/*     */     try {
/*  87 */       JsonParser parser = JsonParserFactory.getJsonParser();
/*  88 */       Map<String, Object> map = parser.parseMap(json);
/*  89 */       if (!map.isEmpty()) {
/*  90 */         addJsonPropertySource(environment, new MapPropertySource("spring.application.json", 
/*  91 */           flatten(map)));
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/*  95 */       logger.warn("Cannot parse JSON for spring.application.json: " + json, ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<String, Object> flatten(Map<String, Object> map)
/*     */   {
/* 105 */     Map<String, Object> result = new LinkedHashMap();
/* 106 */     flatten(null, result, map);
/* 107 */     return result;
/*     */   }
/*     */   
/*     */   private void flatten(String prefix, Map<String, Object> result, Map<String, Object> map)
/*     */   {
/* 112 */     prefix = prefix + ".";
/* 113 */     for (Map.Entry<String, Object> entry : map.entrySet()) {
/* 114 */       extract(prefix + (String)entry.getKey(), result, entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private void extract(String name, Map<String, Object> result, Object value)
/*     */   {
/* 120 */     if ((value instanceof Map)) {
/* 121 */       flatten(name, result, (Map)value);
/*     */     } else { int index;
/* 123 */       if ((value instanceof Collection)) {
/* 124 */         index = 0;
/* 125 */         for (Object object : (Collection)value) {
/* 126 */           extract(name + "[" + index + "]", result, object);
/* 127 */           index++;
/*     */         }
/*     */       }
/*     */       else {
/* 131 */         result.put(name, value);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void addJsonPropertySource(ConfigurableEnvironment environment, PropertySource<?> source) {
/* 137 */     MutablePropertySources sources = environment.getPropertySources();
/* 138 */     String name = findPropertySource(sources);
/* 139 */     if (sources.contains(name)) {
/* 140 */       sources.addBefore(name, source);
/*     */     }
/*     */     else {
/* 143 */       sources.addFirst(source);
/*     */     }
/*     */   }
/*     */   
/*     */   private String findPropertySource(MutablePropertySources sources) {
/* 148 */     if ((ClassUtils.isPresent("org.springframework.web.context.support.StandardServletEnvironment", null)) && 
/* 149 */       (sources.contains("jndiProperties"))) {
/* 150 */       return "jndiProperties";
/*     */     }
/*     */     
/* 153 */     return "systemProperties";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\env\SpringApplicationJsonEnvironmentPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */