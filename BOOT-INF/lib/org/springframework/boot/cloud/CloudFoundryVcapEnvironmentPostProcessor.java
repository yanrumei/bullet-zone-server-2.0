/*     */ package org.springframework.boot.cloud;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.boot.SpringApplication;
/*     */ import org.springframework.boot.env.EnvironmentPostProcessor;
/*     */ import org.springframework.boot.json.JsonParser;
/*     */ import org.springframework.boot.json.JsonParserFactory;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertiesPropertySource;
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
/*     */ public class CloudFoundryVcapEnvironmentPostProcessor
/*     */   implements EnvironmentPostProcessor, Ordered
/*     */ {
/*  96 */   private static final Log logger = LogFactory.getLog(CloudFoundryVcapEnvironmentPostProcessor.class);
/*     */   
/*     */ 
/*     */   private static final String VCAP_APPLICATION = "VCAP_APPLICATION";
/*     */   
/*     */   private static final String VCAP_SERVICES = "VCAP_SERVICES";
/*     */   
/* 103 */   private int order = -2147483639;
/*     */   
/* 105 */   private final JsonParser parser = JsonParserFactory.getJsonParser();
/*     */   
/*     */   public void setOrder(int order) {
/* 108 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 113 */     return this.order;
/*     */   }
/*     */   
/*     */ 
/*     */   public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application)
/*     */   {
/* 119 */     if (CloudPlatform.CLOUD_FOUNDRY.isActive(environment)) {
/* 120 */       Properties properties = new Properties();
/* 121 */       addWithPrefix(properties, getPropertiesFromApplication(environment), "vcap.application.");
/*     */       
/* 123 */       addWithPrefix(properties, getPropertiesFromServices(environment), "vcap.services.");
/*     */       
/* 125 */       MutablePropertySources propertySources = environment.getPropertySources();
/* 126 */       if (propertySources.contains("commandLineArgs"))
/*     */       {
/* 128 */         propertySources.addAfter("commandLineArgs", new PropertiesPropertySource("vcap", properties));
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 134 */         propertySources.addFirst(new PropertiesPropertySource("vcap", properties));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void addWithPrefix(Properties properties, Properties other, String prefix) {
/* 140 */     for (String key : other.stringPropertyNames()) {
/* 141 */       String prefixed = prefix + key;
/* 142 */       properties.setProperty(prefixed, other.getProperty(key));
/*     */     }
/*     */   }
/*     */   
/*     */   private Properties getPropertiesFromApplication(Environment environment) {
/* 147 */     Properties properties = new Properties();
/*     */     try {
/* 149 */       String property = environment.getProperty("VCAP_APPLICATION", "{}");
/* 150 */       Map<String, Object> map = this.parser.parseMap(property);
/* 151 */       extractPropertiesFromApplication(properties, map);
/*     */     }
/*     */     catch (Exception ex) {
/* 154 */       logger.error("Could not parse VCAP_APPLICATION", ex);
/*     */     }
/* 156 */     return properties;
/*     */   }
/*     */   
/*     */   private Properties getPropertiesFromServices(Environment environment) {
/* 160 */     Properties properties = new Properties();
/*     */     try {
/* 162 */       String property = environment.getProperty("VCAP_SERVICES", "{}");
/* 163 */       Map<String, Object> map = this.parser.parseMap(property);
/* 164 */       extractPropertiesFromServices(properties, map);
/*     */     }
/*     */     catch (Exception ex) {
/* 167 */       logger.error("Could not parse VCAP_SERVICES", ex);
/*     */     }
/* 169 */     return properties;
/*     */   }
/*     */   
/*     */   private void extractPropertiesFromApplication(Properties properties, Map<String, Object> map)
/*     */   {
/* 174 */     if (map != null) {
/* 175 */       flatten(properties, map, "");
/*     */     }
/*     */   }
/*     */   
/*     */   private void extractPropertiesFromServices(Properties properties, Map<String, Object> map)
/*     */   {
/* 181 */     if (map != null) {
/* 182 */       for (Object services : map.values())
/*     */       {
/* 184 */         List<Object> list = (List)services;
/* 185 */         for (Object object : list)
/*     */         {
/* 187 */           Map<String, Object> service = (Map)object;
/* 188 */           String key = (String)service.get("name");
/* 189 */           if (key == null) {
/* 190 */             key = (String)service.get("label");
/*     */           }
/* 192 */           flatten(properties, service, key);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void flatten(Properties properties, Map<String, Object> input, String path)
/*     */   {
/* 200 */     for (Map.Entry<String, Object> entry : input.entrySet()) {
/* 201 */       String key = getFullKey(path, (String)entry.getKey());
/* 202 */       Object value = entry.getValue();
/* 203 */       if ((value instanceof Map))
/*     */       {
/* 205 */         flatten(properties, (Map)value, key);
/*     */       } else { int count;
/* 207 */         if ((value instanceof Collection))
/*     */         {
/* 209 */           Collection<Object> collection = (Collection)value;
/* 210 */           properties.put(key, 
/* 211 */             StringUtils.collectionToCommaDelimitedString(collection));
/* 212 */           count = 0;
/* 213 */           for (Object item : collection) {
/* 214 */             String itemKey = "[" + count++ + "]";
/* 215 */             flatten(properties, Collections.singletonMap(itemKey, item), key);
/*     */           }
/*     */         }
/* 218 */         else if ((value instanceof String)) {
/* 219 */           properties.put(key, value);
/*     */         }
/* 221 */         else if ((value instanceof Number)) {
/* 222 */           properties.put(key, value.toString());
/*     */         }
/* 224 */         else if ((value instanceof Boolean)) {
/* 225 */           properties.put(key, value.toString());
/*     */         }
/*     */         else {
/* 228 */           properties.put(key, value == null ? "" : value);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 234 */   private String getFullKey(String path, String key) { if (!StringUtils.hasText(path)) {
/* 235 */       return key;
/*     */     }
/* 237 */     if (key.startsWith("[")) {
/* 238 */       return path + key;
/*     */     }
/* 240 */     return path + "." + key;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\cloud\CloudFoundryVcapEnvironmentPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */