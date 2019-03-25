/*     */ package org.springframework.cache.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.TypedStringValue;
/*     */ import org.springframework.beans.factory.parsing.ReaderContext;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*     */ import org.springframework.beans.factory.support.ManagedList;
/*     */ import org.springframework.beans.factory.support.ManagedMap;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.cache.interceptor.CacheEvictOperation.Builder;
/*     */ import org.springframework.cache.interceptor.CacheInterceptor;
/*     */ import org.springframework.cache.interceptor.CacheOperation;
/*     */ import org.springframework.cache.interceptor.CacheOperation.Builder;
/*     */ import org.springframework.cache.interceptor.CachePutOperation.Builder;
/*     */ import org.springframework.cache.interceptor.CacheableOperation.Builder;
/*     */ import org.springframework.cache.interceptor.NameMatchCacheOperationSource;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.xml.DomUtils;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CacheAdviceParser
/*     */   extends AbstractSingleBeanDefinitionParser
/*     */ {
/*     */   private static final String CACHEABLE_ELEMENT = "cacheable";
/*     */   private static final String CACHE_EVICT_ELEMENT = "cache-evict";
/*     */   private static final String CACHE_PUT_ELEMENT = "cache-put";
/*     */   private static final String METHOD_ATTRIBUTE = "method";
/*     */   private static final String DEFS_ELEMENT = "caching";
/*     */   
/*     */   protected Class<?> getBeanClass(Element element)
/*     */   {
/*  65 */     return CacheInterceptor.class;
/*     */   }
/*     */   
/*     */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/*     */   {
/*  70 */     builder.addPropertyReference("cacheManager", CacheNamespaceHandler.extractCacheManager(element));
/*  71 */     CacheNamespaceHandler.parseKeyGenerator(element, builder.getBeanDefinition());
/*     */     
/*  73 */     List<Element> cacheDefs = DomUtils.getChildElementsByTagName(element, "caching");
/*  74 */     if (cacheDefs.size() >= 1)
/*     */     {
/*  76 */       List<RootBeanDefinition> attributeSourceDefinitions = parseDefinitionsSources(cacheDefs, parserContext);
/*  77 */       builder.addPropertyValue("cacheOperationSources", attributeSourceDefinitions);
/*     */     }
/*     */     else
/*     */     {
/*  81 */       builder.addPropertyValue("cacheOperationSources", new RootBeanDefinition("org.springframework.cache.annotation.AnnotationCacheOperationSource"));
/*     */     }
/*     */   }
/*     */   
/*     */   private List<RootBeanDefinition> parseDefinitionsSources(List<Element> definitions, ParserContext parserContext)
/*     */   {
/*  87 */     ManagedList<RootBeanDefinition> defs = new ManagedList(definitions.size());
/*     */     
/*     */ 
/*  90 */     for (Element element : definitions) {
/*  91 */       defs.add(parseDefinitionSource(element, parserContext));
/*     */     }
/*     */     
/*  94 */     return defs;
/*     */   }
/*     */   
/*     */   private RootBeanDefinition parseDefinitionSource(Element definition, ParserContext parserContext) {
/*  98 */     Props prop = new Props(definition);
/*     */     
/*     */ 
/* 101 */     ManagedMap<TypedStringValue, Collection<CacheOperation>> cacheOpMap = new ManagedMap();
/* 102 */     cacheOpMap.setSource(parserContext.extractSource(definition));
/*     */     
/* 104 */     List<Element> cacheableCacheMethods = DomUtils.getChildElementsByTagName(definition, "cacheable");
/*     */     
/* 106 */     for (Iterator localIterator = cacheableCacheMethods.iterator(); localIterator.hasNext();) { opElement = (Element)localIterator.next();
/* 107 */       String name = prop.merge(opElement, parserContext.getReaderContext());
/* 108 */       TypedStringValue nameHolder = new TypedStringValue(name);
/* 109 */       nameHolder.setSource(parserContext.extractSource(opElement));
/* 110 */       CacheableOperation.Builder builder = (CacheableOperation.Builder)prop.merge(opElement, parserContext
/* 111 */         .getReaderContext(), new CacheableOperation.Builder());
/* 112 */       builder.setUnless(getAttributeValue(opElement, "unless", ""));
/* 113 */       builder.setSync(Boolean.valueOf(getAttributeValue(opElement, "sync", "false")).booleanValue());
/*     */       
/* 115 */       Collection<CacheOperation> col = (Collection)cacheOpMap.get(nameHolder);
/* 116 */       if (col == null) {
/* 117 */         col = new ArrayList(2);
/* 118 */         cacheOpMap.put(nameHolder, col);
/*     */       }
/* 120 */       col.add(builder.build());
/*     */     }
/*     */     
/* 123 */     Object evictCacheMethods = DomUtils.getChildElementsByTagName(definition, "cache-evict");
/*     */     
/* 125 */     for (Element opElement = ((List)evictCacheMethods).iterator(); opElement.hasNext();) { opElement = (Element)opElement.next();
/* 126 */       String name = prop.merge(opElement, parserContext.getReaderContext());
/* 127 */       TypedStringValue nameHolder = new TypedStringValue(name);
/* 128 */       nameHolder.setSource(parserContext.extractSource(opElement));
/* 129 */       CacheEvictOperation.Builder builder = (CacheEvictOperation.Builder)prop.merge(opElement, parserContext
/* 130 */         .getReaderContext(), new CacheEvictOperation.Builder());
/*     */       
/* 132 */       String wide = opElement.getAttribute("all-entries");
/* 133 */       if (StringUtils.hasText(wide)) {
/* 134 */         builder.setCacheWide(Boolean.valueOf(wide.trim()).booleanValue());
/*     */       }
/*     */       
/* 137 */       String after = opElement.getAttribute("before-invocation");
/* 138 */       if (StringUtils.hasText(after)) {
/* 139 */         builder.setBeforeInvocation(Boolean.valueOf(after.trim()).booleanValue());
/*     */       }
/*     */       
/* 142 */       Collection<CacheOperation> col = (Collection)cacheOpMap.get(nameHolder);
/* 143 */       if (col == null) {
/* 144 */         col = new ArrayList(2);
/* 145 */         cacheOpMap.put(nameHolder, col);
/*     */       }
/* 147 */       col.add(builder.build());
/*     */     }
/*     */     Element opElement;
/* 150 */     List<Element> putCacheMethods = DomUtils.getChildElementsByTagName(definition, "cache-put");
/*     */     
/* 152 */     for (Element opElement : putCacheMethods) {
/* 153 */       String name = prop.merge(opElement, parserContext.getReaderContext());
/* 154 */       TypedStringValue nameHolder = new TypedStringValue(name);
/* 155 */       nameHolder.setSource(parserContext.extractSource(opElement));
/* 156 */       CachePutOperation.Builder builder = (CachePutOperation.Builder)prop.merge(opElement, parserContext
/* 157 */         .getReaderContext(), new CachePutOperation.Builder());
/* 158 */       builder.setUnless(getAttributeValue(opElement, "unless", ""));
/*     */       
/* 160 */       Collection<CacheOperation> col = (Collection)cacheOpMap.get(nameHolder);
/* 161 */       if (col == null) {
/* 162 */         col = new ArrayList(2);
/* 163 */         cacheOpMap.put(nameHolder, col);
/*     */       }
/* 165 */       col.add(builder.build());
/*     */     }
/*     */     
/* 168 */     RootBeanDefinition attributeSourceDefinition = new RootBeanDefinition(NameMatchCacheOperationSource.class);
/* 169 */     attributeSourceDefinition.setSource(parserContext.extractSource(definition));
/* 170 */     attributeSourceDefinition.getPropertyValues().add("nameMap", cacheOpMap);
/* 171 */     return attributeSourceDefinition;
/*     */   }
/*     */   
/*     */   private static String getAttributeValue(Element element, String attributeName, String defaultValue)
/*     */   {
/* 176 */     String attribute = element.getAttribute(attributeName);
/* 177 */     if (StringUtils.hasText(attribute)) {
/* 178 */       return attribute.trim();
/*     */     }
/* 180 */     return defaultValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class Props
/*     */   {
/*     */     private String key;
/*     */     
/*     */     private String keyGenerator;
/*     */     
/*     */     private String cacheManager;
/*     */     
/*     */     private String condition;
/*     */     
/*     */     private String method;
/*     */     
/*     */     private String[] caches;
/*     */     
/*     */ 
/*     */     Props(Element root)
/*     */     {
/* 202 */       String defaultCache = root.getAttribute("cache");
/* 203 */       this.key = root.getAttribute("key");
/* 204 */       this.keyGenerator = root.getAttribute("key-generator");
/* 205 */       this.cacheManager = root.getAttribute("cache-manager");
/* 206 */       this.condition = root.getAttribute("condition");
/* 207 */       this.method = root.getAttribute("method");
/*     */       
/* 209 */       if (StringUtils.hasText(defaultCache)) {
/* 210 */         this.caches = StringUtils.commaDelimitedListToStringArray(defaultCache.trim());
/*     */       }
/*     */     }
/*     */     
/*     */     <T extends CacheOperation.Builder> T merge(Element element, ReaderContext readerCtx, T builder) {
/* 215 */       String cache = element.getAttribute("cache");
/*     */       
/*     */ 
/* 218 */       String[] localCaches = this.caches;
/* 219 */       if (StringUtils.hasText(cache)) {
/* 220 */         localCaches = StringUtils.commaDelimitedListToStringArray(cache.trim());
/*     */ 
/*     */       }
/* 223 */       else if (this.caches == null) {
/* 224 */         readerCtx.error("No cache specified for " + element.getNodeName(), element);
/*     */       }
/*     */       
/* 227 */       builder.setCacheNames(localCaches);
/*     */       
/* 229 */       builder.setKey(CacheAdviceParser.getAttributeValue(element, "key", this.key));
/* 230 */       builder.setKeyGenerator(CacheAdviceParser.getAttributeValue(element, "key-generator", this.keyGenerator));
/* 231 */       builder.setCacheManager(CacheAdviceParser.getAttributeValue(element, "cache-manager", this.cacheManager));
/* 232 */       builder.setCondition(CacheAdviceParser.getAttributeValue(element, "condition", this.condition));
/*     */       
/* 234 */       if ((StringUtils.hasText(builder.getKey())) && (StringUtils.hasText(builder.getKeyGenerator())))
/*     */       {
/* 236 */         throw new IllegalStateException("Invalid cache advice configuration on '" + element.toString() + "'. Both 'key' and 'keyGenerator' attributes have been set. These attributes are mutually exclusive: either set the SpEL expression used tocompute the key at runtime or set the name of the KeyGenerator bean to use.");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 241 */       return builder;
/*     */     }
/*     */     
/*     */     String merge(Element element, ReaderContext readerCtx) {
/* 245 */       String method = element.getAttribute("method");
/* 246 */       if (StringUtils.hasText(method)) {
/* 247 */         return method.trim();
/*     */       }
/* 249 */       if (StringUtils.hasText(this.method)) {
/* 250 */         return this.method;
/*     */       }
/* 252 */       readerCtx.error("No method specified for " + element.getNodeName(), element);
/* 253 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\config\CacheAdviceParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */