/*    */ package org.springframework.cache.config;
/*    */ 
/*    */ import org.springframework.beans.MutablePropertyValues;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*    */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CacheNamespaceHandler
/*    */   extends NamespaceHandlerSupport
/*    */ {
/*    */   static final String CACHE_MANAGER_ATTRIBUTE = "cache-manager";
/*    */   static final String DEFAULT_CACHE_MANAGER_BEAN_NAME = "cacheManager";
/*    */   
/*    */   static String extractCacheManager(Element element)
/*    */   {
/* 44 */     return element.hasAttribute("cache-manager") ? element
/* 45 */       .getAttribute("cache-manager") : "cacheManager";
/*    */   }
/*    */   
/*    */   static BeanDefinition parseKeyGenerator(Element element, BeanDefinition def)
/*    */   {
/* 50 */     String name = element.getAttribute("key-generator");
/* 51 */     if (StringUtils.hasText(name)) {
/* 52 */       def.getPropertyValues().add("keyGenerator", new RuntimeBeanReference(name.trim()));
/*    */     }
/* 54 */     return def;
/*    */   }
/*    */   
/*    */ 
/*    */   public void init()
/*    */   {
/* 60 */     registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenCacheBeanDefinitionParser());
/* 61 */     registerBeanDefinitionParser("advice", new CacheAdviceParser());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\config\CacheNamespaceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */