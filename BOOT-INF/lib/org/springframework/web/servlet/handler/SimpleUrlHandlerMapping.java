/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleUrlHandlerMapping
/*     */   extends AbstractUrlHandlerMapping
/*     */ {
/*  57 */   private final Map<String, Object> urlMap = new LinkedHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMappings(Properties mappings)
/*     */   {
/*  69 */     CollectionUtils.mergePropertiesIntoMap(mappings, this.urlMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUrlMap(Map<String, ?> urlMap)
/*     */   {
/*  81 */     this.urlMap.putAll(urlMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, ?> getUrlMap()
/*     */   {
/*  92 */     return this.urlMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initApplicationContext()
/*     */     throws BeansException
/*     */   {
/* 102 */     super.initApplicationContext();
/* 103 */     registerHandlers(this.urlMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void registerHandlers(Map<String, Object> urlMap)
/*     */     throws BeansException
/*     */   {
/* 113 */     if (urlMap.isEmpty()) {
/* 114 */       this.logger.warn("Neither 'urlMap' nor 'mappings' set on SimpleUrlHandlerMapping");
/*     */     }
/*     */     else {
/* 117 */       for (Map.Entry<String, Object> entry : urlMap.entrySet()) {
/* 118 */         String url = (String)entry.getKey();
/* 119 */         Object handler = entry.getValue();
/*     */         
/* 121 */         if (!url.startsWith("/")) {
/* 122 */           url = "/" + url;
/*     */         }
/*     */         
/* 125 */         if ((handler instanceof String)) {
/* 126 */           handler = ((String)handler).trim();
/*     */         }
/* 128 */         registerHandler(url, handler);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\SimpleUrlHandlerMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */