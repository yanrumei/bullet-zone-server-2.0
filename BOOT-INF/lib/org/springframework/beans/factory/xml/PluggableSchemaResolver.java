/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluggableSchemaResolver
/*     */   implements EntityResolver
/*     */ {
/*     */   public static final String DEFAULT_SCHEMA_MAPPINGS_LOCATION = "META-INF/spring.schemas";
/*  66 */   private static final Log logger = LogFactory.getLog(PluggableSchemaResolver.class);
/*     */   
/*     */ 
/*     */ 
/*     */   private final ClassLoader classLoader;
/*     */   
/*     */ 
/*     */ 
/*     */   private final String schemaMappingsLocation;
/*     */   
/*     */ 
/*     */ 
/*     */   private volatile Map<String, String> schemaMappings;
/*     */   
/*     */ 
/*     */ 
/*     */   public PluggableSchemaResolver(ClassLoader classLoader)
/*     */   {
/*  84 */     this.classLoader = classLoader;
/*  85 */     this.schemaMappingsLocation = "META-INF/spring.schemas";
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
/*     */   public PluggableSchemaResolver(ClassLoader classLoader, String schemaMappingsLocation)
/*     */   {
/*  98 */     Assert.hasText(schemaMappingsLocation, "'schemaMappingsLocation' must not be empty");
/*  99 */     this.classLoader = classLoader;
/* 100 */     this.schemaMappingsLocation = schemaMappingsLocation;
/*     */   }
/*     */   
/*     */   public InputSource resolveEntity(String publicId, String systemId) throws IOException
/*     */   {
/* 105 */     if (logger.isTraceEnabled()) {
/* 106 */       logger.trace("Trying to resolve XML entity with public id [" + publicId + "] and system id [" + systemId + "]");
/*     */     }
/*     */     
/*     */ 
/* 110 */     if (systemId != null) {
/* 111 */       String resourceLocation = (String)getSchemaMappings().get(systemId);
/* 112 */       if (resourceLocation != null) {
/* 113 */         Resource resource = new ClassPathResource(resourceLocation, this.classLoader);
/*     */         try {
/* 115 */           InputSource source = new InputSource(resource.getInputStream());
/* 116 */           source.setPublicId(publicId);
/* 117 */           source.setSystemId(systemId);
/* 118 */           if (logger.isDebugEnabled()) {
/* 119 */             logger.debug("Found XML schema [" + systemId + "] in classpath: " + resourceLocation);
/*     */           }
/* 121 */           return source;
/*     */         }
/*     */         catch (FileNotFoundException ex) {
/* 124 */           if (logger.isDebugEnabled()) {
/* 125 */             logger.debug("Couldn't find XML schema [" + systemId + "]: " + resource, ex);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 130 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Map<String, String> getSchemaMappings()
/*     */   {
/* 137 */     if (this.schemaMappings == null) {
/* 138 */       synchronized (this) {
/* 139 */         if (this.schemaMappings == null) {
/* 140 */           if (logger.isDebugEnabled()) {
/* 141 */             logger.debug("Loading schema mappings from [" + this.schemaMappingsLocation + "]");
/*     */           }
/*     */           try
/*     */           {
/* 145 */             Properties mappings = PropertiesLoaderUtils.loadAllProperties(this.schemaMappingsLocation, this.classLoader);
/* 146 */             if (logger.isDebugEnabled()) {
/* 147 */               logger.debug("Loaded schema mappings: " + mappings);
/*     */             }
/* 149 */             Map<String, String> schemaMappings = new ConcurrentHashMap(mappings.size());
/* 150 */             CollectionUtils.mergePropertiesIntoMap(mappings, schemaMappings);
/* 151 */             this.schemaMappings = schemaMappings;
/*     */           }
/*     */           catch (IOException ex) {
/* 154 */             throw new IllegalStateException("Unable to load schema mappings from location [" + this.schemaMappingsLocation + "]", ex);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 160 */     return this.schemaMappings;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 166 */     return "EntityResolver using mappings " + getSchemaMappings();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\xml\PluggableSchemaResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */