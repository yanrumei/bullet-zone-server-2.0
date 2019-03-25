/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.FatalBeanException;
/*     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class DefaultNamespaceHandlerResolver
/*     */   implements NamespaceHandlerResolver
/*     */ {
/*     */   public static final String DEFAULT_HANDLER_MAPPINGS_LOCATION = "META-INF/spring.handlers";
/*  58 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ClassLoader classLoader;
/*     */   
/*     */ 
/*     */ 
/*     */   private final String handlerMappingsLocation;
/*     */   
/*     */ 
/*     */ 
/*     */   private volatile Map<String, Object> handlerMappings;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultNamespaceHandlerResolver()
/*     */   {
/*  78 */     this(null, "META-INF/spring.handlers");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultNamespaceHandlerResolver(ClassLoader classLoader)
/*     */   {
/*  89 */     this(classLoader, "META-INF/spring.handlers");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultNamespaceHandlerResolver(ClassLoader classLoader, String handlerMappingsLocation)
/*     */   {
/* 100 */     Assert.notNull(handlerMappingsLocation, "Handler mappings location must not be null");
/* 101 */     this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
/* 102 */     this.handlerMappingsLocation = handlerMappingsLocation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NamespaceHandler resolve(String namespaceUri)
/*     */   {
/* 114 */     Map<String, Object> handlerMappings = getHandlerMappings();
/* 115 */     Object handlerOrClassName = handlerMappings.get(namespaceUri);
/* 116 */     if (handlerOrClassName == null) {
/* 117 */       return null;
/*     */     }
/* 119 */     if ((handlerOrClassName instanceof NamespaceHandler)) {
/* 120 */       return (NamespaceHandler)handlerOrClassName;
/*     */     }
/*     */     
/* 123 */     String className = (String)handlerOrClassName;
/*     */     try {
/* 125 */       Class<?> handlerClass = ClassUtils.forName(className, this.classLoader);
/* 126 */       if (!NamespaceHandler.class.isAssignableFrom(handlerClass))
/*     */       {
/* 128 */         throw new FatalBeanException("Class [" + className + "] for namespace [" + namespaceUri + "] does not implement the [" + NamespaceHandler.class.getName() + "] interface");
/*     */       }
/* 130 */       NamespaceHandler namespaceHandler = (NamespaceHandler)BeanUtils.instantiateClass(handlerClass);
/* 131 */       namespaceHandler.init();
/* 132 */       handlerMappings.put(namespaceUri, namespaceHandler);
/* 133 */       return namespaceHandler;
/*     */     }
/*     */     catch (ClassNotFoundException ex) {
/* 136 */       throw new FatalBeanException("NamespaceHandler class [" + className + "] for namespace [" + namespaceUri + "] not found", ex);
/*     */     }
/*     */     catch (LinkageError err)
/*     */     {
/* 140 */       throw new FatalBeanException("Invalid NamespaceHandler class [" + className + "] for namespace [" + namespaceUri + "]: problem with handler class file or dependent class", err);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<String, Object> getHandlerMappings()
/*     */   {
/* 150 */     if (this.handlerMappings == null) {
/* 151 */       synchronized (this) {
/* 152 */         if (this.handlerMappings == null) {
/*     */           try
/*     */           {
/* 155 */             Properties mappings = PropertiesLoaderUtils.loadAllProperties(this.handlerMappingsLocation, this.classLoader);
/* 156 */             if (this.logger.isDebugEnabled()) {
/* 157 */               this.logger.debug("Loaded NamespaceHandler mappings: " + mappings);
/*     */             }
/* 159 */             Map<String, Object> handlerMappings = new ConcurrentHashMap(mappings.size());
/* 160 */             CollectionUtils.mergePropertiesIntoMap(mappings, handlerMappings);
/* 161 */             this.handlerMappings = handlerMappings;
/*     */           }
/*     */           catch (IOException ex) {
/* 164 */             throw new IllegalStateException("Unable to load NamespaceHandler mappings from location [" + this.handlerMappingsLocation + "]", ex);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 170 */     return this.handlerMappings;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 176 */     return "NamespaceHandlerResolver using mappings " + getHandlerMappings();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\xml\DefaultNamespaceHandlerResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */